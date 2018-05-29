package anterior.com.thecommon.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;

public class Booking {

    public Date starttime;
    public Date endtime;

    public String id;
    public String attendees;
    public String description;
    public String title;

     public Booking(CommonApplication app, JSONObject data) throws JSONException {

        JSONObject obj = data.getJSONObject("attributes");
        id = data.optString("id","");
        String strStartTime = obj.optString("start","");
        String strEndTime = obj.optString("end","");
        attendees = obj.optString("attendees","");
        description = obj.optString("description","");
        title = obj.optString("title","");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            starttime = dateFormat.parse(strStartTime);
            endtime = dateFormat.parse(strEndTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

}
