package anterior.com.thecommon.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import anterior.com.thecommon.R;
import anterior.com.thecommon.model.Event;
import anterior.com.thecommon.model.Feed;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 15/09/2017.
 */

public class EventAdapter extends BaseAdapter {

    Context context;
    ArrayList<Event> events;
    private static LayoutInflater inflater = null;



    public EventAdapter(Context context, ArrayList<Event> arrayevents)
    {
        this.context = context;
        events = arrayevents;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
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
            vi = inflater.inflate(R.layout.event_row,null);
        Event event = events.get(i);
        CircleImageView imgDetail = vi.findViewById(R.id.img_photo);
        Picasso.with(context).load(event.imageurl).resize(100, 100).into(imgDetail);

        TextView tvTitle = (TextView)vi.findViewById(R.id.tvTitle);
        tvTitle.setText(event.title);

        String day = (String) DateFormat.format("dd",   event.time);
        String month = (String) DateFormat.format("MMM",   event.time);

        TextView tvDay = (TextView)vi.findViewById(R.id.tvDay);
        TextView tvMonth = (TextView)vi.findViewById(R.id.tvMonth);

        tvDay.setText(day);
        tvMonth.setText(month);

        return vi;
    }
}
