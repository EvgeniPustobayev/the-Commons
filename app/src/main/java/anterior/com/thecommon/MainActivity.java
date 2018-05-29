package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import anterior.com.thecommon.model.User;
import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.LocationHelper;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback {
    CommonApplication app;

    SharedPreferences sharedPref;

    Button btnLogin;
    Button btnForgot;
    EditText etUsername;
    EditText etPassword;
    SweetAlertDialog pDialog;
    private FirebaseAuth mAuth;

//    private Location mLastLocation;
//
//    double latitude;
//    double longitude;
//
//    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        app = (CommonApplication)getApplication();
        btnLogin = (Button)findViewById(R.id.btn_login);

        etUsername = (EditText)findViewById(R.id.et_email);
        etPassword = (EditText)findViewById(R.id.et_password);

//        String username = "seb@anterior.com.au";
//        String password = "Hirois12";

//        etUsername.setText(username);
//        etPassword.setText(password);


        btnLogin.setOnClickListener(this);

        btnForgot = (Button)findViewById(R.id.btn_pwdReset);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

//        locationHelper=new LocationHelper(this);
//        locationHelper.checkpermission();

        ButterKnife.bind(this);

        // check availability of play services
//        if (locationHelper.checkPlayServices()) {
//
//            // Building the GoogleApi client
//            locationHelper.buildGoogleApiClient();
//        }

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        FirebaseUser currentUser = mAuth.getCurrentUser();


    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        APIManager.getInstance(this).doLogin(this, etUsername.getText().toString(),etPassword.getText().toString(), new APIManager.CommonCallBackInterface() {

            @Override
            public void onSuccess(Object obj) {

                getAllUsers();


//                getAllUsers();
            }

            @Override
            public void onFailure(final String error, int nCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.hide();
                    }
                });

            }
        });

//        app.commonlocation.currentLocation = locationHelper.getLocation();
//
//        if (mLastLocation != null) {
//            latitude = app.commonlocation.currentLocation.getLatitude();
//            longitude = app.commonlocation.currentLocation.getLongitude();
//            getAddress();
//
//        }
    }

    public void getAllUsers(){



        APIManager.getInstance(this).getallusers(new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.hide();
                        signinwithfirebase(app.user.chat_token);
//                        Intent intent = new Intent(MainActivity.this, MainBoardActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

//    public void getAddress()
//    {
//        Address locationAddress = locationHelper.getAddress(latitude,longitude);
//
//        if(locationAddress!=null)
//        {
//
//            app.commonlocation.setAddress(locationAddress);
//        }
//        else
//            showToast("Something went wrong");
//    }

    public void signinwithfirebase(String chatToken){
        mAuth.signInWithCustomToken(chatToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            app.user.fbid = user.getUid();

                            SharedPreferences.Editor editor = getSharedPreferences("Thecommon",Context.MODE_PRIVATE).edit();
                            editor.putString("fbid", user.getUid());
                            editor.commit();

                            Intent intent = new Intent(MainActivity.this, MainBoardActivity.class);
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        locationHelper.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        locationHelper.checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
//        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
//        mLastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
//        locationHelper.connectApiClient();
    }


    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
//        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
