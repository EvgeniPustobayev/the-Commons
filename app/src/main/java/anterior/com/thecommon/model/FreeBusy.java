package anterior.com.thecommon.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import anterior.com.thecommon.CommonApplication;

public class FreeBusy {

    public Date starttime;
    public Date endtime;


     public FreeBusy(CommonApplication app, JSONObject data) throws JSONException {

        String strStartTime = data.optString("start","");
        String strEndTime = data.optString("end","");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
         dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            starttime = dateFormat.parse(strStartTime);
            endtime = dateFormat.parse(strEndTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

}
