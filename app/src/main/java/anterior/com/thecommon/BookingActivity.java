package anterior.com.thecommon;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import anterior.com.thecommon.adapters.BookingAdapter;
import anterior.com.thecommon.model.Room;
import anterior.com.thecommon.utils.APIManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingActivity extends AppCompatActivity {
    BookingAdapter bookingAdapter;
    ArrayList<Room> arrayRooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        arrayRooms = new ArrayList<Room>();

        int nIndex = getIntent().getIntExtra("nIndex",0) + 2;
        APIManager.getInstance(this).getRooms(nIndex, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayRooms.addAll((ArrayList<Room>) result);
                        Collections.sort(arrayRooms);
                        bookingAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(String error, int nCode) {

            }
        });

        ListView list_Booking = (ListView)findViewById(R.id.list_booking);

        bookingAdapter = new BookingAdapter(this, arrayRooms);
        list_Booking.setAdapter(bookingAdapter);

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        String[] arrayRoomType = getResources().getStringArray(R.array.branches_type);
        txtTitle.setText(arrayRoomType[nIndex - 2]);
    }


    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
