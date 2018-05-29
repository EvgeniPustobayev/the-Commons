package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import anterior.com.thecommon.adapters.NotificationAdapter;
import anterior.com.thecommon.fragment.CommunityFragment;
import anterior.com.thecommon.fragment.DirectoryFragment;
import anterior.com.thecommon.fragment.EventsFragment;
import anterior.com.thecommon.fragment.HomeFragment;
import anterior.com.thecommon.fragment.MessageFragment;
import anterior.com.thecommon.fragment.OurSpaceFragment;
import anterior.com.thecommon.model.CommonActivity;
import anterior.com.thecommon.model.Notification;
import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.Const;
import anterior.com.thecommon.utils.LocationHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainBoardActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback{

    private Toolbar toolbar;
    private DrawerLayout mDrawer,mDrawer2;
    public  FragmentPagerItemAdapter adapter;
    public CommonApplication app;

    HomeFragment homeFragment;
    Fragment prevFragment;
    DirectoryFragment directoryFragment;
    MessageFragment messageFragment;

    public int nCurrentIndex;


    private Location mLastLocation;

    double latitude;
    double longitude;

    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        setTitle("");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        sendDevicetoken(FirebaseInstanceId.getInstance().getToken());
//        getAllUsers();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        app = (CommonApplication)getApplication();
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ImageView img_photo = (ImageView)findViewById(R.id.img_photo);
        Picasso.with(this).load(app.user.profileurl).into(img_photo);

        TextView tf_Username = (TextView)findViewById(R.id.tf_Username);
        tf_Username.setText(app.user.firstname + " " + app.user.lastname);

        nCurrentIndex = 0;
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.community, CommunityFragment.class)
                .add(R.string.events, EventsFragment.class)
                .add(R.string.ourspace, OurSpaceFragment.class)
                .create());

        homeFragment = HomeFragment.newInstance();
        directoryFragment = DirectoryFragment.newInstance();
        messageFragment = MessageFragment.newInstance();

//        changeFragment(homeFragment);

        getNotifications();
        if (savedInstanceState == null) {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.container, homeFragment)
                    .commit();
            prevFragment = homeFragment;


        }

        Button btn_Profile = (Button)findViewById(R.id.btn_Profile);
        btn_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainBoardActivity.this,ProfileActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }
        });


        Button btn_directory = (Button)findViewById(R.id.btn_directory);
        btn_directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawers();

                nCurrentIndex = 1;
                changeFragment(directoryFragment);


            }
        });

        Button btn_home = (Button)findViewById(R.id.btn_Home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawers();
                nCurrentIndex = 0;
                changeFragment(homeFragment);
                homeFragment.viewPager.setCurrentItem(0,true);
//                Intent intent = new Intent(MainBoardActivity.this,MainBoardActivity.class);
//                startActivity(intent);
            }
        });

        Button btn_Room =(Button)findViewById(R.id.btn_rooms);
        btn_Room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawers();
                Intent intent = new Intent(MainBoardActivity.this,RoomActivity.class);
                startActivity(intent);

            }
        });

        Button btn_Event = (Button)findViewById(R.id.btn_events);
        btn_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nCurrentIndex = 0;
                changeFragment(homeFragment);
                mDrawer.closeDrawers();
                homeFragment.viewPager.setCurrentItem(1,true);
            }
        });
        Button btn_ourspace = (Button)findViewById(R.id.btn_ourSpace);
        btn_ourspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nCurrentIndex = 0;
                changeFragment(homeFragment);
                mDrawer.closeDrawers();
                homeFragment.viewPager.setCurrentItem(2,true);
            }
        });

        Button btn_Message = (Button)findViewById(R.id.btn_messages);
        btn_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent =new Intent(MainBoardActivity.this,MessageDetailActivity.class);
//                startActivity(intent);
                mDrawer.closeDrawers();

                nCurrentIndex = 1;
                changeFragment(messageFragment);
            }
        });


        ImageButton btn_notification = (ImageButton)findViewById(R.id.btn_notification);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.RIGHT);

            }
        });

        app = (CommonApplication)getApplication();

        locationHelper=new LocationHelper(this);


        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }



        }

    public void getNotifications(){
        APIManager.getInstance(this).getuseractivities(new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<CommonActivity> arrayActivities = new ArrayList<CommonActivity>();
                        arrayActivities.addAll((ArrayList<CommonActivity>)result);
                        NotificationAdapter notificationAdapter;

                        ListView listView = (ListView) findViewById(R.id.list_Notification);
                        notificationAdapter = new NotificationAdapter(MainBoardActivity.this, arrayActivities);
                        listView.setAdapter(notificationAdapter);
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {
//                Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override
    public void onBackPressed() {
    }

    public void changeFragment(Fragment myNewFragment) {

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if(prevFragment.getClass() == myNewFragment.getClass()){
            return;
        }
        if(prevFragment.getClass() == HomeFragment.class && myNewFragment.getClass() != HomeFragment.class){
            t.hide(homeFragment);
            t.add(R.id.container,myNewFragment);
        }else if(myNewFragment.getClass() == HomeFragment.class){
            t.remove(prevFragment);
            t.show(homeFragment);
        }else{
            t.remove(prevFragment);
            t.add(R.id.container, myNewFragment);
        }
        t.commit();
        prevFragment = myNewFragment;
    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void getAddress()
    {
        Address locationAddress = locationHelper.getAddress(latitude,longitude);

        if(locationAddress!=null)
        {

            app.commonlocation.setAddress(locationAddress);
        }
        else
            showToast("Something went wrong");
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Once connected with google api, get the location
        app.commonlocation.currentLocation=locationHelper.getLocation();
        try {
            latitude = app.commonlocation.currentLocation.getLatitude();
            longitude = app.commonlocation.currentLocation.getLongitude();
            getAddress();
        }catch (Exception e){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        locationHelper.connectApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
        app.commonlocation.currentLocation=locationHelper.getLocation();
        try {
            latitude = app.commonlocation.currentLocation.getLatitude();
            longitude = app.commonlocation.currentLocation.getLongitude();
            getAddress();
        }catch (Exception e){

        }

    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationHelper.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
    }

    public void sendDevicetoken(String deviceToken){
        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("device-token",deviceToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIManager.getInstance(this).senddevicetoken(data, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFailure(final String error, int nCode) {

            }
        });
    }

    public void getAllUsers(){



        APIManager.getInstance(this).getallusers(new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFailure(String error, int nCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
}
