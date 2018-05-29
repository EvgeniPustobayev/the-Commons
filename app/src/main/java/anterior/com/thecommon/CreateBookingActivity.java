package anterior.com.thecommon;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import anterior.com.thecommon.adapters.CreateBookingAdapter;
import anterior.com.thecommon.model.Booking;
import anterior.com.thecommon.model.Room;
import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.Const;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cloudinary.android.payload.LocalUriPayload.PROJECTION;

public class CreateBookingActivity extends AppCompatActivity {


    public ArrayList<String> arrayInviteEmails;
    private Uri uriContact;
    static int row_count = 2;
    ListView list_Invite;
    CreateBookingAdapter addEventAdapter;
    String RoomID;
    public int nBranchID;
    public boolean isEdit;
    public static Booking booking;
    public static Room room;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);



        isEdit = getIntent().getBooleanExtra("isEdit", false);
        RoomID = getIntent().getStringExtra("RoomID");
        nBranchID = getIntent().getIntExtra("nBranchID", 0);

        arrayInviteEmails = new ArrayList<>();

        addEventAdapter = new CreateBookingAdapter(this);
        list_Invite = (ListView)findViewById(R.id.list_Invite);
        list_Invite.setAdapter(addEventAdapter);
        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        if(isEdit)
            tvTitle.setText(booking.title);
        else
            tvTitle.setText(room.short_name);

        ImageButton btn_Back = (ImageButton)findViewById(R.id.btn_back);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_Add = (Button)findViewById(R.id.btn_Add);

        if(isEdit){
            btn_Add.setText("Update");
        }
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createBooking();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            uriContact = data.getData();

            String email = contactPicked(data);
            if(email.length() > 0)
                arrayInviteEmails.add(email);
//            retrieveContactNumber();
//            retrieveContactPhoto();
            addEventAdapter.dialog.dismiss();

            View v = list_Invite.getChildAt(addEventAdapter.nCurrentContact+2 -
                    list_Invite.getFirstVisiblePosition());

            if(v == null)
                return;

            EditText etEmail = v.findViewById(R.id.etEmail);
            etEmail.setText(data.getStringExtra("result"));

        }
        if(requestCode == 2 && resultCode == RESULT_OK){
            if(addEventAdapter.nCurrentContact < arrayInviteEmails.size()){
                arrayInviteEmails.remove(addEventAdapter.nCurrentContact);
                arrayInviteEmails.add(addEventAdapter.nCurrentContact, data.getStringExtra("result"));
            }else
                arrayInviteEmails.add(data.getStringExtra("result"));
            addEventAdapter.dialog.dismiss();
            View v = list_Invite.getChildAt(addEventAdapter.nCurrentContact + 2 -
                    list_Invite.getFirstVisiblePosition());

            if(v == null)
                return;

            EditText etEmail = v.findViewById(R.id.etEmail);
            etEmail.setText(data.getStringExtra("result"));
        }

    }
    private String contactPicked(Intent data) {
        ContentResolver cr = getContentResolver();
        try {
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            Cursor cur = cr.query(uri, null, null, null, null);
            cur.moveToFirst();
            // column index of the contact ID
            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            // column index of the contact name
            // column index of the phone number
            // column index of the email
            Cursor emailCur = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);
            while (emailCur.moveToNext()) {
                // This would allow you get several email addresses
                // if the email addresses were stored in an array
                String email = emailCur.getString(
                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                return(email);         //print data
            }
            emailCur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void createBooking() throws JSONException {

        View firstview = list_Invite.getChildAt(0);
        EditText etTitle = firstview.findViewById(R.id.etTitle);
        EditText etDescription = firstview.findViewById(R.id.etDescription);

        if(etTitle.getText().length() < 1 || etDescription.getText().length() < 1 || addEventAdapter.startDate == null || addEventAdapter.endDate == null){
            new SweetAlertDialog(CreateBookingActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error!")
                    .setContentText("Input all required fields")
                    .show();
            return;
        }


        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("description", etDescription.getText().toString());
        attributes.put("title", etTitle.getText().toString());


        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String strStartTime = fmt.format(addEventAdapter.startDate);
        attributes.put("start", strStartTime);
        String strEndTime = fmt.format(addEventAdapter.endDate);
        attributes.put("end", strEndTime);

        data.put("attributes",attributes);
        data.put("type","meeting-room-bookings");
        String strAttend = "";
        for(String attend : arrayInviteEmails){
            if(arrayInviteEmails.indexOf(attend) == arrayInviteEmails.size() - 1)
                strAttend = strAttend + attend;
            else
                strAttend = strAttend + attend + ",";
        }
        data.put("attendees",strAttend);
        JSONObject link = new JSONObject();
        link.put("self",String.format("%s%s", Const.BASE_URL, Const.API_BOOKROOM));
        data.put("links",link);

        jsonParam.put("data",data);
        final SweetAlertDialog loadingdialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(isEdit?"Updating...":"Booking room...");
        loadingdialog.show();
        if(isEdit){
            APIManager.getInstance(this).updateBooking(booking.id, this, jsonParam, new APIManager.CommonCallBackInterface() {
                @Override
                public void onSuccess(Object result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingdialog.hide();
                            SweetAlertDialog dialog = new SweetAlertDialog(CreateBookingActivity.this, SweetAlertDialog.SUCCESS_TYPE);

                            dialog.setTitleText("Update Successfully")
                                    .setContentText("Pending Approval!")
                                    .show();

                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    CreateBookingActivity.this.finish();
                                }
                            });


                            //
                        }
                    });
                }

                @Override
                public void onFailure(String error, int nCode) {

                }
            });
        }else {
            APIManager.getInstance(this).bookmeetingroom(jsonParam, RoomID, new APIManager.CommonCallBackInterface() {
                @Override
                public void onSuccess(Object result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingdialog.hide();
                            SweetAlertDialog dialog = new SweetAlertDialog(CreateBookingActivity.this, SweetAlertDialog.SUCCESS_TYPE);

                            dialog.setTitleText("Submit Successflly!")
                                    .setContentText("Pending Approval!")
                                    .show();

                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    CreateBookingActivity.this.finish();
                                }
                            });


                            //
                        }
                    });

                }

                @Override
                public void onFailure(final String error, int nCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingdialog.hide();
                            SweetAlertDialog dialog = new SweetAlertDialog(CreateBookingActivity.this, SweetAlertDialog.ERROR_TYPE);
                            dialog.setTitleText("Error!")
                                    .setContentText(error)
                                    .show();

                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    //                                CreateBookingActivity.this.finish();
                                }
                            });
                            //
                        }
                    });

                }
            });
        }
    }
}
