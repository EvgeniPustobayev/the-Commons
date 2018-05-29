package anterior.com.thecommon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonUser {

    public String id;
    public String aboutme;
    public String companies;
    public boolean onboarding;
    public String email;
    public String firstname;
    public String lastname;
    public String phonenumber;
    public String profileurl;
    public int remainingcredits;
    public String title;
    public String website;
    public String access_token;
    public String chat_token;
    public String fbid;

    public void CommonUser(){

    }

    public void setUserValue(JSONObject obj) throws JSONException {


        JSONObject attribute = obj.getJSONObject("attributes");
        id = obj.optString("id","");
        aboutme = attribute.optString("about-me","");
        companies = attribute.optString("companies","");
        onboarding = attribute.optBoolean("completed-onboarding",false);
        email = attribute.optString("email","");
        firstname = attribute.optString("first-name","");
        lastname = attribute.optString("last-name","");
        phonenumber = attribute.optString("phone-number","");
        profileurl = attribute.optString("profile-image-url","");
        remainingcredits = attribute.optInt("remaining-credits",0);
        title = attribute.optString("title","");
        website = attribute.optString("website","");
    }

    public void setTokens(JSONObject token) throws JSONException {
        access_token = token.getString("access-token");
        chat_token = token.getString("chat-token");
    }


    public boolean saveAccessToken(String accesstoken){
        access_token = accesstoken;
        return true;
    }
    public boolean clearAccessToken(){
        return true;
    }

}
