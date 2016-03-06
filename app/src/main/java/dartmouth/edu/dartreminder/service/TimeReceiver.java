package dartmouth.edu.dartreminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.view.NotifyLocationReceivedActivity;
import dartmouth.edu.dartreminder.view.NotifyReceivedActivity;

/**
 * Created by gejing on 3/4/16.
 */
public class TimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        // Log.d("The id is: ", Integer.toString(id));
        Intent i = new Intent(context, NotifyReceivedActivity.class);
        i.putExtra(Globals.SCHEDULE_ID, id);
        i.putExtra(Globals.INTENT_TYPE, Globals.TIME_INTENT);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
