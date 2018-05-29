package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.Const;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditProfileActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;

    EditText etName;
    EditText etEmail;
    EditText etPhoneNumber;
    EditText etTitle;
    EditText etDescription;

    Bitmap uploadbitmap;
    Uri imageuri;

    SweetAlertDialog loadingdialog;

    CommonApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        app = (CommonApplication) getApplication();


        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        etDescription = (EditText)findViewById(R.id.etDescription);

        etName.setText(app.user.firstname + " " + app.user.lastname);
        etEmail.setText(app.user.email);
        etTitle.setText(app.user.title);
        etDescription.setText(app.user.aboutme);

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().length() > 0 && etTitle.getText().length() > 0 && etEmail.length() > 0){
                    loadingdialog = new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Updating Profile...");
                    loadingdialog.show();

                    if(imageuri != null) {
                        String requestId = MediaManager.get().upload(imageuri).unsigned(Const.UPLOAD_PRESET).callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {

                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                String strURL = (String) resultData.get("secure_url");
                                try {
                                    editProfile(strURL);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        }).dispatch();
                    }else{
                        try {
                            editProfile("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Please input all fields.").show();
                }
            }
        });

        Button btn_uploadImage = (Button)findViewById(R.id.btn_UploadImage);
        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Upload Image Event


                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(EditProfileActivity.this,SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Select the Profile Image!");
                sweetAlertDialog.setConfirmText("From Gallery!");
                sweetAlertDialog.setCancelText("From Camera!");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        //Toast.makeText(getApplicationContext(),"image clicked",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
// Show only images, no videos or anything else
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.cancel();
                        //Camera
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
                sweetAlertDialog.show();
            }
        });
    }

    public void editProfile(String strImageURL) throws JSONException {

        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();
        String[] name = etName.getText().toString().split(" ");

        attributes.put("first-name", name[0]);
        attributes.put("last-name", name.length > 1 ? name[1] : "");
        attributes.put("about-me", etDescription.getText().toString());
        attributes.put("title", etTitle.getText().toString());
        attributes.put("phone-number",etPhoneNumber.getText().toString());
        if(strImageURL.length() > 0)
            attributes.put("profile-image-url", strImageURL);


        data.put("attributes",attributes);
        data.put("type","users");
        jsonParam.put("data",data);

        APIManager.getInstance(this).editProfile(EditProfileActivity.this, jsonParam, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingdialog.hide();
                        SweetAlertDialog dialog = new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE);

                        dialog.setTitleText("Success!")
                                .setContentText("Updated your profile successfully!")
                                .show();

                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
//                                CreateEventActivity.this.finish();
                            }
                        });



//
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingdialog.hide();
                        new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Your profile is not updated")
                                .show();
//                        EditProfileActivity.this.finish();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            //myPrefsEdit.putString("url", uri.toString());
            //myPrefsEdit.commit();

            try {
                uploadbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                // Log.d(TAG, String.valueOf(bitmap));

                //picture.setImageBitmap(bitmap);
                //Picasso.with(this).load(uri).into(avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            imageuri = data.getData();
            uploadbitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //Picasso.with(this).load().into(avatar);
            //avatar.setImageBitmap(photo);

        }
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
