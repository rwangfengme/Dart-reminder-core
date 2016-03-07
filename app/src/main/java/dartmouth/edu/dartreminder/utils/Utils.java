package dartmouth.edu.dartreminder.utils;


import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import dartmouth.edu.dartreminder.data.Schedule;

/**
 * Created by LeoZhu on 3/6/16.
 */
public class Utils {
    public static JSONObject scheduleToJson(Schedule schedule, String userName, String sender){
            JSONObject rowObject = new JSONObject();
            try {
                rowObject.put(Schedule.FIELD_NAME_ID, userName + "," + schedule.getTime());
                rowObject.put(Schedule.FIELD_NAME_TITLE, schedule.getTitle());
                rowObject.put(Schedule.FIELD_NAME_NOTES, schedule.getNotes());
                rowObject.put(Schedule.FIELD_NAME_USE_TIME, schedule.getUseTime());
                rowObject.put(Schedule.FIELD_NAME_TIME, schedule.getTime());
                rowObject.put(Schedule.FIELD_NAME_LOCATION_NAME, schedule.getLocationName());
                rowObject.put(Schedule.FIELD_NAME_LAT, schedule.getLat());
                rowObject.put(Schedule.FIELD_NAME_LNG, schedule.getLng());
                rowObject.put(Schedule.FIELD_NAME_ARRIVE, schedule.getArrive());
                rowObject.put(Schedule.FIELD_NAME_RADIUS, schedule.getRadius());
                rowObject.put(Schedule.FIELD_NAME_PRIORITY, schedule.getPriority());
                rowObject.put(Schedule.FIELD_NAME_REPEAT, schedule.getRepeat());
                rowObject.put(Schedule.FIELD_NAME_IS_COMPLETED, schedule.getCompleted());
                rowObject.put(Schedule.FIELD_NAME_USER_NAME, userName);
                rowObject.put(Schedule.FIELD_NAME_SENDER, sender);
            } catch (Exception e) {

            }

            return rowObject;
    }

    public static Schedule JsonToSchedule(JSONObject scheduleElement){
        Schedule schedule = new Schedule();

        try{
            String id = scheduleElement.getString("Id");
            String title = scheduleElement.getString("Title");
            String notes = scheduleElement.getString("Notes");
            boolean useTime = Boolean.parseBoolean(scheduleElement.getString("UseTime"));
            Long time = Long.parseLong(scheduleElement.getString("Time"));
            String locationName = scheduleElement.getString("LocationName");
            double lat = Double.parseDouble(scheduleElement.getString("Lat"));
            double lng = Double.parseDouble(scheduleElement.getString("Lng"));
            boolean arrive = Boolean.parseBoolean(scheduleElement.getString("Arrive"));
            double radius = Double.parseDouble(scheduleElement.getString("Radius"));
            int priority = Integer.parseInt(scheduleElement.getString("Priority"));
            int repeat = Integer.parseInt(scheduleElement.getString("Repeat"));
            boolean isCompleted = Boolean.parseBoolean(scheduleElement.getString("isCompleted"));

            schedule.setTitle(title);
            schedule.setNotes(notes);
            schedule.setUseTime(useTime);
            schedule.setTime(time);
            schedule.setLocationName(locationName);
            schedule.setLat(lat);
            schedule.setLng(lng);
            schedule.setArrive(arrive);
            schedule.setRadius(radius);
            schedule.setPriority(priority);
            schedule.setRepeat(repeat);
            schedule.setCompleted(isCompleted);
        }catch (JSONException e){

        }

        return schedule;
    }
}
