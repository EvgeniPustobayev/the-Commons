package anterior.com.thecommon.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.CreateEventActivity;
import anterior.com.thecommon.EventDetailActivity;
import anterior.com.thecommon.MainBoardActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.adapters.EventAdapter;
import anterior.com.thecommon.model.Event;
import anterior.com.thecommon.model.Feed;
import anterior.com.thecommon.utils.APIManager;
import anterior.com.thecommon.utils.Const;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    CommonApplication app;

    int nPage;
    boolean flag_loading;

    private OnFragmentInteractionListener mListener;

    ArrayList<Event> arrayEvents = new ArrayList<Event>();
    public  EventAdapter adapter;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nPage = 1;
        final ListView event_List = (ListView)view.findViewById(R.id.list_event);

        final MainBoardActivity mainBoardActivity = (MainBoardActivity) getActivity();
        adapter = new EventAdapter(mainBoardActivity.getApplicationContext(), arrayEvents);
        event_List.setAdapter(adapter);
        app = (CommonApplication) getActivity().getApplication();
        Button btn_planEvent = (Button)view.findViewById(R.id.btn_planEvent);
        btn_planEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mainBoardActivity, CreateEventActivity.class);
                startActivityForResult(intent, Const.EVENT_CODE);
            }
        });

        event_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                app.selectedEvent = arrayEvents.get(i);
                EventDetailActivity.events = arrayEvents.get(i);
                Intent intent = new Intent(mainBoardActivity, EventDetailActivity.class);
                startActivity(intent);
            }
        });

        event_List.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        loadevents();

                    }
                }
            }

        });
        loadevents();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    public void loadevents(){
        APIManager.getInstance(getActivity()).getevents(nPage++, new APIManager.CommonCallBackInterface() {
            @Override
            public void onSuccess(final Object result) {

                arrayEvents.addAll((ArrayList<Event>)result);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if(((ArrayList<Event>)result).size() > 0)
                            flag_loading = false;
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode== Const.EVENT_CODE)
        {
            loadevents();
        }
    }
}
