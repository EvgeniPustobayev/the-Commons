package anterior.com.thecommon.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.CommunityDetailActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.adapters.CommunityAdapter;
import anterior.com.thecommon.model.Feed;
import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.Const;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzahg.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommunityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_LOCATION_REQUEST = 2;
    private static final int CAMERA_REQUEST = 1888;

    Bitmap uploadbitmap;
    Uri imageuri;
    SweetAlertDialog loadingdialog;

    ArrayList<Feed> arrayFeeds = new ArrayList<Feed>();
    CommunityAdapter adapter;
    int nPage;
    boolean flag_loading;

    EditText posttext;
    CommonApplication app;
    ListView listview;

    int itemclikced = -1;

    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout swipeContainer;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflateview = inflater.inflate(R.layout.fragment_community, container, false);
        app = (CommonApplication) getActivity().getApplication();
        return inflateview;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nPage = 1;
        listview = (ListView)view.findViewById(R.id.listview);

        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        loadfeeds();

                    }
                }
            }

        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                arrayFeeds.clear();
                nPage = 1;
                loadfeeds();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });
        adapter = new CommunityAdapter(getActivity(), arrayFeeds);
        listview.setAdapter(adapter);

        loadfeeds();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Feed data = arrayFeeds.get(i);
                CommunityDetailActivity.data = data;
                itemclikced = i;
//
//                SharedPreferences sharedPreferences = getContext().getSharedPreferences("",Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("selected", i);
//                editor.commit();
                Intent intent = new Intent(getActivity().getApplicationContext(), CommunityDetailActivity.class);
                startActivityForResult(intent, 123);
//                getActivity().startActivity(intent);
            }
        });

        posttext = view.findViewById(R.id.txtpost);

        ImageButton camera = view.findViewById(R.id.btn_postCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("camer", "camer");
                //Upload Image Event

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Select the Event Image!");
                sweetAlertDialog.setConfirmText("From Gallery!");
                sweetAlertDialog.setCancelText("From Camera!");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        //Toast.makeText(getApplicationContext(),"image clicked",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
// Show only images, no videos or anything else
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.cancel();
                        //Camera
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
                sweetAlertDialog.show();
            }
        });
        Button post = view.findViewById(R.id.btn_postbutton);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("post", "post");
                if(imageuri != null && posttext.getText().length() > 0 ){
                    loadingdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Creating Feed...");
                    loadingdialog.show();
                    String requestId = MediaManager.get().upload(imageuri).unsigned(Const.UPLOAD_PRESET).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {

                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String strURL = (String) resultData.get("secure_url");
                            try {
                                postWithImage(strURL);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {

                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }
                    }).dispatch();
                }else if(posttext.getText().length() > 0){
                    try {
                        loadingdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("Creating Feed...");
                        loadingdialog.show();
                        postWithoutImage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Please input all!").show();
                }

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });


        // or  (ImageView) view.findViewById(R.id.foo);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            //myPrefsEdit.putString("url", uri.toString());
            //myPrefsEdit.commit();

            try {
                uploadbitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageuri);
                // Log.d(TAG, String.valueOf(bitmap));

                //picture.setImageBitmap(bitmap);
                //Picasso.with(this).load(uri).into(avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imageuri = data.getData();
            uploadbitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //Picasso.with(this).load().into(avatar);
            //avatar.setImageBitmap(photo);

        }
        else if(requestCode == 123){
            boolean likestate = data.getBooleanExtra("result", false);
            updateView(itemclikced, likestate);
        }
    }

    private void updateView(int index, boolean likestate){
        View v = listview.getChildAt(index - listview.getFirstVisiblePosition());
        if(v == null)
            return;
        TextView txtLike = v.findViewById(R.id.txtlikes);
        txtLike.setText(String.format("%d",arrayFeeds.get(index).like));

        ImageView img_like = (ImageView)v.findViewById(R.id.imglike);
        if (likestate){
            img_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
        }else{
            img_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
        }
        if (arrayFeeds.get(index).liked){
            if (likestate == false)
                arrayFeeds.get(index).like -= 1;
        }else{
            if (likestate == true)
                arrayFeeds.get(index).like += 1;
        }
        arrayFeeds.get(index).liked = likestate;


    }

    public void loadfeeds(){


        APIManager.getInstance(getActivity()).getposts(nPage++, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {
                if(nPage == 2)
                    arrayFeeds.clear();
                arrayFeeds.addAll((ArrayList<Feed>)result);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if(((ArrayList<Feed>)result).size() > 0)
                            flag_loading = false;

                        swipeContainer.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

            }
        });
    }

    public void postWithImage(final String imgUrl) throws JSONException {
        final String txt;
        txt = String.valueOf(posttext.getText());

        JSONObject jsonParam = new JSONObject();
        JSONObject data1 = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("main-image-url", imgUrl);
        attributes.put("description", txt);

        data1.put("attributes",attributes);
        data1.put("type","posts");
        jsonParam.put("data",data1);

        APIManager.getInstance(getContext()).posttoFeed(jsonParam, app.user.id , new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
//                loadingdialog.hide();
                int i = 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingdialog.hide();
                        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);



                        dialog.setTitleText("Submit Successflly!")
                                .show();

                        Feed newFeed = new Feed();
                        newFeed.imageurl = imgUrl;
                        newFeed.description = txt;
                        newFeed.user = app.user;
                        newFeed.time = new Date();

                        posttext.setText("");

                        arrayFeeds.add(0, newFeed);
                        adapter.notifyDataSetChanged();





                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {

                            }
                        });



//
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingdialog.hide();
//                        new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("Error!")
//                                .setContentText("Event is not created")
//                                .show();
//                        CreateEventActivity.this.finish();
//                    }
//                });
            }
        });
    }

    public void postWithoutImage() throws JSONException {
        final String txt;
        txt = String.valueOf(posttext.getText());

        JSONObject jsonParam = new JSONObject();
        JSONObject data1 = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("description", txt);



        data1.put("attributes",attributes);
        data1.put("type","posts");
        jsonParam.put("data",data1);

        APIManager.getInstance(getContext()).posttoFeed(jsonParam, app.user.id , new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingdialog.hide();
                        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);


                        dialog.setTitleText("Submit Successflly!")
                                .show();

                        Feed newFeed = new Feed();
                        newFeed.imageurl = "null";
                        newFeed.description = txt;
                        newFeed.user = app.user;
                        newFeed.time = new Date();

                        posttext.setText("");

                        arrayFeeds.add(0, newFeed);
                        adapter.notifyDataSetChanged();
                    }
                });
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingdialog.hide();
//                        SweetAlertDialog dialog = new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//
//                        dialog.setTitleText("Submit Successflly!")
//                                .setContentText("Pending Approval!")
//                                .show();
//
//                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
//                                Intent returnIntent = new Intent();
//                                setResult(CommonActivity.RESULT_OK,returnIntent);
//                                CreateEventActivity.this.finish();
//                            }
//                        });
//
//
//
////
//                    }
//                });

            }

            @Override
            public void onFailure(String error, int nCode) {
                int i = 0;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingdialog.hide();
//                        new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("Error!")
//                                .setContentText("Event is not created")
//                                .show();
//                        CreateEventActivity.this.finish();
//                    }
//                });
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
