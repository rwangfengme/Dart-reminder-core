package dartmouth.edu.dartreminder.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.utils.Globals;

public class DetailedInformationActivity extends AppCompatActivity {

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
        mSendto.setVisibility(View.GONE);
        mEditEmail.setVisibility(View.GONE);
        mSendButton.setVisibility(View.GONE);

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

        this.finish();
    }

    public void onCancelClicked(View v) {
        finish();
    }

}