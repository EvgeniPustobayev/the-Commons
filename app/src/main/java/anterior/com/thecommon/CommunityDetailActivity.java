package anterior.com.thecommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import anterior.com.thecommon.adapters.ComDetailAdapter;
import anterior.com.thecommon.model.Comment;
import anterior.com.thecommon.model.Feed;
import anterior.com.thecommon.utils.APIManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CommunityDetailActivity extends AppCompatActivity {

    public static Feed data;
    public ArrayList<Comment> arraycomments = new ArrayList<Comment>();
    ComDetailAdapter comDetailAdapter;
    EditText sendComment;
    Intent returnIntent;

    boolean flag_loading;
    boolean flag_creat;
    int nPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        nPage = 1;
        sendComment = (EditText) findViewById(R.id.txtSendComment);



        Button send = (Button) findViewById(R.id.btn_Send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (!sendComment.getText().toString().equals("")) {
                        OnCreatComment();
                        sendComment.setText("");


                    }else{
                        Log.i("ad", "adf");
                    }
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        Object Data = getIntent().getSerializableExtra("Data");

        final ListView listView = (ListView)findViewById(R.id.list_CommunityDetailList);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        loadComments();

                    }
                }
            }
        });

//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                return false;
//            }
//        });

//        comDetailAdapter = new ComDetailAdapter(getApplicationContext(), data, arraycomments);
//        listView.setAdapter(comDetailAdapter);
//        loadComments();
        comDetailAdapter = new ComDetailAdapter(getApplicationContext(), data, arraycomments);
        listView.setAdapter(comDetailAdapter);

        APIManager.getInstance(this).getcomments(nPage++, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {
//                arraycomments.clear();
                //            comDetailAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arraycomments.addAll((ArrayList<Comment>)result);
                        if (((ArrayList<Comment>) result).size() == 0){
                            nPage--;
                        }

                        comDetailAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
            }
        }, data.id);
    }

    public void loadComments(){
        APIManager.getInstance(this).getcomments(nPage++, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {
//                arraycomments.clear();
                //            comDetailAdapter.notifyDataSetChanged();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Comment> arrayResult = (ArrayList<Comment>) result;
                        if(arrayResult.size() != 0){
                            arraycomments.addAll((ArrayList<Comment>)result);
                            if (((ArrayList<Comment>) result).size() == 0){
                                nPage --;
                            }
                            comDetailAdapter.notifyDataSetChanged();
                            flag_loading = false;
                        }


                    }
                });
            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
            }
        }, data.id);
    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void OnCreatComment() throws JSONException {
        String txt;
        txt = String.valueOf(sendComment.getText());

        JSONObject jsonParam = new JSONObject();
        JSONObject data1 = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("text", txt);



        data1.put("attributes",attributes);
        data1.put("type","comments");
        jsonParam.put("data",data1);

        APIManager.getInstance(this).creatComment(jsonParam, data.id, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {

                int i = 0;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingdialog.hide();
//                        SweetAlertDialog dialog = new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//
//                        dialog.setTitleText("Submit Successflly!")
//                                .setContentText("Pending Approval!")
//                                .show();
//
//                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
//                                Intent returnIntent = new Intent();
//                                setResult(CommonActivity.RESULT_OK,returnIntent);
//                                CreateEventActivity.this.finish();
//                            }
//                        });
//
//
//
////
//                    }
//                });

            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingdialog.hide();
//                        new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("Error!")
//                                .setContentText("Event is not created")
//                                .show();
//                        CreateEventActivity.this.finish();
//                    }
//                });
            }
        });

        Comment com = new Comment();
        com.setComment(txt);
        com.setUsername(data.user.firstname + data.user.lastname);
        com.setUsertitle(data.user.title);
        com.setImage(data.user.profileurl);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat spf= new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        com.setTime(spf.format(currentTime));
        arraycomments.add(com);
        comDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", comDetailAdapter.likestate);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

}
