package anterior.com.thecommon.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.exblr.dropdownmenu.DropdownListItem;
import com.exblr.dropdownmenu.DropdownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import anterior.com.thecommon.R;
import anterior.com.thecommon.adapters.NavAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OurSpaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OurSpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OurSpaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<String> listDataHeader,listDataHeader1,listDataHeader2,listDataHeader3,listDataHeader4;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChild1;
    HashMap<String, List<String>> listDataChild2;
    HashMap<String, List<String>> listDataChild3;
    HashMap<String, List<String>> listDataChild4;
    private OnFragmentInteractionListener mListener;

    private  static int checkState = 1;

    private ArrayList list1 = createMockList(2, false);

    public OurSpaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OurSpaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OurSpaceFragment newInstance(String param1, String param2) {
        OurSpaceFragment fragment = new OurSpaceFragment();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.extendable_list);
        prepareListData();
        expandableListView.setAdapter(new NavAdapter(getContext(), listDataHeader, listDataChild));

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });



        ExpandableListView expandableListView1 = (ExpandableListView)view.findViewById(R.id.extendable_listview1);
        prepareListData1();

        expandableListView1.setAdapter(new NavAdapter(getContext(), listDataHeader1, listDataChild1));

        ExpandableListView expandableListView2 = (ExpandableListView)view.findViewById(R.id.extendable_listview2);
        prepareListData2();

        expandableListView2.setAdapter(new NavAdapter(getContext(), listDataHeader2, listDataChild2));

        ExpandableListView expandableListView3 = (ExpandableListView)view.findViewById(R.id.extendable_listview3);
        prepareListData3();

        expandableListView3.setAdapter(new NavAdapter(getContext(), listDataHeader3, listDataChild3));


        final DropdownMenu mDropdownMenu = view.findViewById(R.id.dropdown_menu);

        expandableListView1.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        expandableListView2.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        expandableListView3.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });



        View customContentView = getActivity().getLayoutInflater().inflate(R.layout.layout_ourspace, null, false);
        final CheckBox cb_collingwood = customContentView.findViewById(R.id.cb_collingwood);
        final CheckBox cb_southMelbourn = customContentView.findViewById(R.id.cb_southMelbourne);


        final TextView tv_theCommon = (TextView)view.findViewById(R.id.tv_theCommon);


        cb_collingwood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cb_collingwood.setChecked(b);
                cb_southMelbourn.setChecked(!b);
                tv_theCommon.setText("Welcome to The Commons,\nCollingwood");
                mDropdownMenu.dismissCurrentPopupWindow();
            }
        });

        cb_southMelbourn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cb_southMelbourn.setChecked(b);
                cb_collingwood.setChecked(!b);
                tv_theCommon.setText("Welcome to The Commons,\nSouth Melbourne");
                mDropdownMenu.dismissCurrentPopupWindow();
            }
        });

        mDropdownMenu.add("Select your Space", customContentView);

    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private ArrayList createMockList(int count, boolean hasEmpty) {
        ArrayList list = new ArrayList();
        if (hasEmpty) {
            list.add(new DropdownListItem(0, "a", true, true));
        }
        for (int i = 1; i <= count; i++) {
            if(i==1){
                list.add(new DropdownListItem(10, "Collingwood"));
            }else{
                list.add(new DropdownListItem(11, "South Melbourne"));
            }
        }
        return list;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        // Adding headers
        Resources res = getResources();
        String[] headers = res.getStringArray(R.array.nav_drawer_labels);
        listDataHeader = Arrays.asList(headers);

        // Static method
        List<String> hour,address,keyCard,wifiPassword,Phone,Security;
        String[] shour,saddress,skeyCard,swifiPassword,sPhone,sSecurity;

        shour = res.getStringArray(R.array.elements_hour);
        hour = Arrays.asList(shour);

        saddress = res.getStringArray(R.array.elements_Address);
        address = Arrays.asList(saddress);

        skeyCard = res.getStringArray(R.array.elements_keyCard);
        keyCard = Arrays.asList(skeyCard);

        swifiPassword = res.getStringArray(R.array.elements_wifiPassword);
        wifiPassword = Arrays.asList(swifiPassword);

        sPhone = res.getStringArray(R.array.elements_phone);
        Phone = Arrays.asList(sPhone);

        sSecurity = res.getStringArray(R.array.elements_security);
        Security = Arrays.asList(sSecurity);


        listDataChild.put(listDataHeader.get(0), hour);
        listDataChild.put(listDataHeader.get(1), address);
        listDataChild.put(listDataHeader.get(2), keyCard);
        listDataChild.put(listDataHeader.get(3), wifiPassword);
        listDataChild.put(listDataHeader.get(4), Phone);
        listDataChild.put(listDataHeader.get(5), Security);

        //listDataChild.put(listDataHeader.get(2), keyCard);
    }

    private void prepareListData1() {
        listDataHeader1 = new ArrayList<String>();
        listDataChild1 = new HashMap<String, List<String>>();


        // Adding headers
        Resources res = getResources();
        String[] headers = res.getStringArray(R.array.nav_drawer_contacts);
        listDataHeader1 = Arrays.asList(headers);

        // Static method
        List<String> emergency,itSupport;
        String[] semergency,sitSupport;

        semergency = res.getStringArray(R.array.elements_EmergencyNumber);
        emergency = Arrays.asList(semergency);

        sitSupport = res.getStringArray(R.array.elements_itSupport);
        itSupport = Arrays.asList(sitSupport);

        listDataChild1.put(listDataHeader1.get(0), emergency);
        listDataChild1.put(listDataHeader1.get(1), itSupport);

        //listDataChild.put(listDataHeader.get(2), keyCard);
    }
    private void prepareListData2() {
        listDataHeader2 = new ArrayList<String>();
        listDataChild2 = new HashMap<String, List<String>>();


        // Adding headers
        Resources res = getResources();
        String[] headers = res.getStringArray(R.array.nav_drawer_activities);
        listDataHeader2 = Arrays.asList(headers);

        // Static method
        List<String> wellness,oursocialmedia;
        String[] swellness,sourSocialMedia;

        swellness = res.getStringArray(R.array.elements_EmergencyNumber);
        wellness = Arrays.asList(swellness);

        sourSocialMedia = res.getStringArray(R.array.elements_itSupport);
        oursocialmedia = Arrays.asList(sourSocialMedia);

        listDataChild2.put(listDataHeader2.get(0), wellness);
        listDataChild2.put(listDataHeader2.get(1), oursocialmedia);

        //listDataChild.put(listDataHeader.get(2), keyCard);
    }

    private void prepareListData3() {
        listDataHeader3 = new ArrayList<String>();
        listDataChild3 = new HashMap<String, List<String>>();


        // Adding headers
        Resources res = getResources();
        String[] headers = res.getStringArray(R.array.nav_drawer_usingTheSpace);
        listDataHeader3 = Arrays.asList(headers);

        // Static method
        List<String> metting, kitchen,Printing,Beer,Bike,Outdoor,Moveout;
        String[] sMeeting, skitchen,sPrinting,sBeer,sBike,sOutdoor,sMoveout;

        sMeeting = res.getStringArray(R.array.elements_meetingRoom);
        metting = Arrays.asList(sMeeting);

        skitchen = res.getStringArray(R.array.elements_kitchen);
        kitchen = Arrays.asList(skitchen);

        sPrinting = res.getStringArray(R.array.elements_printing);
        Printing = Arrays.asList(sPrinting);

        sBeer = res.getStringArray(R.array.elements_Beer);
        Beer = Arrays.asList(sBeer);

        sBike = res.getStringArray(R.array.elements_Bike);
        Bike = Arrays.asList(sBike);

        sOutdoor= res.getStringArray(R.array.elements_outDoor);
        Outdoor = Arrays.asList(sOutdoor);

        sMoveout = res.getStringArray(R.array.elements_moveOut);
        Moveout = Arrays.asList(sMoveout);

        listDataChild3.put(listDataHeader3.get(0), metting);
        listDataChild3.put(listDataHeader3.get(1), kitchen);
        listDataChild3.put(listDataHeader3.get(2), Printing);
        listDataChild3.put(listDataHeader3.get(3), Beer);
        listDataChild3.put(listDataHeader3.get(4), Bike);
        listDataChild3.put(listDataHeader3.get(5), Outdoor);
        listDataChild3.put(listDataHeader3.get(6), Moveout);

        //listDataChild.put(listDataHeader.get(2), keyCard);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_our_space, container, false);
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
