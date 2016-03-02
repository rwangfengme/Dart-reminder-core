package dartmouth.edu.dartreminder.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import dartmouth.edu.dartreminder.R;

/**
 * Created by gejing on 2/28/16.
 */
public class DialogFragment extends android.app.DialogFragment {

    public static final int DIALOG_ID_DATE = 1;
    public static final int DIALOG_ID_TIME = 2;
    private static final String DIALOG_ID_KEY = "dialog_id";

    public static DialogFragment newInstance(int dialog_id) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ID_KEY, dialog_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int dialog_id = getArguments().getInt(DIALOG_ID_KEY);
        final Calendar mDateAndTime = Calendar.getInstance();
        int hour, minute, year, month, day;

        switch (dialog_id) {

            case DIALOG_ID_DATE:
                year = mDateAndTime.get(Calendar.YEAR);
                month = mDateAndTime.get(Calendar.MONTH);
                day = mDateAndTime.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        ((NewScheduleActivity) getActivity()).onDateSet(year, month, day);
                    }
                }, year, month, day);

            case DIALOG_ID_TIME:
                hour = mDateAndTime.get(Calendar.HOUR_OF_DAY);
                minute = mDateAndTime.get(Calendar.MINUTE);

                return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        ((NewScheduleActivity) getActivity()).onTimeSet(hour, minute);
                    }
                }, hour, minute, false);
            default:
                return null;
        }
    }
}