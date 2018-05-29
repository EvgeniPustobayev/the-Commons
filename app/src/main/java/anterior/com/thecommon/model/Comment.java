package anterior.com.thecommon.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;


public class Comment {

    public String username;
    public String comment;
    public String usertitle;
    public String time;
    public String image;
    public CommonUser user;


    public Comment(CommonApplication app, JSONObject obj, ArrayList<CommonUser> arrayUsers) throws JSONException {


        JSONObject attribute = obj.getJSONObject("attributes");

        comment = attribute.optString("text","");
        time = attribute.optString("timestamp","");

        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        Date newDate= null;
        try {
            newDate = spf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        time = spf.format(newDate);

        String strUserID = obj.getJSONObject("relationships").getJSONObject("author").getJSONObject("data").optString("id","");
        for (CommonUser userobj: arrayUsers) {
            if(userobj.id.equals(strUserID)){
                user = userobj;
                break;
            }
        }
        username = user.firstname + user.lastname;
        usertitle = user.title;
        image = user.profileurl;
    }
    public Comment(){
    }
    public void setUsername(String name){
        username = name;
    }
    public void setComment(String com){
        comment = com;
    }
    public void setUsertitle(String title){
        usertitle = title;
    }
    public void setImage(String img){
        image = img;
    }
    public void setTime(String t){
        time = t;
    }

}
