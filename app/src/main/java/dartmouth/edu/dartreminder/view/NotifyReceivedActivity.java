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
import android.support.v4.app.NotificationCompat;
import android.view.ContextThemeWrapper;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.utils.Globals;

/**
 * Created by gejing on 3/4/16.
 */
public class NotifyReceivedActivity extends Activity {

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

        if(schedule == null) {
            vibrator.cancel();
            finish();
        }

        if(schedule != null && !schedule.getCompleted()){
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
                                // set this schedule as complete
                                schedule.setCompleted(true);
                                mScheduleDBHelper.updateSchedule(schedule);

                                dialog.cancel();
                                vibrator.cancel();
                                finish();
                            }
                        })
                        .setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // update schedule with 5 minutes later
                                schedule.setTime(schedule.getTime() + 60000 * 5);
                                mScheduleDBHelper.updateSchedule(schedule);

                                AlarmManager mgrAlarm = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                Intent intent = new Intent(getApplicationContext(), TimeReceiver.class);
                                intent.putExtra("id", row_id);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), row_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                        schedule.getTime(),
                                        pendingIntent);

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
