package dartmouth.edu.dartreminder.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.utils.ListViewAdapter;

public class RecentListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private String type;

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext;
    private View view;

    private DartReminderDBHelper mScheduleDBHelper;
    private getAllFromDBTask task = null;

    public RecentListFragment(String type) {
        // Required empty public constructor
        this.type = type;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_time, null);

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
        mScheduleDBHelper = new DartReminderDBHelper(getActivity());
        task = new getAllFromDBTask();
        task.execute();
    }

    private void initialRecentList(ArrayList<Schedule> schedules){
        mContext = getActivity();
        mListView = (ListView) view.findViewById(R.id.recent_listview);
        mAdapter = new ListViewAdapter(mContext, type, schedules);

        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
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
            ArrayList<Schedule> allSchedule;
            if(type.equals("Time")) {
                allSchedule = mScheduleDBHelper.fetchSchedulesByTime();
            }else{
                allSchedule = mScheduleDBHelper.fetchSchedulesByActivity();
            }

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
