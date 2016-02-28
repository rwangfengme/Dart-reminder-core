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

public class NewScheduleActivity extends Activity {

    private Switch mSwitchAllDay;
    private Switch mSwitchLocation;
    private TextView mNewScheduleDate;
    private TextView mNewScheduleTime;
    private ImageButton mChooseLocation;
    private Spinner mChoosePriority;

    private Calendar mDateAndTime;
    private String mDate = "", mTime = "";

    private Schedule schedule;
    private InsertDbTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        mDateAndTime = Calendar.getInstance();
        if(savedInstanceState != null) {
            mDateAndTime.setTimeInMillis(savedInstanceState.getLong("mDateAndTime"));
//            mDate = Integer.toString(mDateAndTime.get(Calendar.YEAR));
//            mTime = Integer.toString(mDateAndTime.get(Calendar.HOUR_OF_DAY));
        } else {
            mDateAndTime.setTimeInMillis(System.currentTimeMillis());
        }

        mNewScheduleDate = (TextView) findViewById(R.id.TextView_DayPicker);
        mNewScheduleTime = (TextView) findViewById(R.id.TextView_TimePicker);
        mNewScheduleDate.setVisibility(View.GONE);
        mNewScheduleTime.setVisibility(View.GONE);

        mChooseLocation = (ImageButton) findViewById(R.id.ImageButton_ChooseLocation);
        mChooseLocation.setImageResource(R.drawable.ic_menu_slideshow);
        mChooseLocation.setVisibility(View.GONE);

        mChoosePriority = (Spinner) findViewById(R.id.Spinner_Priority);
        ArrayAdapter<String> arrayAdapterPriorities = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                Globals.PRIORITIES );
        mChoosePriority.setAdapter(arrayAdapterPriorities);

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
                }
            }
        });

        mSwitchLocation = (Switch) findViewById(R.id.Switch_LocationReminder);
        mSwitchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mChooseLocation.setVisibility(View.VISIBLE);
                } else {
                    mChooseLocation.setVisibility(View.GONE);
                }
            }
        });

        schedule = new Schedule();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("mDateAndTime", mDateAndTime.getTimeInMillis());
    }

    public void onSaveClicked(View v) {
        schedule.setTitle(((EditText) findViewById(R.id.EditText_NewScheduleTitle)).getText()
                .toString());
        schedule.setNotes(((EditText) findViewById(R.id.EditText_NewScheduleNotes)).getText()
                .toString());
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

    public void displayDialog(int id) {
        android.app.DialogFragment fragment = DialogFragment.newInstance(id);
        fragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_general));
    }

    public void onDateSet(int year, int month, int day) {
        mDateAndTime.set(Calendar.YEAR, year);
        mDateAndTime.set(Calendar.MONTH, month);
        mDateAndTime.set(Calendar.DAY_OF_MONTH, day);
        // mDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
    }

    public void onTimeSet(int hour, int minute) {
        mDateAndTime.set(Calendar.HOUR_OF_DAY, hour);
        mDateAndTime.set(Calendar.MINUTE, minute);
        // mTime = Integer.toString(mDateAndTime.get(Calendar.HOUR_OF_DAY)) + mDateAndTime.get(Calendar.MINUTE);
    }

    class InsertDbTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            // publishProgress(Long.toString(e.getId()));
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
