package anterior.com.thecommon;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import anterior.com.thecommon.model.CommonLocation;
import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.Event;
import anterior.com.thecommon.utils.Const;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class CommonApplication extends Application {
    public CommonUser user;
    public CommonUser selectedUser;
    public Event selectedEvent;
    public ArrayList<CommonUser> arrayUsers;
    public CommonLocation commonlocation;

    @Override
    public void onCreate() {
        super.onCreate();

        commonlocation = new CommonLocation();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/roboto/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Map config = new HashMap();
        config.put("cloud_name", Const.PROD_ENV);
        MediaManager.init(this, config);




    }
}
