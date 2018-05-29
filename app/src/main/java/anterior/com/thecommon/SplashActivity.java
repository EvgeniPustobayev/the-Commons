package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.User;
import anterior.com.thecommon.utils.APIManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CommonApplication app = (CommonApplication) getApplication();
        SharedPreferences sharedPref = getSharedPreferences("Thecommon", Context.MODE_PRIVATE);
        boolean isLogin = sharedPref.getBoolean("login_State", false);
        if(isLogin){
            String strJSON =  sharedPref.getString("userobject","");
            String fbid = sharedPref.getString("fbid","");
            try {
                JSONObject object = new JSONObject(strJSON);
                JSONObject jsonUser = object.getJSONObject("user").getJSONObject("data");
                JSONObject jsonToken = object.getJSONObject("auth").getJSONObject("data").getJSONObject("attributes");

                app.user = new CommonUser();
                app.user.setUserValue(jsonUser);
                app.user.setTokens(jsonToken);
                app.user.fbid = fbid;

                getAllUsers();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 3000);
        }
    }
    public void getAllUsers(){



        APIManager.getInstance(this).getallusers(new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
