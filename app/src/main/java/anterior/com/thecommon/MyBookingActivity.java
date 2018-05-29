package anterior.com.thecommon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import anterior.com.thecommon.adapters.BookingAdapter;
import anterior.com.thecommon.adapters.MyBookingAdapter;
import anterior.com.thecommon.model.Booking;
import anterior.com.thecommon.utils.APIManager;

public class MyBookingActivity extends AppCompatActivity {
    private ArrayList<Booking> arrayList = new ArrayList<Booking>();
    MyBookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        ListView listview = (ListView)findViewById(R.id.list_booking);
        adapter = new MyBookingAdapter(this, arrayList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CreateBookingActivity.booking = arrayList.get(i);
                Intent intent = new Intent(MyBookingActivity.this, CreateBookingActivity.class);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent, 1);

            }
        });

        getMyBooking();

        ImageButton btn_Back = (ImageButton)findViewById(R.id.btn_back);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
                        adapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getMyBooking();

    }
}
