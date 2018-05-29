package anterior.com.thecommon;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import anterior.com.thecommon.utils.APIManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etEmail;
    Button btn_reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etEmail = (EditText)findViewById(R.id.et_email);
        btn_reset = (Button)findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonParam = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    data.put("email",etEmail.getText().toString());
//                    jsonParam.put("data",data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final SweetAlertDialog loadingdialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Sending Email...");
                loadingdialog.show();
                APIManager.getInstance(ForgotPasswordActivity.this).resetpassword(data, new APIManager.CommonCallBackInterface() {
                    @Override
                    public void onSuccess(Object result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingdialog.hide();
                                SweetAlertDialog dialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                dialog.setTitleText("Sent")
                                        .setContentText("Reset password email is sent!")
                                        .show();
                            }});
                    }

                    @Override
                    public void onFailure(final String error, int nCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingdialog.hide();
                                SweetAlertDialog dialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.ERROR_TYPE);
                                dialog.setTitleText("Error!")
                                        .setContentText(error)
                                        .show();
                            }});
                    }
                });
            }
        });
    }

}
