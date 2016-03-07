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

    private Vibrator vibrator;
    private Context mContext;
    private boolean fromLocation = false;
    private String title = "";
    private String notes = "";
    private String location = "";
    private double lat = 0;
    private double lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mContext = getApplicationContext();
        fromLocation = intent.getBooleanExtra(Globals.MSG_LOCATION_ALARM, false);
        title = intent.getStringExtra(Globals.SCHEDULE_TITLE);
        notes = intent.getStringExtra(Globals.SCHEDULE_NOTE);
        location = intent.getStringExtra(Globals.LOCATION_TITLE);
        lat = intent.getDoubleExtra(Globals.LOCATION_LAT, 0);
        lng = intent.getDoubleExtra(Globals.LOCATION_LNG, 0);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(Globals.mVibratePattern, 1);

            // set notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(notes);
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());

            // set alert box
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Holo_Dialog_Alert));
            builder.setTitle(title)
                    .setMessage(notes)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(mContext, LocationDetailActivity.class);
                            i.putExtra(Globals.MSG_LOCATION_ALARM, fromLocation);
                            i.putExtra(Globals.SCHEDULE_TITLE, title);
                            i.putExtra(Globals.SCHEDULE_NOTE, notes);
                            i.putExtra(Globals.LOCATION_TITLE, location);
                            i.putExtra(Globals.LOCATION_LAT, lat);
                            i.putExtra(Globals.LOCATION_LNG, lng);
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
