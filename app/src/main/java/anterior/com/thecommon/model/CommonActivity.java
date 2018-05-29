package anterior.com.thecommon.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;

public class CommonActivity {

    public Date starttime;
    public Date endtime;

    public String type;
    public String sourceuserid;
    public String targetuserid;
    public String targetpost;

    public CommonUser sourceuser;
    public CommonUser targetuser;

    public String sourcecomment;

     public CommonActivity(CommonApplication app, JSONObject data) throws JSONException {

         JSONObject obj = data.getJSONObject("attributes");
         type = obj.optString("activity-type");
         JSONObject relation = data.getJSONObject("relationships");
         sourceuserid = relation.getJSONObject("source-user").getJSONObject("data").optString("id");
         targetpost = relation.getJSONObject("target-post").getJSONObject("data").optString("id");
         targetuserid = relation.getJSONObject("target-user").getJSONObject("data").optString("id");



    }

}
