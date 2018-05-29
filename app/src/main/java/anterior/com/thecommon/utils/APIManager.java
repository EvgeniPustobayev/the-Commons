package anterior.com.thecommon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.model.CommonActivity;
import anterior.com.thecommon.model.Comment;
import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.Event;
import anterior.com.thecommon.model.Feed;
import anterior.com.thecommon.model.Booking;
import anterior.com.thecommon.model.FreeBusy;
import anterior.com.thecommon.model.Room;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;


public class APIManager {

    public interface CommonCallBackInterface {

        void onSuccess(Object result);
        void onFailure(String error, int nCode);
    }

    CommonApplication application;


    private static APIManager instance;
    OkHttpClient client;
    private APIManager() {
        client = new OkHttpClient();
    }
    public static APIManager getInstance(Context context) {
        if (instance == null) {
            instance = new APIManager();
        }
        instance.application = (CommonApplication)context.getApplicationContext();
        return instance;
    }

    public void doLogin(final Context context, final String username, final String password, final CommonCallBackInterface callback){
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody formBody = formBuilder.build();

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(username,password)).build();

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_LOGIN);

        Request request = new Request.Builder()
                .url(strURL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {

                    JSONObject object = new JSONObject(jsonData);
                    JSONObject jsonUser = object.getJSONObject("user").getJSONObject("data");
                    JSONObject jsonToken = object.getJSONObject("auth").getJSONObject("data").getJSONObject("attributes");

                    APIManager.this.application.user = new CommonUser();
                    APIManager.this.application.user.setUserValue(jsonUser);
                    APIManager.this.application.user.setTokens(jsonToken);

                    SharedPreferences.Editor editor = context.getSharedPreferences("Thecommon",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("login_State", true);
                    editor.putString("userobject", object.toString());
                    editor.commit();



                    callback.onSuccess(null);

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }

    public void getuseractivities(final CommonCallBackInterface callback){

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_GETACTIVITIES);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();

        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<CommonActivity> arrayActivities = new ArrayList<CommonActivity>();
                    JSONObject object = new JSONObject(jsonData);
                    JSONArray array = object.getJSONArray("data");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        CommonActivity comonact = new CommonActivity(application, obj);
                        arrayActivities.add(comonact);
                    }


                    if(code == 200){
                        callback.onSuccess(arrayActivities);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }

    public void resetpassword(JSONObject parameter, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_RESETPASS);
//        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody body = RequestBody.create(JSON, parameter.toString());

//        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    try {
                        JSONObject object = new JSONObject(jsonData);
                        String strError = object.getString("errorDescription");
                        callback.onFailure(strError, code);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }
    public void senddevicetoken(JSONObject parameter, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_DEVTOKEN);
