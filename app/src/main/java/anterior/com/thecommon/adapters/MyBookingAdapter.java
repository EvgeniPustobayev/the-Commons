package anterior.com.thecommon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import anterior.com.thecommon.R;
import anterior.com.thecommon.model.Booking;

public class MyBookingAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Booking> arrayBooking;
    public MyBookingAdapter(Context context, ArrayList<Booking> arrayList)
    {
        this.context = context;
        this.arrayBooking = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return this.arrayBooking.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;

        if(vi == null)
            vi = inflater.inflate(R.layout.mybooking_row,null);

        Booking booking = arrayBooking.get(i);

        TextView tvTitle = vi.findViewById(R.id.txtTitle);
        TextView tvDescription = vi.findViewById(R.id.txtDescription);
        TextView tvDate = vi.findViewById(R.id.txtDate);


        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, d MMM yyyy");

        String fromtime = timeformat.format(booking.starttime);
        String endtime = timeformat.format(booking.endtime);

        String strdate = dateformat.format(booking.starttime);

        tvTitle.setText(booking.title);
        tvDescription.setText(String.format("%s - %s at %s", fromtime, endtime, strdate));

        tvDate.setVisibility(View.GONE);

        return vi;
    }
}