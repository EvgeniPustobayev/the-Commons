package anterior.com.thecommon.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.MainBoardActivity;
import anterior.com.thecommon.MessageDetailActivity;
import anterior.com.thecommon.ProfileActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.adapters.DirectoryAdapter;
import anterior.com.thecommon.adapters.MessageAdapter;
import anterior.com.thecommon.model.Thread;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean isAdded = false;
    CommonApplication app;

    ArrayList<Thread> threads = new ArrayList<Thread>();

    private OnFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();

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
        app = (CommonApplication) getActivity().getApplication();

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ListView list = view.findViewById(R.id.list_messages);

        final MessageAdapter adapter = new MessageAdapter(getActivity(),threads);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageDetailActivity.thread = threads.get(i);
                Intent intent = new Intent(getActivity().getApplicationContext(), MessageDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });

        MainBoardActivity activity = (MainBoardActivity)getActivity();
        activity.nCurrentIndex = 1;



        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(app.user.fbid).child("threads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded)
                    return;
                threads.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
//                    String value = (String) postSnapshot.getValue();


                    final Thread thread = new Thread();
                    thread.id = key;
                    thread.from_id = key.split("-")[0];
                    thread.to_id = key.split("-")[1];
                    if(app.user.id.equals(thread.from_id)){
                        thread.opponentid = thread.to_id;
                    }else{
                        thread.opponentid = thread.from_id;
                    }

                    database.child("threads").child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                try {
                                    String key = postSnapshot.getKey();
                                    Object value = postSnapshot.getValue();
                                    thread.setValues(key, value);
                                }catch (Exception e){

                                }
                            }
                            if(!threads.contains(thread))
                                threads.add(thread);

                            Collections.sort(threads);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


//                    database.child("threads").child(key).addValueEventListener(new ValueEventListener() {
                }
                isAdded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
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
