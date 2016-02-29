package dartmouth.edu.dartreminder.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.data.ScheduleDBHelper;

public class NewScheduleActivity extends Activity {

    private Switch mSwitchAllDay;
    private Switch mSwitchLocation;
    private Switch mSwitchActivity;
    private TextView mNewScheduleDate;
    private TextView mNewScheduleTime;
    private TextView mChooseLocation;
    private TextView mDatePicker;
    private TextView mTimePicker;
    private Spinner mChoosePriority;
    private Spinner mChooseRepeat;

    private Calendar mDateAndTime;
    private boolean chooseTimeAlert = false, chooseLocationAlert = false;

    private Schedule schedule;
    private ScheduleDBHelper mScheduleDBHelper;
    private InsertDbTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        mDateAndTime = Calendar.getInstance();
        if(savedInstanceState != null) {
            mDateAndTime.setTimeInMillis(savedInstanceState.getLong("mDateAndTime"));
            chooseTimeAlert = savedInstanceState.getBoolean("chooseTimeAlert");
            chooseLocationAlert = savedInstanceState.getBoolean("chooseLocationAlert");
        } else {
            mDateAndTime.setTimeInMillis(System.currentTimeMillis());
        }

        mDatePicker = (TextView) findViewById(R.id.TextView_DayPicker);
        mTimePicker = (TextView) findViewById(R.id.TextView_TimePicker);

        mNewScheduleDate = (TextView) findViewById(R.id.TextView_DayPicker);
        mNewScheduleTime = (TextView) findViewById(R.id.TextView_TimePicker);
        mNewScheduleDate.setVisibility(View.GONE);
        mNewScheduleTime.setVisibility(View.GONE);

        mChooseLocation = (TextView) findViewById(R.id.TextView_Location);
        mChooseLocation.setVisibility(View.GONE);

        mChoosePriority = (Spinner) findViewById(R.id.Spinner_Priority);
        ArrayAdapter<String> arrayAdapterPriorities = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                Globals.PRIORITIES );
        mChoosePriority.setAdapter(arrayAdapterPriorities);

        mChooseRepeat = (Spinner) findViewById(R.id.Spinner_Repeat);
        ArrayAdapter<String> arrayAdapterRepeat = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                Globals.REPEAT );
        mChooseRepeat.setAdapter(arrayAdapterRepeat);

        mSwitchAllDay = (Switch) findViewById(R.id.Switch_AllDayReminder);
        mSwitchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State = ", "" + isChecked);
                if(isChecked) {
                    mNewScheduleDate.setVisibility(View.VISIBLE);
                    mNewScheduleTime.setVisibility(View.VISIBLE);
                    chooseTimeAlert = true;
                } else {
                    mNewScheduleDate.setVisibility(View.GONE);
                    mNewScheduleTime.setVisibility(View.GONE);
                    chooseTimeAlert = false;
                }
            }
        });

        mSwitchLocation = (Switch) findViewById(R.id.Switch_LocationReminder);
        mSwitchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mChooseLocation.setVisibility(View.VISIBLE);
                    chooseLocationAlert = true;
                } else {
                    mChooseLocation.setVisibility(View.GONE);
                    chooseLocationAlert = false;
                }
            }
        });

        mSwitchActivity = (Switch) findViewById(R.id.Switch_ActivityReminder);

        schedule = new Schedule();
        mScheduleDBHelper = new ScheduleDBHelper(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("mDateAndTime", mDateAndTime.getTimeInMillis());
        outState.putBoolean("chooseTimeAlert", chooseTimeAlert);
        outState.putBoolean("chooseLocationAlert", chooseLocationAlert);
    }

    public void onSaveClicked(View v) {

        schedule.setTitle(((EditText) findViewById(R.id.EditText_NewScheduleTitle)).getText()
                .toString());
        schedule.setNotes(((EditText) findViewById(R.id.EditText_NewScheduleNotes)).getText()
                .toString());

        if(chooseTimeAlert) {
            schedule.setTime(mDateAndTime.getTimeInMillis());
        }

        if(chooseLocationAlert) {

        }

        schedule.setPriority(mChoosePriority.getSelectedItemPosition());

        task = new InsertDbTask();
        task.execute();
        this.finish();
    }

    public void onCancelClicked(View v) {
        finish();
    }

    public void onDatePickerClicked(View v) {
        displayDialog(DialogFragment.DIALOG_ID_DATE);
    }

    public void onTimePickerClicked(View v) {
        displayDialog(DialogFragment.DIALOG_ID_TIME);
    }

    public void onLocationClicked(View v) {
        // start a map activity
    }

    public void displayDialog(int id) {
        android.app.DialogFragment fragment = DialogFragment.newInstance(id);
        fragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_general));
    }

    public void onDateSet(int year, int month, int day) {
        mDateAndTime.set(Calendar.YEAR, year);
        mDateAndTime.set(Calendar.MONTH, month);
        mDateAndTime.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        mDatePicker.setText(format.format(mDateAndTime.getTime()));
    }

    public void onTimeSet(int hour, int minute) {
        mDateAndTime.set(Calendar.HOUR_OF_DAY, hour);
        mDateAndTime.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aaa");
        mTimePicker.setText(format.format(mDateAndTime.getTime()));
    }

    class InsertDbTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... unused) {
            long id = mScheduleDBHelper.insertSchedule(schedule);
            publishProgress(Long.toString(id));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... name) {
            Toast.makeText(NewScheduleActivity.this, "Entry #" + name[0] + " saved", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            task = null;
        }
    }
}
