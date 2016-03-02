package dartmouth.edu.dartreminder.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.data.ScheduleDBHelper;
import dartmouth.edu.dartreminder.utils.ListViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext;
    private View view;

    private ScheduleDBHelper mScheduleDBHelper;
    private getAllFromDBTask task = null;

    public RecentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentListFragment newInstance(String param1, String param2) {
        RecentListFragment fragment = new RecentListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_list, null);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDeepRed)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewScheduleActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScheduleDBHelper = new ScheduleDBHelper(getActivity());
        task = new getAllFromDBTask();
        task.execute();
    }

    private void initialRecentList(ArrayList<Schedule> schedules){
        mContext = getActivity();
        mListView = (ListView) view.findViewById(R.id.recent_listview);
        mAdapter = new ListViewAdapter(mContext, schedules);

        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class getAllFromDBTask extends AsyncTask<Void, String, ArrayList> {

        @Override
        protected ArrayList doInBackground(Void... unused) {
            ArrayList<Schedule> allSchedule = mScheduleDBHelper.fetchSchedules();
            return allSchedule;
        }

        @Override
        protected void onProgressUpdate(String... name) {
            //Toast.makeText(NewScheduleActivity.this, "Entry #" + name[0] + " saved", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            initialRecentList(arrayList);
        }
    }
}
