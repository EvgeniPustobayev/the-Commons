package anterior.com.thecommon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import anterior.com.thecommon.ContactsActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.model.Feed;

/**
 * Created by admin on 27/09/2017.
 */

public class DialogAdapter extends BaseAdapter{

    ViewHolder viewHolder;
    Context context;
    AppCompatActivity addEventActivity;
    private static LayoutInflater inflater = null;

    public DialogAdapter(AppCompatActivity addEventActivity) {
        this.addEventActivity = addEventActivity;
        inflater = (LayoutInflater) addEventActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;
        if(vi==null)
            vi = inflater.inflate(R.layout.dialog_row, null);

        Button btn_Cancel = (Button)vi.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button btn_phone_Contacts = (Button)vi.findViewById(R.id.btn_phone_Contacts);
        btn_phone_Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
                addEventActivity.startActivityForResult(intent, 1);



            }
        });
        Button btn_Common_Contacts = (Button)vi.findViewById(R.id.btn_commons_contacts);
        btn_Common_Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(addEventActivity, ContactsActivity.class);
                addEventActivity.startActivityForResult(intent, 2);
            }
        });
        return vi;
    }
}
