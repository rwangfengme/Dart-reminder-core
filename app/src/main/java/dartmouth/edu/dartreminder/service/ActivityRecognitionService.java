package dartmouth.edu.dartreminder.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.view.NotifyActivityReceivedActivity;

/**
 * Created by _ReacTor on 16/3/5.
 */
public class ActivityRecognitionService extends IntentService{

    public static int UNCOMPLETED_ACT_NUM = 0;
    public static ArrayList<Integer> ACT_COUNTER = new ArrayList<>(Arrays.asList(0, 0, 0));
    public static ArrayList<Schedule> list = new ArrayList<>();

    private int still = 0;
    private int walking = 0;
    private int running = 0;
    private int actType;

    private static ArrayList<Integer> classifier = new ArrayList<>(Arrays.asList(0, 0, 0));
    private static int count = 0;
    private static boolean active = true;

    public ActivityRecognitionService() {
        super(Globals.GCM_AR);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(UNCOMPLETED_ACT_NUM != 0) {
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
            for (DetectedActivity da : detectedActivities) {
                Log.i(Globals.GCM_AR, da.getType() + " " + da.getConfidence() + "%");
                int daType = da.getType();
                if (daType == 3) {
                    still = da.getConfidence();
                } else if (daType == 7) {
                    walking = da.getConfidence();
                } else if (daType == 8) {
                    running = da.getConfidence();
                } else if (daType == 5 && da.getConfidence() == 100) {
                    still = 100;
                }
            }

            if (still > walking + running) {
                actType = 0;
                classifier.set(0, classifier.get(0) + 1);
            } else if (walking >= running) {
                actType = 1;
                classifier.set(1, classifier.get(1) + 1);
            } else if (walking < running) {
                actType = 2;
                classifier.set(2, classifier.get(2) + 1);
            }

            count++;
            if (count == 2) {
                int actNum = classifier.indexOf(Collections.max(classifier));
                if(ACT_COUNTER.get(actNum) != 0) {
                    String stageActivity = Globals.ACTIVITIES[actNum];
                    Intent dialogIntent = new Intent(getApplicationContext(), NotifyActivityReceivedActivity.class);
                    dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogIntent.putExtra(Globals.ACT_TITLE, "Activity");
                    dialogIntent.putExtra(Globals.ACT_TYPE, stageActivity);
                    startActivity(dialogIntent);
                    count = 0;
                    classifier.set(0, 0);
                    classifier.set(1, 0);
                    classifier.set(2, 0);
                    ACT_COUNTER.set(actNum, 0);
                    DartReminderDBHelper mScheduleDBHelper = new DartReminderDBHelper(getApplicationContext());
                    for(Schedule s : list){
                        if(s.getActivity() == actNum){
                            s.setCompleted(true);
                            mScheduleDBHelper.updateSchedule(s);
                        }
                    }

                }
                count = 0;
            }
        }
        //NotifyActivityReceivedActivity
    }
}
