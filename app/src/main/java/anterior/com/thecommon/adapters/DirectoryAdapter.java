package anterior.com.thecommon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.R;
import anterior.com.thecommon.model.CommonUser;

/**
 * Created by admin on 15/09/2017.
 */

public class DirectoryAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater = null;
    CommonApplication app;
    ArrayList<CommonUser> arrayUsers;

    public DirectoryAdapter(Context context, ArrayList<CommonUser> array)
    {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        app = (CommonApplication)context.getApplicationContext();
        arrayUsers = array;
    }

    @Override
    public int getCount() {
        return arrayUsers.size();
    }

    @Override
    public CommonUser getItem(int i) {
        return arrayUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi=view;
        if(vi == null)
            vi = inflater.inflate(R.layout.directory_cell,null);


        CommonUser user = arrayUsers.get(i);

        TextView tvName = vi.findViewById(R.id.txtName);
        tvName.setText(user.firstname + " " + user.lastname);

        ImageView imgAvatar = vi.findViewById(R.id.img_photo);
        Picasso.with(context).load(user.profileurl).resize(100,100).into(imgAvatar);

        TextView tvAlphabet = vi.findViewById(R.id.txtAlphabet);
        String firstcharactor = user.firstname.substring(0,1);
        tvAlphabet.setText(firstcharactor.toUpperCase());
        if(i != 0){
            CommonUser prevUser = arrayUsers.get(i-1);
            String prevFistCharactor =  prevUser.firstname.substring(0,1);
            if(firstcharactor.equalsIgnoreCase(prevFistCharactor)){
                tvAlphabet.setVisibility(View.INVISIBLE);
            }else{

                tvAlphabet.setVisibility(View.VISIBLE);
            }
        }else{
            tvAlphabet.setVisibility(View.VISIBLE);
        }



        return vi;
    }
}
