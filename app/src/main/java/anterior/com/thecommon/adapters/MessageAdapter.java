package anterior.com.thecommon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.choota.dev.ctimeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.R;
import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.Thread;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 18/09/2017.
 */

public class MessageAdapter extends BaseAdapter{

    Activity mAct;
    private static LayoutInflater inflater = null;
    CommonApplication app;
    public ArrayList<Thread> mThreads;

    public MessageAdapter(Activity act, ArrayList<Thread> threads)
    {
        this.mAct = act;
        mThreads = threads;
        inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        app = (CommonApplication) act.getApplication();

    }

    @Override
    public int getCount() {
        return mThreads.size();
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
            vi = inflater.inflate(R.layout.message_row,null);

        Thread thread = mThreads.get(i);
        CommonUser lastuser = null;
        String userID;
        if(thread.to_id.equals(app.user.id)){
            userID = thread.from_id;
        }else{
            userID = thread.to_id;
        }
        for(CommonUser user : app.arrayUsers){
            if(user.id.equals(userID)){
                lastuser = user;
            }
        }
        TimeAgo timeAgo = new TimeAgo();
        TextView txtTime = vi.findViewById(R.id.txtTime);
        long timestamp = Long.parseLong( String.format( "%.0f",thread.lasttimestamp.doubleValue()*1000 ) ) ;
        Date netDate = (new Date(timestamp));
        txtTime.setText(timeAgo.getTimeAgo(netDate));
        try {
            CircleImageView avatar = (CircleImageView) vi.findViewById(R.id.img_photo);
            Picasso.with(mAct).load(lastuser.profileurl).resize(100,100).into(avatar);
            TextView tvUsername = vi.findViewById(R.id.txtUsername);
            tvUsername.setText(lastuser.firstname + " " + lastuser.lastname);
            TextView tvChat = vi.findViewById(R.id.txtChat);
            tvChat.setText(thread.lasttext);
        }catch (Exception e){

        }

        return vi;
    }
}
