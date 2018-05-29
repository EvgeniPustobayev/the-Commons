package anterior.com.thecommon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import anterior.com.thecommon.R;
import anterior.com.thecommon.model.Comment;
import anterior.com.thecommon.model.Feed;
import anterior.com.thecommon.utils.APIManager;

/**
 * Created by admin on 26/09/2017.
 */

public class ComDetailAdapter extends BaseAdapter{

    Feed data;
    Context context;
    ArrayList<Comment> comments;
    public boolean likestate;
    private static LayoutInflater inflater = null;

    public ComDetailAdapter(final Context context, Feed data, ArrayList<Comment> commentsarray)
    {
        this.data = data;
        this.context = context;
        this.comments = commentsarray;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        likestate = data.liked;

        View v = inflater.inflate(R.layout.comdetail2, null);

    }
    @Override
    public int getCount() {
        return comments.size()+1;
//        return 3;
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
        View vi=view;

        if(i==0){

            vi = inflater.inflate(R.layout.comdetail2, null);
            ImageView imgAvatar = vi.findViewById(R.id.img_photo);

            final ImageView like = vi.findViewById(R.id.imglike);
            final TextView txtlikes = (TextView)vi.findViewById(R.id.txtlikes);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(likestate){
                        deletelike();
//                        like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                        data.like -= 1;
//                                txtlikes.setText(String.valueOf(data.like-1));
                        txtlikes.setText(String.valueOf(data.like));
                        likestate = false;
                    }else{
                        postlike();
//                        like.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
                        data.like += 1;
                        txtlikes.setText(String.valueOf(data.like) + " Unlike");

//                                txtlikes.setText(String.valueOf(data.like+1));

                        likestate = true;
                    }
                    data.liked = likestate;

//                        SharedPreferences sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("likestate", likestate);
//                        editor.commit();
                }
            });

            Picasso.with(context).load(this.data.user.profileurl).fit().centerCrop().into(imgAvatar);
            TextView txtUsername = vi.findViewById(R.id.txtUsername);
            txtUsername.setText(this.data.user.firstname + " " + data.user.lastname);

            TextView txtTitle = (TextView)vi.findViewById(R.id.txtTitle);
            txtTitle.setText(this.data.user.companies);

            TextView txtDate = vi.findViewById(R.id.txtTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
            txtDate.setText(dateFormat.format(this.data.time));

            ImageView imgDetail = (ImageView)vi.findViewById(R.id.imgDetail);
            if(this.data.imageurl.equals("null")){
                imgDetail.setVisibility(View.GONE);
            }else {
                Picasso.with(context).load(this.data.imageurl).into(imgDetail);
                imgDetail.setVisibility(View.VISIBLE);
            }


            TextView txtContent = (TextView)vi.findViewById(R.id.txtContent);
            txtContent.setText(this.data.description);

            TextView txtlocation = vi.findViewById(R.id.txtlocation);
            txtlocation.setText(this.data.location);


            if (data.liked){
                txtlikes.setText(String.valueOf(this.data.like) + " Unlike");

            }else{
                txtlikes.setText(String.valueOf(this.data.like));
            }

            TextView txtcomment = (TextView)vi.findViewById(R.id.txtcomment);
            txtcomment.setText(String.valueOf(this.data.comment));

//                TextView txtlocation = (TextView)vi.findViewById(R.id.txtlocation);
//                txtlocation.setText(data);


        }
        else{
            if (comments != null){
                vi = inflater.inflate(R.layout.communitydetailcell, null);
                ImageView imgAvatar = vi.findViewById(R.id.avatar);
                Picasso.with(context).load(comments.get(i-1).image).resize(100,100).into(imgAvatar);

                TextView txtusername = vi.findViewById(R.id.txtUsername);
                TextView title = vi.findViewById(R.id.txtTitle);
                TextView txtComment = vi.findViewById(R.id.txtdetailComment);
                TextView txtTime = vi.findViewById(R.id.txtTime);

                txtusername.setText(comments.get(i-1).username);
                title.setText(comments.get(i-1).usertitle);
                txtComment.setText(comments.get(i-1).comment);
                txtTime.setText(comments.get(i-1).time);
            }
        }

        return vi;
    }
    public void postlike(){
        APIManager.getInstance(context).postlike(data.id, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                int i = 0;
            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
            }
        });
    }

    public void deletelike(){
        APIManager.getInstance(context).deletelike(data.id, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                int i = 0;
            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
            }
        });
    }
}
