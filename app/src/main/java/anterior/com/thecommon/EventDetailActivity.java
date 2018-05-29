package anterior.com.thecommon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import anterior.com.thecommon.model.Event;
import anterior.com.thecommon.utils.APIManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EventDetailActivity extends AppCompatActivity {

    public static Event events;
    public boolean likestate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvDescription = (TextView) findViewById(R.id.txtDescription);

        TextView tvDay = (TextView) findViewById(R.id.tvDay);
        TextView tvMonth = (TextView) findViewById(R.id.tvMonth);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        final TextView txtlikes = (TextView) findViewById(R.id.txtlikes);
        if(events.liked)
            txtlikes.setText(String.format("%d Unlike",events.like));
        else
            txtlikes.setText(String.format("%d",events.like));

        TextView txtcomment = (TextView) findViewById(R.id.txtcomment);
        txtcomment.setText(String.format("%d",events.comment));

        RoundedImageView img_photo = (RoundedImageView)findViewById(R.id.img_photo);
        Picasso.with(this).load(events.imageurl).into(img_photo);


        String day = (String) DateFormat.format("dd",   events.time);
        String month = (String) DateFormat.format("MMM",   events.time);
        String time = (String) DateFormat.format("EEE 'at' HH:MM AA",   events.time);

        tvDescription.setText(events.description);

        tvDay.setText(day);
        tvMonth.setText(month);
        tvTitle.setText(events.title);
        tvDate.setText(time);
        ImageButton btnLike = (ImageButton)findViewById(R.id.imglike);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likestate){
                    deletelike();
//                        like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                    events.like -= 1;
//                                txtlikes.setText(String.valueOf(data.like-1));
                    txtlikes.setText(String.valueOf(events.like));
                    likestate = false;
                }else{
                    postlike();
//                        like.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
                    events.like += 1;
                    txtlikes.setText(String.valueOf(events.like) + " Unlike");

//                                txtlikes.setText(String.valueOf(data.like+1));

                    likestate = true;
                }
                events.liked = likestate;

//                        SharedPreferences sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("likestate", likestate);
//                        editor.commit();
            }
        });


    }

    public void postlike(){
        APIManager.getInstance(this).postlike(events.feedid, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                int i = 0;
            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
            }
        });
    }

    public void deletelike(){
        APIManager.getInstance(this).deletelike(events.feedid, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                int i = 0;
            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
            }
        });
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
