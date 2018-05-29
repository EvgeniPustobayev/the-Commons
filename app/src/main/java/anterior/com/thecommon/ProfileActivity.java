package anterior.com.thecommon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.Thread;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {
    CommonApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        application = (CommonApplication) getApplication();



        Button btn_Logout = (Button)findViewById(R.id.btn_Logout);






        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        boolean otheruser = getIntent().getBooleanExtra("otheruser",false);

        ImageButton btn_Request = (ImageButton)findViewById(R.id.btn_Request);
        if(otheruser){
            btn_Request.setVisibility(View.GONE);
        }
        btn_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ReportActivity.class);
                startActivity(intent);
            }
        });


        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("Thecommon",Context.MODE_PRIVATE).edit();
                editor.putBoolean("login_State", false);
                editor.commit();
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }
    public void showprofile(){
        Button btn_edit = (Button)findViewById(R.id.btn_edit);
        boolean otheruser = getIntent().getBooleanExtra("otheruser",false);
        Button btn_Logout = (Button)findViewById(R.id.btn_Logout);
        final CommonUser user;
        if(otheruser) {
            btn_Logout.setVisibility(View.INVISIBLE);
            user = application.selectedUser;
            btn_edit.setText("Send Message");
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Thread newthread = new Thread();
                    if(user.id.compareTo(application.user.id)<0) {
                        newthread.id = user.id + "-" + application.user.id;
                    }else{
                        newthread.id = application.user.id + "-" + user.id;
                    }
                    newthread.from_id = application.user.id;
                    newthread.to_id = user.id;
                    newthread.isNew = true;
                    newthread.opponentid = user.id;

                    MessageDetailActivity.thread = newthread;
                    Intent intent = new Intent(ProfileActivity.this, MessageDetailActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            btn_Logout.setVisibility(View.VISIBLE);
            user = application.user;
            btn_edit.setText("Edit");
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                    startActivity(intent);
                }
            });
        }



        CircleImageView avatar = (CircleImageView)findViewById(R.id.img_photo);
        Picasso.with(this).load(user.profileurl).into(avatar);

        TextView tvName = (TextView)findViewById(R.id.tvFullname);
        tvName.setText(user.firstname + " " + user.lastname);

        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(user.title);

        TextView tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvCompany.setText(user.companies);

        TextView tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvEmail.setText(user.email);

        TextView tvPhone = (TextView)findViewById(R.id.tvPhoneNumber);
        tvPhone.setText(user.phonenumber);

        TextView tvDescription = (TextView)findViewById(R.id.tvDescription);
        if(!user.aboutme.equals("\"null\""))
            tvDescription.setText(user.aboutme);
    }
    @Override public void onResume(){
        super.onResume();
        showprofile();

    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
