package dartmouth.edu.dartreminder.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.utils.Globals;

public class NotifyLocationReceivedActivity extends Activity {

    private DartReminderDBHelper mScheduleDBHelper;
    private Schedule schedule;
    private Vibrator vibrator;
    private Context mContext;
    private int row_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mContext = getApplicationContext();
        row_id = intent.getIntExtra(Globals.SCHEDULE_ID, -1);
        mScheduleDBHelper = new DartReminderDBHelper(getApplicationContext());
        schedule = mScheduleDBHelper.fetchScheduleByIndex(row_id);
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(Globals.mVibratePattern, 1);

        if(schedule != null) {
            // set notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(schedule.getTitle())
                            .setContentText(schedule.getNotes());
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(row_id, mBuilder.build());

            // set alert box
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Holo_Dialog_Alert));
            builder.setTitle(schedule.getTitle())
                    .setMessage(schedule.getNotes())
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(mContext, LocationDetailActivity.class);
                            i.putExtra(Globals.MSG_LOCATION_ALARM, true);
                            i.putExtra(Globals.LOCATION_TITLE, schedule.getTitle());
                            i.putExtra(Globals.LOCATION_DETAIL, schedule.getNotes());
                            i.putExtra(Globals.LOCATION_LAT, schedule.getLat());
                            i.putExtra(Globals.LOCATION_LNG, schedule.getLng());
                            startActivity(i);
                            dialog.cancel();
                            vibrator.cancel();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            vibrator.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

}
