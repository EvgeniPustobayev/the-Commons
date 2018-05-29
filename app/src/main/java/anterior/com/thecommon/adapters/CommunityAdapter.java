package anterior.com.thecommon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import anterior.com.thecommon.Like_Activity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.model.Feed;
import anterior.com.thecommon.utils.APIManager;


public class CommunityAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView imgAvatar;
        TextView txtUsername;
        TextView txtTitle;
        TextView txtDate;
        RoundedImageView imgDetail;
        TextView txtContent;
        TextView txtComment;
        TextView txtLike;
        TextView txtlocation;
        ImageView img_like;
    }

    Context context;
    ArrayList<Feed> feeds;
    private static LayoutInflater inflater = null;

    public CommunityAdapter(Context context, ArrayList<Feed> arrayFeeds) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.feeds = arrayFeeds;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return feeds.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return feeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;

        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.community_cell, null);
            holder = new ViewHolder();
            vi.setTag(holder);
            holder.imgAvatar = vi.findViewById(R.id.avatar);
            holder.txtUsername = vi.findViewById(R.id.txtUsername);
            holder.txtTitle = vi.findViewById(R.id.txtTitle);
            holder.txtUsername = vi.findViewById(R.id.txtUsername);
            holder.txtDate = vi.findViewById(R.id.txtTime);
            holder.imgDetail = vi.findViewById(R.id.imgDetail);
            holder.txtContent = vi.findViewById(R.id.txtContent);

            holder.txtComment = vi.findViewById(R.id.txtcomment);
            holder.txtLike = vi.findViewById(R.id.txtlikes);
            holder.txtlocation = vi.findViewById(R.id.txtlocation);
            holder.img_like = (ImageView)vi.findViewById(R.id.imglike);
        }
        else
            holder=(ViewHolder) convertView.getTag();


        final Feed feed = feeds.get(position);


        if(feed.user != null) {
            Picasso.with(context).load(feed.user.profileurl).fit().into(holder.imgAvatar);
            holder.txtUsername.setText(feed.user.firstname + " " + feed.user.lastname);
            holder.txtTitle.setText(feed.user.companies);
        }else{
//            Picasso.with(context).load("").into(imgAvatar);

            holder.txtUsername.setText("");
            TextView txtTitle = vi.findViewById(R.id.txtTitle);
            txtTitle.setText("");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        holder.txtDate.setText(dateFormat.format(feed.time));



        if(feed.imageurl.equals("null") || feed.imageurl.length() < 1){
            holder.imgDetail.setVisibility(View.GONE);
        }else {
            Picasso.with(context).load(feed.imageurl).fit().centerCrop().into(holder.imgDetail);
            holder.imgDetail.setVisibility(View.VISIBLE);
        }

        holder.txtContent.setText(feed.description);


        holder.txtComment.setText(String.format("%d",feed.comment));

        if(feed.liked)
            holder.txtLike.setText(String.format("%d Unlike",feed.like));
        else
            holder.txtLike.setText(String.format("%d",feed.like));


        holder.txtlocation.setText(feed.location);


        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(feed.liked){
                    deletelike(feed);
                    feed.like -= 1;

                    holder.txtLike.setText(String.format("%d",feed.like));

                }else{
                    postlike(feed);
                    feed.like += 1;
                    holder.txtLike.setText(String.format("%d Unlike",feed.like));
                }
                feed.liked = !feed.liked;

            }
        });

        return vi;
    }

    public void postlike(Feed data){
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

    public void deletelike(Feed data){
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