package dartmouth.edu.dartreminder.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.data.ScheduleDBHelper;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;private
    ScheduleDBHelper mScheduleDBHelper;
    private DelScheduleTask task = null;
    private ArrayList<Schedule> dataScource;

    private SwipeLayout swipeLayout;

    public ListViewAdapter(Context mContext, ArrayList dataSource) {
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



                mScheduleDBHelper = new ScheduleDBHelper(mContext);
                task = new DelScheduleTask();
                task.execute(dataScource.get(position).getId(), new Long(position));

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

                //Send Email
                Uri uri = Uri.parse("mailto:");
                //String[] email = {"rwangfengdev@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Shared Event");
                intent.putExtra(Intent.EXTRA_TEXT, "Event Notes");
                mContext.startActivity(Intent.createChooser(intent, "Please select Email App"));
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Schedule singleSchedule = dataScource.get(position);

        TextView t = (TextView)convertView.findViewById(R.id.position);
        t.setText(singleSchedule.getTitle());
        TextView text_data = (TextView)convertView.findViewById(R.id.text_data);
        text_data.setText(singleSchedule.getNotes());
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

    class DelScheduleTask extends AsyncTask<Long, String, Long> {

        @Override
        protected Long doInBackground(Long... rowId) {
            mScheduleDBHelper.removeSchedule(rowId[0]);
            return rowId[1];
        }

        @Override
        protected void onProgressUpdate(String... name) {
            //Toast.makeText(NewScheduleActivity.this, "Entry #" + name[0] + " saved", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Long pos) {
            //updateAfterDel(pos);
            Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
        }
    }
}
