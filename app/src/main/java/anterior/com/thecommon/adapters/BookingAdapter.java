package anterior.com.thecommon.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import anterior.com.thecommon.MeetingRoomActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.model.Room;

/**
 * Created by admin on 21/09/2017.
 */

public class BookingAdapter extends BaseAdapter{
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Room> arrayRooms;
    public BookingAdapter(Context context, ArrayList<Room> arrayList)
    {
        this.context = context;
        this.arrayRooms = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return this.arrayRooms.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View vi=view;

        if(vi == null)
            vi = inflater.inflate(R.layout.booking_row,null);

        final Room room = arrayRooms.get(i);

        Button btn_booking = (Button)vi.findViewById(R.id.btn_Booking);

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingRoomActivity.room = room;
                Intent intent = new Intent(context,MeetingRoomActivity.class);
                intent.putExtra("room_id",room.id);
                intent.putExtra("branch",i);
                context.startActivity(intent);
            }
        });

        TextView tvTitle = (TextView)vi.findViewById(R.id.txtTitle);
        TextView tvDetail = (TextView)vi.findViewById(R.id.txtDetail);
        TextView tvDescription = (TextView)vi.findViewById(R.id.txtDescription);

        ImageView imageview = (ImageView)vi.findViewById(R.id.imgDetail);
        Picasso.with(context).load(room.imageurl).fit().into(imageview);

        tvTitle.setText(room.short_name);
        tvDetail.setText(room.name);
        tvDescription.setText(room.description);

        return vi;
    }
}

