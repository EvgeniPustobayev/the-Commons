package anterior.com.thecommon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import anterior.com.thecommon.adapters.BranchesAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BranchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        ListView list_Braches = (ListView)findViewById(R.id.list_branches);

        BranchesAdapter branchesAdapter = new BranchesAdapter(this);
        list_Braches.setAdapter(branchesAdapter);

        list_Braches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BranchesActivity.this, BookingActivity.class);
                intent.putExtra("nIndex",i);
                startActivity(intent);
            }
        });

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
