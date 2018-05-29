package anterior.com.thecommon.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;

public class Event {

    public String id;
    public String title;
    public String description;
    public String imageurl;
    public Date time;

    public String placename;
    public String state;
    public String street;
    public String area;
    public String city;
    public String country;
    public String country_code;
    public String postcode;

    public Double latitude;
    public Double longitude;
    public boolean approved;

    public String feedid;
    public int comment;
    public int like;
    public boolean liked;



    public CommonUser user;


    public Event(CommonApplication app, JSONObject obj, JSONArray arrayIncludes) throws JSONException {

        JSONObject attributes = obj.getJSONObject("attributes");
        id = obj.optString("id","");
        description = attributes.optString("description","");
        imageurl = attributes.optString("main-image-url","");
        title = attributes.optString("title","");

        String strTime = attributes.optString("event-time","");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            time = dateFormat.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        area = attributes.optString("area","");
        city = attributes.optString("city","");
        country = attributes.optString("country","");
        country_code = attributes.optString("country-code","");
        state = attributes.optString("state","");
        street = attributes.optString("street","");
        placename = attributes.optString("place-name","");
        postcode = attributes.optString("postcode","");

        latitude = attributes.optDouble("latitude",0.0);
        longitude = attributes.optDouble("longitude",0.0);

        approved = attributes.optBoolean("approved");


        String strUserID = obj.getJSONObject("relationships").getJSONObject("author").getJSONObject("data").optString("id","");
        for (CommonUser userobj: app.arrayUsers) {
            if(userobj.id.equals(strUserID)){
                user = userobj;
                break;
            }
        }

        feedid = obj.getJSONObject("relationships").getJSONObject("post").getJSONObject("data").optString("id","");

        for(int i = 0; i < arrayIncludes.length(); i++){
            JSONObject objIncludes = arrayIncludes.getJSONObject(i);

            if(objIncludes.optString("type","").equals("posts")){
                if(objIncludes.optString("id","").equals(feedid)){
                    JSONObject include_attr = objIncludes.getJSONObject("attributes");
                    like = include_attr.optInt("like-count",0);
                    liked = include_attr.optBoolean("liked",false);
                    comment = include_attr.optInt("comment-count",0);
                }
                break;
            }

        }



    }

}
