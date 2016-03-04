package dartmouth.edu.dartreminder.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.view.DialogFragment;
import dartmouth.edu.dartreminder.view.NewScheduleActivity;

/**
 * Created by gejing on 3/4/16.
 */
public class TimeReceiver extends BroadcastReceiver {
    private DartReminderDBHelper mScheduleDBHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("idfromtask", -1);
        Log.d("The id is: ", Integer.toString(id));
        mScheduleDBHelper = new DartReminderDBHelper(context);
        Schedule schedule = mScheduleDBHelper.fetchScheduleByIndex(id);
        if(!schedule.getCompleted()){
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(10000);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(schedule.getTitle())
                            .setContentText(schedule.getNotes());
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, mBuilder.build());
        }


        startRLDialog(context);
    }

    private void startRLDialog(Context context) {

    }
}
