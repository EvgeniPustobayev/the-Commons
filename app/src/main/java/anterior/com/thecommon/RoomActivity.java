package anterior.com.thecommon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.lzyzsd.circleprogress.DonutProgress;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RoomActivity extends AppCompatActivity {
    CommonApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (CommonApplication)getApplication();
        setContentView(R.layout.activity_room);

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_bookRoom = (Button)findViewById(R.id.btn_bookRoom);
        btn_bookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomActivity.this,BranchesActivity.class);
                startActivity(intent);
            }
        });
        Button btn_myBooking = (Button)findViewById(R.id.btn_myBooking);
        btn_myBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomActivity.this,MyBookingActivity.class);
                startActivity(intent);
            }
        });

        Button btn_myCalendar = (Button)findViewById(R.id.bt_myCalendar);
        btn_myCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RoomActivity.this,MyCalendarActivity.class);
                startActivity(intent);
            }
        });

        DonutProgress donut_progress = (DonutProgress)findViewById(R.id.donut_progress);
        donut_progress.setMax(20);
        donut_progress.setProgress(50);
        donut_progress.setTextColor(Color.BLUE);
        donut_progress.setShowText(true);
        donut_progress.setText(String.valueOf(app.user.remainingcredits));
        donut_progress.setMinimumWidth(50);
//        donut_progress.setBackgroundColor(Color.RED);//Square
//        donut_progress.setInnerBackgroundColor(Color.BLACK);//Inner
//        donut_progress.setUnfinishedStrokeColor(Color.BLACK);//Unfinished Color
        donut_progress.setFinishedStrokeColor(Color.BLACK);
        donut_progress.setFinishedStrokeWidth(100);
        donut_progress.setUnfinishedStrokeWidth(100);
    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
