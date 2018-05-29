package anterior.com.thecommon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import anterior.com.thecommon.R;

/**
 * Created by admin on 20/09/2017.
 */

public class LikeAdapter extends BaseAdapter{

    Context context;
    private static LayoutInflater inflater = null;

    public LikeAdapter(Context context)
    {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 10;
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
            vi = inflater.inflate(R.layout.like_row,null);
        SmartImageView avatar;
        avatar = (SmartImageView) vi.findViewById(R.id.img_photo);
        Picasso.with(context).load("https://res.cloudinary.com/dismuutoz/image/upload/v1484814402/bmd99hw7rl5dbdkdw3on.jpg").into(avatar);

        return vi;
    }
}
