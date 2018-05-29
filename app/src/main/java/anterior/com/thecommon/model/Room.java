package anterior.com.thecommon.model;


import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;

public class Room implements Comparable<Room> {

    public String id;
    public String description;
    public String imageurl;
    public String name;
    public String short_name;
    public Date time;

     public Room(CommonApplication app, JSONObject obj) throws JSONException {

        JSONObject attributes = obj.getJSONObject("attributes");
        id = obj.optString("id","");
        description = attributes.optString("description","");
        imageurl = attributes.optString("main-image-url","");
        name = attributes.optString("name","");
        short_name = attributes.optString("short-name","");

        String strTime = attributes.optString("timestamp","");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        try {
            time = dateFormat.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    @Override
    public int compareTo(@NonNull Room room) {
         return short_name.compareTo(room.short_name);

    }
}
