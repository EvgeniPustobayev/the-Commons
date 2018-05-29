package anterior.com.thecommon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.MainBoardActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.model.CommonActivity;
import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.model.Notification;


public class NotificationAdapter extends BaseAdapter {
    ArrayList<CommonActivity> arrayActivities;
    MainBoardActivity mAct;
    private static LayoutInflater inflater = null;

    public NotificationAdapter(MainBoardActivity act, ArrayList<CommonActivity> array)
    {
        this.mAct = act;
        this.arrayActivities = array;
        inflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return arrayActivities.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v1 = view;
        v1 = inflater.inflate(R.layout.notification_cell, null);

        CommonActivity comAct = arrayActivities.get(i);
        CommonApplication app = (CommonApplication) mAct.getApplication();
        for(CommonUser user : app.arrayUsers){
            if(user.id.equals(comAct.sourceuserid)){
                comAct.sourceuser = user;
            }else if(user.id.equals(comAct.targetuserid)){
                comAct.targetuser = user;
            }
        }
        ImageView img = v1.findViewById(R.id.notification_image);
        Picasso.with(mAct).load(comAct.sourceuser.profileurl).into(img);
        TextView txt = v1.findViewById(R.id.notification_text);

        if(Integer.valueOf(comAct.type) == 1){
            txt.setText(comAct.sourceuser.firstname + " " + comAct.sourceuser.lastname + " has liked your post.");
        }else if(Integer.valueOf(comAct.type) == 1) {
            txt.setText(comAct.sourceuser.firstname + " " + comAct.sourceuser.lastname + " has commented on your post.");
        }
//        ImageView img = v1.findViewById(R.id.notification_image);
//        Picasso.with(context).load(notifications.get(i).image).into(img);
//        TextView txt = v1.findViewById(R.id.notification_text);
//        txt.setText(notifications.get(i).text);

        return v1;
    }
}
