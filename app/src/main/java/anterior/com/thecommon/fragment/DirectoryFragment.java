package anterior.com.thecommon.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudinary.Search;

import java.util.ArrayList;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.MainBoardActivity;
import anterior.com.thecommon.ProfileActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.adapters.DirectoryAdapter;
import anterior.com.thecommon.model.CommonUser;
import anterior.com.thecommon.utils.APIManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DirectoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DirectoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class DirectoryFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match

        public ArrayList<CommonUser> arrayUsers = new ArrayList<CommonUser>();
        public ArrayList<CommonUser> arraySearchUsers = new ArrayList<CommonUser>();

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        int nPage = 1;
    boolean flag_loading;

    DirectoryAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public DirectoryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DirectoryFragment newInstance() {
        DirectoryFragment fragment = new DirectoryFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ListView list = view.findViewById(R.id.listview);

//        SearchView searchView = view.findViewById(R.id.searchView);
        final CommonApplication app = (CommonApplication) getActivity().getApplication();
        arraySearchUsers.addAll(app.arrayUsers);

        adapter = new DirectoryAdapter(getActivity(), arraySearchUsers);
        list.setAdapter(adapter);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arraySearchUsers.clear();
                if (newText.length() < 1) {
                    arraySearchUsers.addAll(app.arrayUsers);
                }

                for (CommonUser user : app.arrayUsers) {
                    if (user.firstname.toLowerCase().contains(newText.toLowerCase()) || user.lastname.toLowerCase().contains(newText.toLowerCase())) {
                        arraySearchUsers.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommonApplication app = (CommonApplication) getActivity().getApplication();
                app.selectedUser = arraySearchUsers.get(i);
                Intent profileintent = new Intent(getActivity(), ProfileActivity.class);
                profileintent.putExtra("otheruser", true);
                getActivity().startActivity(profileintent);

            }
        });

        MainBoardActivity activity = (MainBoardActivity) getActivity();
        activity.nCurrentIndex = 1;

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
