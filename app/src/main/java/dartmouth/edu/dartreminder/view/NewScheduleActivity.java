package dartmouth.edu.dartreminder.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.utils.Globals;

public class NewScheduleActivity extends AppCompatActivity {

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
    private boolean useLocation = false;
    private String mLocationTitle = "";
    private boolean mArrive = true;
    private double mRadius = 0.0, mLat = 0.0, mLng = 0.0;
    private boolean chooseDateAlert = false, chooseTimeAlert = false, chooseLocationAlert = false;

    private Schedule schedule;
    private DartReminderDBHelper mScheduleDBHelper;
    private InsertDbTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

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
                this,
                android.R.layout.simple_list_item_1,
                Globals.PRIORITIES );
        mChoosePriority.setAdapter(arrayAdapterPriorities);

        mChooseRepeat = (Spinner) findViewById(R.id.Spinner_Repeat);
        ArrayAdapter<String> arrayAdapterRepeat = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Globals.REPEAT );
        mChooseRepeat.setAdapter(arrayAdapterRepeat);

        mDateAndTime = Calendar.getInstance();
        if(savedInstanceState != null) {
            mDateAndTime.setTimeInMillis(savedInstanceState.getLong("mDateAndTime"));
            chooseDateAlert = savedInstanceState.getBoolean("chooseDateAlert");
            if(chooseDateAlert) {
                SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd, yyyy");
                mDatePicker.setText(format.format(mDateAndTime.getTime()));
            }
            chooseTimeAlert = savedInstanceState.getBoolean("chooseTimeAlert");
            if(chooseTimeAlert) {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm aaa");
                mTimePicker.setText(format.format(mDateAndTime.getTime()));
            }
            chooseLocationAlert = savedInstanceState.getBoolean("chooseLocationAlert");

            useLocation = savedInstanceState.getBoolean("useLocation");
            mArrive = savedInstanceState.getBoolean("Arrive");
            mLocationTitle = savedInstanceState.getString("Location_Title");
            mRadius = savedInstanceState.getDouble("Radius");
            mLat = savedInstanceState.getDouble("Lat");
            mLng = savedInstanceState.getDouble("Lng");
        } else {
            mDateAndTime.setTimeInMillis(System.currentTimeMillis());
        }

        mSwitchAllDay = (Switch) findViewById(R.id.Switch_AllDayReminder);
        mSwitchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State = ", "" + isChecked);
                if(isChecked) {
                    mNewScheduleDate.setVisibility(View.VISIBLE);
                    mNewScheduleTime.setVisibility(View.VISIBLE);
                } else {
                    mNewScheduleDate.setVisibility(View.GONE);
                    mNewScheduleTime.setVisibility(View.GONE);
                    chooseDateAlert = false;
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
        mScheduleDBHelper = new DartReminderDBHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        useLocation = data.getBooleanExtra(Globals.SAVE, false);
        if(useLocation){
            mLocationTitle = data.getStringExtra(Globals.LOCATION_NAME);
            mArrive = data.getBooleanExtra(Globals.ARRIVE, true);
            mRadius = data.getDoubleExtra(Globals.RADIUS, 0.0);
            mLat = data.getDoubleExtra(Globals.LAT, 0.0);
            mLng = data.getDoubleExtra(Globals.LNG, 0.0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("mDateAndTime", mDateAndTime.getTimeInMillis());
        outState.putBoolean("chooseDateAlert", chooseDateAlert);
        outState.putBoolean("chooseTimeAlert", chooseTimeAlert);
        outState.putBoolean("chooseLocationAlert", chooseLocationAlert);
        outState.putBoolean("useLocation", useLocation);
        outState.putString("Location_Title", mLocationTitle);
        outState.putBoolean("Arrive", mArrive);
        outState.putDouble("Radius", mRadius);
        outState.putDouble("Lat", mLat);
        outState.putDouble("Lng", mLng);
    }

    public void onSaveClicked(View v) {

        schedule.setTitle(((EditText) findViewById(R.id.EditText_NewScheduleTitle)).getText()
                .toString());
        schedule.setNotes(((EditText) findViewById(R.id.EditText_NewScheduleNotes)).getText()
                .toString());

        schedule.setUseTime(chooseDateAlert || chooseTimeAlert);
        schedule.setTime(mDateAndTime.getTimeInMillis());

        if(chooseLocationAlert) {
            schedule.setLocationName(mLocationTitle);
            schedule.setArrive(mArrive);
            schedule.setRadius(mRadius);
            schedule.setLat(mLat);
            schedule.setLng(mLng);
        }

        schedule.setRepeat(mChooseRepeat.getSelectedItemPosition());
        schedule.setPriority(mChoosePriority.getSelectedItemPosition());

        // write your own check function
        schedule.setCompleted(false);

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
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, 1);
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
        chooseDateAlert = true;
    }

    public void onTimeSet(int hour, int minute) {
        mDateAndTime.set(Calendar.HOUR_OF_DAY, hour);
        mDateAndTime.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aaa");
        mTimePicker.setText(format.format(mDateAndTime.getTime()));
        chooseTimeAlert = true;
    }

    class InsertDbTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... unused) {
            long id = mScheduleDBHelper.insertSchedule(schedule);
            publishProgress(Long.toString(id));

            if(schedule.getUseTime() && !schedule.getCompleted()) {
                AlarmManager mgrAlarm = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), TimeReceiver.class);
                intent.putExtra("id", (int) id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                        mDateAndTime.getTimeInMillis(),
                        pendingIntent);

//                pendingIntent.cancel();
//                mgrAlarm.cancel(pendingIntent);
            }

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
