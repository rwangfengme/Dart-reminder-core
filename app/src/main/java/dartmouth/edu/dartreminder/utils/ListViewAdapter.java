package dartmouth.edu.dartreminder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.server.ServerUtilities;
import dartmouth.edu.dartreminder.view.ShareScheduleActivity;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private String type;
    private DartReminderDBHelper mScheduleDBHelper;
    private DelScheduleTask task = null;
    private ArrayList<Schedule> dataScource;

    private SwipeLayout swipeLayout;

    public ListViewAdapter(Context mContext, String type, ArrayList dataSource) {
        this.type = type;
        this.mContext = mContext;
        this.dataScource = dataSource;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        final View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Del DB

                mScheduleDBHelper = new DartReminderDBHelper(mContext);
                task = new DelScheduleTask();
//                task.execute(dataScource.get(position).getId(), new Long(position));
                task.execute(dataScource.get(position));


                dataScource.remove(position);
                ListViewAdapter.this.notifyDataSetChanged();
                swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position-1));
                swipeLayout.close(false);
            }
        });
        v.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "share", Toast.LENGTH_SHORT).show();

//                //Send Email
//                Uri uri = Uri.parse("mailto:");
//                //String[] email = {"rwangfengdev@gmail.com"};
//                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Shared Event");
//                intent.putExtra(Intent.EXTRA_TEXT, "Event Notes");
//                mContext.startActivity(Intent.createChooser(intent, "Please select Email App"));

                Intent i = new Intent(mContext.getApplicationContext(), ShareScheduleActivity.class);
                i.putExtra(Globals.SCHEDULE_ID, dataScource.get(position).getId());
                mContext.startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Schedule singleSchedule = dataScource.get(position);

        TextView t = (TextView)convertView.findViewById(R.id.position);
        TextView text_data = (TextView)convertView.findViewById(R.id.text_data);

        if(type.equals("Time")){
            Date date = new Date(singleSchedule.getTime());
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
            t.setText("Event: " + singleSchedule.getTitle() + " Time: " + formattedDate);
            text_data.setText("Detail: " + singleSchedule.getNotes());
        }else{
            t.setText("Event: " + singleSchedule.getTitle() + Globals.ACTIVITIES[singleSchedule.getActivity()]);
            text_data.setText("Detail:" + singleSchedule.getNotes());
        }

    }

    @Override
    public int getCount() {
        return dataScource.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private void updateAfterDel(long pos){
        View v = viewList.get((int) pos - 1);
        Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
        ListViewAdapter.this.notifyDataSetChanged();
        swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId((int) pos -1));
        swipeLayout.close(false);
    }*/

    class DelScheduleTask extends AsyncTask<Schedule, Void, Void> {

        private String userName;
        @Override
        protected void onPreExecute(){
            SharedPreferences userProfile = mContext.getApplicationContext()
                    .getSharedPreferences("userProfile", mContext.MODE_PRIVATE);
            userName = userProfile.getString("USERNAME",null);
        }

        @Override
        protected Void doInBackground(Schedule... schedules) {
            Long id = schedules[0].getId();
            mScheduleDBHelper.removeSchedule(id);
            String server_id = userName + "," + schedules[0].getTime();
            Map<String, String> map = new HashMap<>();
            map.put("id", server_id);

            // Upload the history of all entries using upload().
            String uploadState="";
            try {
                ServerUtilities.post(Globals.SERVER_ADDR + "/deleteSchedule.do", map);
            } catch (IOException e1) {
                uploadState = "Sync failed: " + e1.getCause();
                Log.e("TAG", "data posting error " + e1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void pos) {
            //updateAfterDel(pos);
            Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
        }
    }
}
