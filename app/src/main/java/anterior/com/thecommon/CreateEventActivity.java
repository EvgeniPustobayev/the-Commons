package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import anterior.com.thecommon.model.CommonLocation;
import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.Const;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateEventActivity extends AppCompatActivity {


    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_LOCATION_REQUEST = 2;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap uploadbitmap;
    Uri imageuri;
    Button btnLocationPicker;
    CommonLocation eventLocation;
    TextView txtLocation;
    Date selectedDate;

    EditText etDescription;
    EditText etTitle;

    SweetAlertDialog loadingdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        etDescription = (EditText)findViewById(R.id.etDescription);
        etTitle = (EditText)findViewById(R.id.etTitle);

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final CommonApplication app = (CommonApplication)getApplication();
        Button btn_Submit = (Button)findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageuri != null && selectedDate != null && etTitle.getText().length() > 0 && etDescription.length() > 0 && eventLocation != null){
                    loadingdialog = new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Creating Event...");
                    loadingdialog.show();
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
                                createEvent(strURL);
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
                  new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.ERROR_TYPE)
                          .setTitleText("Please input all!").show();
                }


            }
        });
        btnLocationPicker = (Button) findViewById(R.id.btnLocationPicker);
        btnLocationPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withLocation(app.commonlocation.currentLocation.getLatitude(), app.commonlocation.currentLocation.getLongitude())
                        .withGeolocApiKey(Const.GEO_CODE_API_KEY)
                        .shouldReturnOkOnBackPressed()
                        .build(getApplicationContext());

                startActivityForResult(intent, PICK_LOCATION_REQUEST);
            }
        });

        Button btn_uploadImage = (Button)findViewById(R.id.btn_UploadImage);
        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Upload Image Event

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CreateEventActivity.this,SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Select the Event Image!");
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
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
                sweetAlertDialog.show();
            }
        });

        txtLocation = (TextView)findViewById(R.id.txtLocation);
        final TextView tvDate = (TextView)findViewById(R.id.txtDate);
        final TextView tvTime = (TextView)findViewById(R.id.txtTime);

        Button btnDateTime = (Button)findViewById(R.id.btnDate);
        btnDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                selectedDate = date;
                                SimpleDateFormat fmt = new SimpleDateFormat("EEE, d MMM yyyy");
                                tvDate.setText(fmt.format(selectedDate));

                                fmt = new SimpleDateFormat("h:mm a");
                                tvTime.setText(String.format("at %s",fmt.format(selectedDate)));
                            }
                        })
                        .setInitialDate(new Date())
                        .build()
                        .show();
//                Toast.makeText(CreateEventActivity.this, "asdf", Toast.LENGTH_SHORT).show();
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

        }else if(requestCode == PICK_LOCATION_REQUEST && resultCode == Activity.RESULT_OK){
            double latitude = data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0);
            Log.d("LATITUDE****", String.valueOf(latitude));
            double longitude = data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0);
            Log.d("LONGITUDE****", String.valueOf(longitude));



            Address fullAddress = data.getParcelableExtra(LocationPickerActivity.ADDRESS);
            eventLocation = new CommonLocation();
            eventLocation.setAddress(fullAddress);
            eventLocation.latitude = latitude;
            eventLocation.longitude = longitude;

            txtLocation.setText(String.format("%s",eventLocation.address));

        }
    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void createEvent(String strImageURL) throws JSONException {

        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("description", etDescription.getText().toString());
        attributes.put("title", etTitle.getText().toString());
        attributes.put("state", eventLocation.state);
        attributes.put("postcode", eventLocation.postalCode);
        attributes.put("street", eventLocation.address1);
        attributes.put("full-address", eventLocation.address);
        attributes.put("country-code", eventLocation.countryCode);
        attributes.put("city", eventLocation.city);
        attributes.put("main-image-url", strImageURL);
        attributes.put("approved", true);
        attributes.put("longitude", eventLocation.longitude);
        attributes.put("latitude", eventLocation.latitude);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String strTime = fmt.format(selectedDate);
        attributes.put("event-time", strTime);

        data.put("attributes",attributes);
        data.put("type","events");
        jsonParam.put("data",data);

        APIManager.getInstance(this).creatEvent(jsonParam, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingdialog.hide();
                        SweetAlertDialog dialog = new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.SUCCESS_TYPE);

                                dialog.setTitleText("Submit Successflly!")
                                .setContentText("Pending Approval!")
                                .show();

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        Intent returnIntent = new Intent();
                                        setResult(Activity.RESULT_OK,returnIntent);
                                        CreateEventActivity.this.finish();
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
                        new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Event is not created")
                                .show();
                        CreateEventActivity.this.finish();
                    }
                });

            }
        });
    }
}
