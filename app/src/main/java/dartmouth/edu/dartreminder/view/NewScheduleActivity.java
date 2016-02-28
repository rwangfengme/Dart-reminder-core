package dartmouth.edu.dartreminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewScheduleActivity extends Activity {

    private Switch mSwitchAllDay;
    private TextView mNewScheduleDate;
    private TextView mNewScheduleTime;

    private Calendar mDateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        mNewScheduleDate = (TextView) findViewById(R.id.TextView_DayPicker);
        mNewScheduleTime = (TextView) findViewById(R.id.TextView_TimePicker);
        mNewScheduleDate.setVisibility(View.GONE);
        mNewScheduleTime.setVisibility(View.GONE);

        mDateAndTime = Calendar.getInstance();

        mSwitchAllDay = (Switch) findViewById(R.id.Switch_AllDayReminder);
        mSwitchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                if(isChecked) {
                    mNewScheduleDate.setVisibility(View.VISIBLE);
                    mNewScheduleTime.setVisibility(View.VISIBLE);
                } else {
                    mNewScheduleDate.setVisibility(View.GONE);
                    mNewScheduleTime.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onSaveClicked(View v) {
        finish();
    }

    public void onCancelClicked(View v) {
        v.setEnabled(false);
        finish();
    }

    public void onDatePickerClicked(View v) {
        Log.d("S", "ss");
        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
                //System.out.println(format.format(mDateAndTime.getTime()));
            }
        };

        new DatePickerDialog(this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onTimePickerClicked(View v) {
        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDateAndTime.set(Calendar.MINUTE, minute);
            }
        };

        new TimePickerDialog(this, mTimeListener,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), true).show();

    }
}
