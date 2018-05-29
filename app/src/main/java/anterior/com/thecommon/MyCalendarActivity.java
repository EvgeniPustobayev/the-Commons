package anterior.com.thecommon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import anterior.com.thecommon.model.Booking;
import anterior.com.thecommon.utils.APIManager;


public class MyCalendarActivity extends AppCompatActivity{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
    private AppBarLayout appBarLayout;
    private CompactCalendarView compactCalendarView;
    private ArrayList<WeekViewEvent> mNewEvents;

    private ArrayList<Booking> arrayList = new ArrayList<Booking>();

    private boolean isExpanded = false;

    TextView tvTitle;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    WeekView mWeekView;
    String RoomID;
    Date currentDate;

    protected String getEventTitle(Calendar starTime, Calendar endTime) {
        return String.format("%02d:%02d - %02d:%02d Meeting room busy", starTime.get(Calendar.HOUR_OF_DAY), starTime.get(Calendar.MINUTE), endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));
    }
    public static Date addOneMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    public static Date minusOneMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        tvTitle = (TextView)findViewById(R.id.title);

        RoomID = getIntent().getStringExtra("room_id");


        currentDate = new Date();

        getMyBooking();

        mWeekView = (WeekView) findViewById(R.id.weekView);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        ImageButton btn_Back = (ImageButton)findViewById(R.id.btn_back);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_Today = (Button)findViewById(R.id.btn_Today);
        btn_Today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate = new Date();
                mWeekView.goToToday();
                compactCalendarView.setCurrentDate(currentDate);

            }
        });


        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                currentDate = newFirstVisibleDay.getTime();
                compactCalendarView.setCurrentDate(currentDate);

            }
        });

        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
                for(Booking object:arrayList) {

                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(object.starttime);

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.setTime(object.endtime);

                    if(newMonth == startTime.get(Calendar.MONTH)) {
                        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime, endTime), startTime, endTime);
                        event.setColor(getResources().getColor(R.color.gray_holo_dark));
                        events.add(event);
                    }

                }
                return events;


            }

        };

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        mWeekView.setMonthChangeListener(mMonthChangeListener);

        compactCalendarView.setFirstDayOfWeek(1);
        compactCalendarView.setCurrentDayTextColor(Color.BLACK);

        // Force English
        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);

        compactCalendarView.setShouldDrawDaysHeader(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));

                isExpanded = false;
                appBarLayout.setExpanded(isExpanded, false);

                Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.setTime(dateClicked);
                mWeekView.goToDate(cal);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }

        });

        // Set current date to today
        setCurrentDate(new Date());

        RelativeLayout datePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
                if(isExpanded){
                    tvTitle.setText(dateFormatForMonth.format(currentDate));
                }else{
                    tvTitle.setText("My Calendar");
                }
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                tvTitle.setText(dateFormatForMonth.format(dateClicked));
                currentDate = dateClicked;
                Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.setTime(currentDate);
                mWeekView.goToDate(cal);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tvTitle.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

    }

    @Override
    public void setTitle(CharSequence title) {
        TextView tvTitle = (TextView) findViewById(R.id.title);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
    public void setSubtitle(String subtitle) {
        TextView datePickerTextView = (TextView) findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
//            datePickerTextView.setText(subtitle);
        }
    }
    public void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
        }
    }
    public void getMyBooking(){
        APIManager.getInstance(this).getMyBooking(new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayList.clear();
                        arrayList.addAll((ArrayList<Booking>)result);

                        Calendar cal = Calendar.getInstance(Locale.getDefault());
                        cal.setTime(new Date());
                        mWeekView.goToDate(cal);
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

            }
        });
    }

}
