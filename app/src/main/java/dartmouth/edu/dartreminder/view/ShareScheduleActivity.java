package dartmouth.edu.dartreminder.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.server.ServerUtilities;
import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.utils.Utils;

public class ShareScheduleActivity extends AppCompatActivity {

    private long id = -1;

    private Switch mSwitchAllDay;
    private Switch mSwitchLocation;
    private Switch mSwitchActivity;
    private TextView mSendto;
    private EditText mEditEmail;
    private EditText mEditTitle;
    private EditText mEditNote;
    private TextView mNewScheduleDate;
    private TextView mNewScheduleTime;
    private TextView mChooseLocation;
    private TextView mDatePicker;
    private TextView mTimePicker;
    private Spinner mChoosePriority;
    private Spinner mChooseRepeat;
    private Spinner mChooseActivity;
    private Button mSendButton;

    private Schedule schedule;
    private DartReminderDBHelper mScheduleDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        Intent intent = getIntent();
        id = intent.getLongExtra(Globals.SCHEDULE_ID, -1);
        mScheduleDBHelper = new DartReminderDBHelper(this);
        schedule = mScheduleDBHelper.fetchScheduleByIndex(id);

        mSendto = (TextView) findViewById(R.id.TextView_SendTo);
        mEditEmail = (EditText) findViewById(R.id.EditText_EditEmail);
        mSendButton = (Button) findViewById(R.id.Button_SaveNewSchedule);
        mSendButton.setText("Send");

        mNewScheduleDate = (TextView) findViewById(R.id.TextView_DayPicker);
        mNewScheduleTime = (TextView) findViewById(R.id.TextView_TimePicker);
        mNewScheduleDate.setVisibility(View.GONE);
        mNewScheduleTime.setVisibility(View.GONE);


        mDatePicker = (TextView) findViewById(R.id.TextView_DayPicker);
        mTimePicker = (TextView) findViewById(R.id.TextView_TimePicker);
        mDatePicker.setVisibility(View.GONE);
        mTimePicker.setVisibility(View.GONE);

        mChooseLocation = (TextView) findViewById(R.id.TextView_Location);
        mChooseLocation.setVisibility(View.GONE);

        mChooseActivity = (Spinner) findViewById(R.id.Spinner_Activity);
        ArrayAdapter<String> arrayAdapterActivities = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_text,
                Globals.ACTIVITIES);
        mChooseActivity.setAdapter(arrayAdapterActivities);
        mChooseActivity.setVisibility(View.GONE);

        mSwitchAllDay = (Switch) findViewById(R.id.Switch_AllDayReminder);
        mSwitchLocation = (Switch) findViewById(R.id.Switch_LocationReminder);
        mSwitchActivity = (Switch) findViewById(R.id.Switch_ActivityReminder);
        mChooseActivity = (Spinner) findViewById(R.id.Spinner_Activity);

        mChoosePriority = (Spinner) findViewById(R.id.Spinner_Priority);
        ArrayAdapter<String> arrayAdapterPriorities = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_text,
                Globals.PRIORITIES );
        mChoosePriority.setAdapter(arrayAdapterPriorities);

        mChooseRepeat = (Spinner) findViewById(R.id.Spinner_Repeat);
        ArrayAdapter<String> arrayAdapterRepeat = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item_text,
                Globals.REPEAT );
        mChooseRepeat.setAdapter(arrayAdapterRepeat);

        if(schedule == null) {
            finish();
        } else {
            mEditTitle = (EditText) findViewById(R.id.EditText_NewScheduleTitle);
            mEditTitle.setText(schedule.getTitle());
            mEditNote = (EditText) findViewById(R.id.EditText_NewScheduleNotes);
            mEditNote.setText(schedule.getNotes());
            if(schedule.getUseTime()){
                mSwitchAllDay.setChecked(true);
                mNewScheduleDate.setVisibility(View.VISIBLE);
                mNewScheduleTime.setVisibility(View.VISIBLE);
                SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd, yyyy");
                mDatePicker.setText(format.format(schedule.getTime()));
                //mDatePicker.setEnabled(false);
                format = new SimpleDateFormat("hh:mm aaa");
                mTimePicker.setText(format.format(schedule.getTime()));
                //mTimePicker.setEnabled(false);
            }
            mSwitchAllDay.setEnabled(false);

            if(!schedule.getLocationName().equals("")){
                mSwitchLocation.setChecked(true);
                mChooseLocation.setVisibility(View.VISIBLE);
                mChooseLocation.setText(schedule.getLocationName());
                //mChooseLocation.setEnabled(false);
            }
            mSwitchLocation.setEnabled(false);

            if(schedule.getActivity() != -1){
                mSwitchActivity.setChecked(true);
                mChooseActivity.setVisibility(View.VISIBLE);
                mChooseActivity.setSelection(schedule.getActivity());
                mChooseActivity.setEnabled(false);
            }
            mSwitchActivity.setEnabled(false);

            mChooseRepeat.setSelection(schedule.getRepeat());
            mChooseRepeat.setEnabled(false);
            mChoosePriority.setSelection(schedule.getPriority());
            mChoosePriority.setEnabled(false);
        }
    }

    public void onDatePickerClicked(View v) {

    }

    public void onTimePickerClicked(View v) {

    }

    public void onLocationClicked(View v) {

    }

    public void onSaveClicked(View v) {
        String sender = mEditEmail.getText().toString();
        if (sender == null || sender.isEmpty()){
            Toast.makeText(getApplicationContext(), "Error: Sender Email is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        ShareScheduleTask shareScheduleTask = new ShareScheduleTask();
        shareScheduleTask.execute();
        this.finish();
    }

    public void onCancelClicked(View v) {
        finish();
    }


    public class ShareScheduleTask extends AsyncTask<Void, String, String> {
        private String userName;
        private String sender;
        @Override
        protected void onPreExecute(){
            SharedPreferences userProfile = getApplicationContext().getSharedPreferences("userProfile", MODE_PRIVATE);
            sender = userProfile.getString("USERNAME",null);
            userName = mEditEmail.getText().toString();
        }

        @Override
        protected String doInBackground(Void... unused) {
            //sync schedule onto GAE
            JSONArray resultSet = new JSONArray();
            resultSet.put(Utils.scheduleToJson(schedule, userName, sender));

            //put JsonArray into a map
            Map<String, String> map = new HashMap<>();
            map.put("ScheduleKey", resultSet.toString());

            // Upload the history of all entries using upload().
            String uploadState="";
            try {
                ServerUtilities.post(Globals.SERVER_ADDR + "/addSchedule.do", map);
            } catch (IOException e1) {
                uploadState = "Sync failed: " + e1.getCause();
                Log.e("TAG", "data posting error " + e1);
                return uploadState;
            }

            Log.d("TAG_NAME", resultSet.toString());
            return uploadState;
        }

        @Override
        protected void onPostExecute(String uploadState) {
            Toast.makeText(getApplicationContext(), uploadState, Toast.LENGTH_SHORT).show();
        }
    }
}
