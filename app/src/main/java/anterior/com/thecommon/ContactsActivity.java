package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import anterior.com.thecommon.adapters.DirectoryAdapter;
import anterior.com.thecommon.model.CommonUser;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContactsActivity extends AppCompatActivity {
    public ArrayList<CommonUser> arraySearchUsers = new ArrayList<CommonUser>();
    DirectoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ListView list = (ListView) findViewById(R.id.listview);

        ImageButton btn_Back = (ImageButton)findViewById(R.id.btn_back);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final CommonApplication app = (CommonApplication)getApplication();
        arraySearchUsers.addAll(app.arrayUsers);

        adapter = new DirectoryAdapter(this, arraySearchUsers);
        list.setAdapter(adapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arraySearchUsers.clear();
                if (newText.length() < 1) {
                    arraySearchUsers.addAll(app.arrayUsers);
                }

                for (CommonUser user : app.arrayUsers) {
                    if (user.firstname.toLowerCase().contains(newText.toLowerCase()) || user.lastname.toLowerCase().contains(newText.toLowerCase())) {
                        arraySearchUsers.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommonApplication app = (CommonApplication) getApplication();
                CommonUser user = arraySearchUsers.get(i);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",user.email);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
