package dartmouth.edu.dartreminder.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.utils.Globals;

/**
 * Created by _ReacTor on 16/3/5.
 */
public class ActivityRecognitionService extends IntentService{
    private int still = 0;
    private int walking = 0;
    private int running = 0;
    private int actType;

    public ActivityRecognitionService() {
        super(Globals.GCM_AR);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Globals.AR_BROADCAST);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        still = 0;
        walking = 0;
        running = 0;
        // Log each activity.
        for (DetectedActivity da: detectedActivities) {
            Log.i(Globals.GCM_AR,  da.getType() + " " + da.getConfidence() + "%");
            int daType = da.getType();
            if(daType == 3){
                still = da.getConfidence();
            }else if(daType == 7){
                walking = da.getConfidence();
            }else if(daType == 8){
                running =da.getConfidence();
            }else if(daType == 5 && da.getConfidence() == 100){
                still = 100;
            }
        }

        if(still > walking+running){
            actType = 0;
        }else if(walking >= running){
            actType = 1;
        }else if(walking < running){
            actType = 2;
        }
        Log.d(Globals.GCM_AR, actType+"|"+still+"/"+walking+"/"+running);
    }
}
