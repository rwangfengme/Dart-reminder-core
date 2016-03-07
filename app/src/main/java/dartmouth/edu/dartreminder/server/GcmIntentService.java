package dartmouth.edu.dartreminder.server;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.utils.Utils;

public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                String msg = extras.getString("message");

                if (msg != null && !msg.isEmpty()) {
                    SharedPreferences userProfile = getApplicationContext().
                            getSharedPreferences("userProfile", MODE_PRIVATE);
                    String currentUserName = userProfile.getString("USERNAME", null);
                    String[] words = msg.split(",");

                    if (words[0].equals(currentUserName)) {
                        String id = words[1] + "," + words[2];
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Id", id);

                        try {
                            String response = ServerUtilities.post(Globals.SERVER_ADDR + "/fetchScheduleById.do", map);
                            JSONArray resultSet = new JSONArray(response);
                            Schedule schedule = Utils.JsonToSchedule(resultSet.getJSONObject(0));
                            DartReminderDBHelper dbHelper = new DartReminderDBHelper(getApplicationContext());
                            long user_id = dbHelper.insertSchedule(schedule);

                            //set broadcast to history fragment
                            Intent i = new Intent("ScheduleUpdate");
                            i.putExtra("id", user_id);
                            sendBroadcast(i);
                        }catch (IOException e){
                            showToast(e.toString());
                        }catch (JSONException e){
                            showToast(e.toString());
                        }
                    }
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}