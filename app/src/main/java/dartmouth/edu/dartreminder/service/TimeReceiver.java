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
import dartmouth.edu.dartreminder.view.NewScheduleActivity;
import dartmouth.edu.dartreminder.view.NotifyReceivedActivity;

/**
 * Created by gejing on 3/4/16.
 */
public class TimeReceiver extends BroadcastReceiver {
    private DartReminderDBHelper mScheduleDBHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        // Log.d("The id is: ", Integer.toString(id));
        Intent i = new Intent(context, NotifyReceivedActivity.class);
        i.putExtra("id", id);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
