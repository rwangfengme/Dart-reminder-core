package dartmouth.edu.dartreminder.data;

import android.location.Location;

/**
 * Created by gejing on 2/28/16.
 */
public class Schedule {
    public static final String FIELD_NAME_ID = "Id";
    public static final String FIELD_NAME_TITLE= "Title";
    public static final String FIELD_NAME_NOTES = "Notes";
    public static final String FIELD_NAME_USE_TIME = "UseTime";
    public static final String FIELD_NAME_TIME = "Time";
    public static final String FIELD_NAME_LOCATION_NAME = "LocationName";
    public static final String FIELD_NAME_LAT = "Lat";
    public static final String FIELD_NAME_LNG = "Lng";
    public static final String FIELD_NAME_ARRIVE = "Arrive";
    public static final String FIELD_NAME_RADIUS = "Radius";
    public static final String FIELD_NAME_PRIORITY = "Priority";
    public static final String FIELD_NAME_REPEAT = "Repeat";
    public static final String FIELD_NAME_IS_COMPLETED = "isCompleted";
    public static final String FIELD_NAME_USER_NAME = "userName";
    public static final String FIELD_NAME_SENDER = "sender";


    private Long Id;
    private String Title;
    private String Notes;
    private boolean UseTime;
    private Long Time;
    private String LocationName;
    private double Lat;
    private double Lng;
    private boolean Arrive;
    private double Radius;
    private int Activity;
    private int Priority;
    private int Repeat;
    private boolean isCompleted;

    public Schedule() {
        this.Title = "";
        this.Notes = "";
        this.UseTime = false;
        this.Time = System.currentTimeMillis();
        this.LocationName = "";
        this.Lat = 0;
        this.Lng = 0;
        this.Arrive = true;
        this.Radius = 0;
        this.Activity = -1;
        this.Priority = 0;
        this.Repeat = 0;
        this.isCompleted = false;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getNotes() {
        return this.Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }

    public boolean getUseTime() {
        return this.UseTime;
    }

    public void setUseTime(boolean usetime) {
        this.UseTime = usetime;
    }

    public Long getTime() {
        return this.Time;
    }

    public void setTime(Long time) {
        this.Time = time;
    }

    public String getLocationName() {
        return this.LocationName;
    }

    public void setLocationName(String location) {
        this.LocationName = location;
    }

    public double getLat() {
        return this.Lat;
    }

    public void setLat(double lat) {
        this.Lat = lat;
    }

    public double getLng() {
        return this.Lng;
    }

    public void setLng(double lng) {
        this.Lng = lng;
    }

    public boolean getArrive() {
        return this.Arrive;
    }

    public void setArrive(boolean arrive) {
        this.Arrive = arrive;
    }

    public double getRadius() {
        return this.Radius;
    }

    public void setRadius(double radius) {
        this.Radius = radius;
    }

    public int getActivity() {
        return this.Activity;
    }

    public void setActivity (int activity) {
        this.Activity = activity;
    }

    public int getPriority() {
        return this.Priority;
    }

    public void setPriority(int priority) {
        this.Priority = priority;
    }

    public int getRepeat() {
        return this.Repeat;
    }

    public void setRepeat(int repeat) {
        this.Repeat = repeat;
    }

    public boolean getCompleted() {
        return this.isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(this.Lat);
        location.setLongitude(this.Lng);
        return location;
    }
}

