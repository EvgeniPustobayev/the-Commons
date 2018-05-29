package anterior.com.thecommon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.Message;
import anterior.com.thecommon.model.Thread;
import anterior.com.thecommon.model.User;
import anterior.com.thecommon.utils.APIManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MessageDetailActivity extends MessageBaseActivity implements   MessageInput.InputListener{



    CommonApplication app;
    public static Thread thread;
    CommonUser opponentuser;
    private MessagesList messagesList;
//    CommonUser opp;
//    CommonUser me;

    Bitmap bitmapme;
    Bitmap bitmapopp;
    DatabaseReference database;

    SweetAlertDialog pDialog;

    boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        this.messagesList = (MessagesList) findViewById(R.id.messagesList);


        initAdapter();

        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        app = (CommonApplication) getApplication();

        for(CommonUser user : app.arrayUsers){
            if(user.id.equals(thread.opponentid)){
                opponentuser = user;
            }
        }
        try {
            Picasso.with(this).load(app.user.profileurl).resize(100,100).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmapme = bitmap;
                    //                me = new CommonUser(Integer.parseInt(app.user.id), app.user.firstname+" "+app.user.lastname, bitmapme);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }catch (Exception e){

        }

        try {
            Picasso.with(this).load(opponentuser.profileurl).resize(100,100).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmapopp = bitmap;
                    //                opp = new CommonUser(Integer.parseInt(opponentuser.id), opponentuser.firstname+" "+opponentuser.lastname, bitmapopp);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }catch (Exception e){

        }

//        pDialog = new SweetAlertDialog(MessageDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading Messages...");
//        pDialog.setCancelable(false);
//        pDialog.show();
        super.messagesAdapter.clear();
        try {
            database = FirebaseDatabase.getInstance().getReference();
            database.child("messages").child(thread.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (isAdded)
                        return;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Object value1 = postSnapshot.getValue();
                        HashMap<String, Object> chatsnapshot = (HashMap) postSnapshot.getValue();
                        String strText = "";
                        String strSender = "";
                        Double lasttimestamp = new Double(0);
                        for (Map.Entry<String, Object> entry : chatsnapshot.entrySet()) {
                            try {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (key.equals("text")) {
                                    strText = (String) value;
                                }
                                if (key.equals("user-id")) {
                                    strSender = (String) value;
                                }
                                if (key.equals("timestamp")) {
                                    lasttimestamp = (Double) value;
                                }

                            } catch (Exception e) {

                            }
                        }

                        if (strSender.equals(app.user.id)) {
                            User me = new User("0", app.user.firstname + " " + app.user.lastname, app.user.profileurl, true);
                            long timestamp = Long.parseLong(String.format("%.0f", lasttimestamp.doubleValue() * 1000));
                            Date netDate = (new Date(timestamp));
                            Message newMessage = new Message(app.user.id, me, strText, netDate);

                            MessageDetailActivity.super.messagesAdapter.addToStart(newMessage, true);
                        } else {

                            User oppo = new User(opponentuser.id, opponentuser.firstname + " " + opponentuser.lastname, opponentuser.profileurl, true);
                            long timestamp = Long.parseLong(String.format("%.0f", lasttimestamp.doubleValue() * 1000));
                            Date netDate = (new Date(timestamp));
                            Message newMessage = new Message(opponentuser.id, oppo, strText, netDate);
                            MessageDetailActivity.super.messagesAdapter.addToStart(newMessage, true);

                        }


                    }
                    isAdded = true;
                    //                    mChatView.scrollToEnd();
                    //                pDialog.hide();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onSelectionChanged(int i) {

    }



    private void initAdapter() {

        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextLayout(R.layout.item_custom_incoming_text_message)
                .setOutcomingTextLayout(R.layout.item_custom_outcoming_text_message);

        super.messagesAdapter = new MessagesListAdapter<>(super.senderId,holdersConfig, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        super.messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, Message message) {
                        
                    }
                });
        this.messagesList.setAdapter(super.messagesAdapter);
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onSubmit(CharSequence charSequence) {
        User me = new User("0", app.user.firstname + " " + app.user.lastname, app.user.profileurl, true);

        Date netDate = (new Date());
        Message newMessage = new Message(app.user.id, me, charSequence.toString(), netDate);
        super.messagesAdapter.addToStart(newMessage, true);

        HashMap<String, Object> message = new HashMap<>();
        message.put("display-name", app.user.firstname + " " + app.user.lastname);
        message.put("text", charSequence.toString());

        Date curDate = new Date();
        long curMillis = curDate.getTime();
        Double timestamp = new Double(curMillis)/1000;
        message.put("timestamp", timestamp);
        message.put("user-id",app.user.id);

        HashMap<String, Object> threadhash = new HashMap<>();
        threadhash.put("last-display-name",app.user.firstname + " " + app.user.lastname);
        threadhash.put("last-text",charSequence.toString());
        threadhash.put("last-timestamp",timestamp);
        threadhash.put("last-user-id",app.user.id);

        HashMap<String, Object> membershash = new HashMap<>();
        membershash.put(app.user.id, true);
        membershash.put(opponentuser.id, true);
        threadhash.put("members",membershash);



        database = FirebaseDatabase.getInstance().getReference();

        String key = database.child("messages").child(thread.id).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/threads/"+thread.id, threadhash);
        childUpdates.put("/messages/"+thread.id + "/" + key, message);
        childUpdates.put("/users/"+thread.opponentid+"/threads/"+ thread.id,app.user.firstname+ " "+app.user.lastname);
        childUpdates.put("/users/"+app.user.id+"/threads/"+ thread.id,opponentuser.firstname+ " "+opponentuser.lastname);

        database.updateChildren(childUpdates);


        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("message-text",charSequence.toString());
            data.put("thread-id",thread.id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIManager.getInstance(this).sendchat(opponentuser.id, data, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFailure(final String error, int nCode) {

            }
        });

        return true;
    }
}