//        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    try {
                        JSONObject object = new JSONObject(jsonData);
                        String strError = object.getString("errorDescription");
                        callback.onFailure(strError, code);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }
    public void sendchat(String userid, JSONObject parameter, final CommonCallBackInterface callback){

        String strURL = String.format("%susers/%s/%s",Const.BASE_URL,userid,Const.API_SENDCHAT);
//        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    try {
                        JSONObject object = new JSONObject(jsonData);
                        String strError = object.getString("errorDescription");
                        callback.onFailure(strError, code);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    public void getusers(int nPage, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s?page=%d",Const.BASE_URL,Const.API_GETUSERS,nPage);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();

        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();

                ArrayList<CommonUser>arrayUsers = new ArrayList<CommonUser>();
                int code = response.code();
                try {

                    JSONObject object = new JSONObject(jsonData);
                    JSONArray array = object.getJSONArray("data");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject userobj = array.getJSONObject(i);
                        CommonUser user = new CommonUser();
                        user.setUserValue(userobj);
                        arrayUsers.add(user);
                    }

                    Collections.sort(arrayUsers, new Comparator<CommonUser>() {
                        @Override
                        public int compare(CommonUser user1, CommonUser user2) {
                            return user1.firstname.compareToIgnoreCase(user2.firstname);
                        }
                    });

                    if(code == 200){
                        callback.onSuccess(arrayUsers);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }

    public void getallusers(final CommonCallBackInterface callback){

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_GETALLUSERS);

        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).authenticator(getBasicAuth(application.user.access_token,"")).build();

        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();

                application.arrayUsers = new ArrayList<CommonUser>();
                int code = response.code();
                try {

                    JSONObject object = new JSONObject(jsonData);
                    JSONArray array = object.getJSONArray("data");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject userobj = array.getJSONObject(i);
                        CommonUser user = new CommonUser();
                        user.setUserValue(userobj);
                        application.arrayUsers.add(user);
                    }

                    Collections.sort(application.arrayUsers, new Comparator<CommonUser>() {
                        @Override
                        public int compare(CommonUser user1, CommonUser user2) {
                            return user1.firstname.compareToIgnoreCase(user2.firstname);
                        }
                    });

                    if(code == 200){
                        callback.onSuccess(null);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }
    public void getevents(int nPage, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s?page=%d",Const.BASE_URL,Const.API_GETEVENTS, nPage);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<Event> arrayEvents = new ArrayList<Event>();
                    JSONObject object = new JSONObject(jsonData);

//                    ArrayList<CommonUser> arrayUsers = new ArrayList<CommonUser>();
//                    JSONArray arrayincluded = object.getJSONArray("included");
//                    for(int i = 0; i < arrayincluded.length(); i++){
//                        JSONObject userobj = arrayincluded.getJSONObject(i);
//                        CommonUser user = new CommonUser();
//                        user.setUserValue(userobj);
//                        arrayUsers.add(user);
//                    }
                    JSONArray arrayincluded = object.getJSONArray("included");
                    JSONArray arraydata = object.getJSONArray("data");
                    for(int i = 0; i < arraydata.length(); i++){
                        JSONObject obj = arraydata.getJSONObject(i);
                        Event event = new Event(application,obj, arrayincluded);
                        if(event.approved)
                            arrayEvents.add(event);
                    }

                    if(code == 200){
                        callback.onSuccess(arrayEvents);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }
    public void getposts(int nPage, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s?page=%d",Const.BASE_URL,Const.API_GETPOST,nPage);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<Feed> arrayFeeds = new ArrayList<Feed>();
                    JSONObject object = new JSONObject(jsonData);


                    JSONArray array = object.getJSONArray("included");


                    JSONArray arraydata = object.getJSONArray("data");
                    for(int i = 0; i < arraydata.length(); i++){
                        JSONObject obj = arraydata.getJSONObject(i);
                        Feed feed = new Feed(application,obj, array);
                        arrayFeeds.add(feed);
                    }

                    if(code == 200){
                        callback.onSuccess(arrayFeeds);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public void creatEvent(JSONObject parameter, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_NEWEVENT);
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                    if(code == 200 || code == 201){
                        callback.onSuccess(null);
                    }
                    else {
                        callback.onFailure(null, code);
                    }

            }
        });

    }

    public void editProfile(final Context context, JSONObject parameter, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_UPDATEPROFILE);
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    JSONObject object = new JSONObject(jsonData);
                    JSONObject jsonUser = object.getJSONObject("data");

                    APIManager.this.application.user.setUserValue(jsonUser);
                    SharedPreferences sharedPref = context.getSharedPreferences("Thecommon", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    String strJSON =  sharedPref.getString("userobject","");
                    JSONObject userobject = new JSONObject(strJSON);
                    userobject.put("user",object);

                    editor.putBoolean("login_State", true);
                    editor.putString("userobject", userobject.toString());
                    editor.commit();
                }
                catch(Exception e){

                }

                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }

            }
        });

    }

    public void getRooms(int nIndex, final CommonCallBackInterface callback){

        String strURL = String.format("%sbranches/%d/%s",Const.BASE_URL,nIndex,Const.API_BRANCHES);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<Room> arrayRooms = new ArrayList<Room>();
                    JSONObject object = new JSONObject(jsonData);
                    JSONArray arraydata = object.getJSONArray("data");
                    for(int i = 0; i < arraydata.length(); i++){
                        JSONObject obj = arraydata.getJSONObject(i);
                        Room room = new Room(application,obj);
                        arrayRooms.add(room);
                    }

                    if(code == 200){
                        callback.onSuccess(arrayRooms);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }

    public void getFreeBusy(Date startDate, Date endDate, String RoomID, final CommonCallBackInterface callback){


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);

        String strURL = String.format("%s%s/%s/%s?start=%s&end=%s",Const.BASE_URL,Const.API_BRANCHES,RoomID,Const.API_FREEBUSY, strStartDate, strEndDate);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<FreeBusy> arrayFreeBusy = new ArrayList<FreeBusy>();
                    JSONObject object = new JSONObject(jsonData);
                    JSONArray arraydata = object.getJSONArray("busy");
                    for(int i = 0; i < arraydata.length(); i++){
                        JSONObject obj = arraydata.getJSONObject(i);
                        FreeBusy booking = new FreeBusy(application,obj);
                        arrayFreeBusy.add(booking);
                    }

                    if(code == 200){
                        callback.onSuccess(arrayFreeBusy);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }

    public void bookmeetingroom(JSONObject parameter, String RoomID, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s/%s/%s",Const.BASE_URL,Const.API_BRANCHES,RoomID,Const.API_BOOKROOM);
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    try {
                        JSONObject object = new JSONObject(jsonData);
                        String strError = object.getString("errorDescription");
                        callback.onFailure(strError, code);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    public void updateBooking(String id, final Context context, JSONObject parameter, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s/%s",Const.BASE_URL,Const.API_BOOKROOM,id);
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();

                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }

            }
        });

    }

    public void deletebooking(String id, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s/%s",Const.BASE_URL,Const.API_BOOKROOM,id);
        RequestBody body = RequestBody.create(JSON, "");
        String credential = Credentials.basic(application.user.access_token, "");

        client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .addHeader("Authorization", credential)
                .url(strURL)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }
            }
        });

    }
    public void getMyBooking(final CommonCallBackInterface callback){



        String strURL = String.format("%s%s",Const.BASE_URL,Const.API_BOOKROOM);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<Booking> arrayFreeBusy = new ArrayList<Booking>();
                    JSONObject object = new JSONObject(jsonData);
                    JSONArray arraydata = object.getJSONArray("data");
                    for(int i = 0; i < arraydata.length(); i++){
                        JSONObject obj = arraydata.getJSONObject(i);
                        Booking booking = new Booking(application,obj);
                        arrayFreeBusy.add(booking);
                    }

                    if(code == 200){
                        callback.onSuccess(arrayFreeBusy);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }

    public void posttoFeed(JSONObject parameter, String id, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s%s%s",Const.BASE_URL,"branches/","3","/posts");
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }

            }
        });

    }

    public void postlike(String id, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s/%s%s",Const.BASE_URL,Const.API_GETPOST,id,"/likes");
        RequestBody body = RequestBody.create(JSON, "");
        String credential = Credentials.basic(application.user.access_token, "");


        client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .addHeader("Authorization", credential)
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }

            }
        });

    }

    public void deletelike(String id, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s/%s%s",Const.BASE_URL,Const.API_GETPOST,id,"/likes");
        RequestBody body = RequestBody.create(JSON, "");
        String credential = Credentials.basic(application.user.access_token, "");

        client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .addHeader("Authorization", credential)
                .url(strURL)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }
            }
        });

    }



    public void creatComment(JSONObject parameter, String id, final CommonCallBackInterface callback){

        String strURL = String.format("%s%s/%s%s",Const.BASE_URL,Const.API_GETPOST,id,"/comments");
        RequestBody body = RequestBody.create(JSON, parameter.toString());

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();


                if(code == 200 || code == 201){
                    callback.onSuccess(null);
                }
                else {
                    callback.onFailure(null, code);
                }

            }
        });

    }

    public void getcomments(int nPage, final CommonCallBackInterface callback, String id){
        String strId = String.valueOf(id);
        String strURL = String.format("%s%s/%s%s%d",Const.BASE_URL,Const.API_GETPOST,id,"/comments?page=", nPage);

        client = new OkHttpClient.Builder().authenticator(getBasicAuth(application.user.access_token,"")).build();

        Request request = new Request.Builder()
                .url(strURL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                try {
                    ArrayList<Comment> arrayComments = new ArrayList<Comment>();
                    JSONObject object = new JSONObject(jsonData);


                    ArrayList<CommonUser> arrayUsers = new ArrayList<CommonUser>();
                    JSONArray arrayincluded = object.getJSONArray("included");
                    for(int i = 0; i < arrayincluded.length(); i++){
                        JSONObject userobj = arrayincluded.getJSONObject(i);
                        CommonUser user = new CommonUser();
                        user.setUserValue(userobj);
                        arrayUsers.add(user);
                    }

                    JSONArray array = object.getJSONArray("data");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        Comment comment = new Comment(application, obj, arrayUsers);
                        arrayComments.add(comment);
                    }

//                    Collections.sort(application.arrayUsers, new Comparator<Comment>() {
//                        @Override
//                        public int compare(User user1, User user2) {
//                            return user1.firstname.compareToIgnoreCase(user2.firstname);
//                        }
//                    });

                    if(code == 200){
                        callback.onSuccess(arrayComments);
                    }
                    else {
                        callback.onFailure(object.getString("errors"), code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Network Error", code);
                }
            }
        });

    }
    protected Authenticator getBasicAuth(final String username, final String password) {
        return new Authenticator() {
            private int mCounter = 0;

            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                Log.d("OkHttp", "authenticate(Route route, Response response) | mCounter = " + mCounter);
                if (mCounter++ > 0) {
                    Log.d("OkHttp", "authenticate(Route route, Response response) | I'll return null");
                    return null;
                } else {
                    Log.d("OkHttp", "authenticate(Route route, Response response) | This is first time, I'll try to authenticate");
                    String credential = Credentials.basic(username, password);
                    return response.request().newBuilder().header("Authorization", credential).build();
                }
            }
        };
    }
}
