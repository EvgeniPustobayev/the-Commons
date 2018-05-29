package anterior.com.thecommon.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;

public class Feed {

    public String id;
    public String description;
    public String imageurl;
    public String title;

    public int comment;
    public int like;
    public boolean liked;
    public Date time;

    public String location;

    public CommonUser user;
    public Event event;
    public String eventid;


    public Feed(CommonApplication app, JSONObject obj, JSONArray array) throws JSONException {

        JSONObject attributes = obj.getJSONObject("attributes");
        id = obj.optString("id","");
        description = attributes.optString("description","");
        imageurl = attributes.optString("main-image-url","");
        title = attributes.optString("title","");

        comment = attributes.optInt("comment-count",0);
        like = attributes.optInt("like-count",0);
        liked = attributes.optBoolean("liked",false);
        String strTime = attributes.optString("timestamp","");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        try {
            time = dateFormat.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strUserID = obj.getJSONObject("relationships").getJSONObject("author").getJSONObject("data").optString("id","");
        String strEventID = "";
        if(!obj.getJSONObject("relationships").getJSONObject("event").isNull("data"))
            strEventID = obj.getJSONObject("relationships").getJSONObject("event").getJSONObject("data").optString("id","");

        if(strUserID != null) {
            for(int i = 0; i < array.length(); i++){
                JSONObject include = array.getJSONObject(i);
                if(include.optString("type","").equals("users")){
                    if(strUserID.equals(include.optString("id",""))){
                        user = new CommonUser();
                        user.setUserValue(include);
                    }
                }else if(include.optString("type","").equals("events") && strEventID.equals(include.optString("id",""))){
                    imageurl = include.getJSONObject("attributes").optString("main-image-url","");
                    location = include.getJSONObject("attributes").optString("area","");
                }
            }
        }


    }

    public Feed() {
        super();
    }
}
