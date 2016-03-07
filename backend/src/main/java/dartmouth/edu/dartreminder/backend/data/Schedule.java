package dartmouth.edu.dartreminder.backend.data;

public class Schedule {
    public static final String SCHEDULE_ENTRY_ENTITY_NAME = "Schedule_Entry";

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

    private String Id;
    private String Title;
    private String Notes;
    private boolean UseTime;
    private String Time;
    private String LocationName;
    private double Lat;
    private double Lng;
    private boolean Arrive;
    private double Radius;
    private String Priority;
    private String Repeat;
    private boolean isCompleted;
    private String userName;
    private String sender;

    public Schedule() {
        this.Id = "";
        this.Title = "";
        this.Notes = "";
        this.UseTime = false;
        this.Time = "";
        this.LocationName = "";
        this.Lat = 0;
        this.Lng = 0;
        this.Arrive = true;
        this.Radius = 0;
        this.Priority = "0";
        this.Repeat = "0";
        this.isCompleted = false;
        this.userName = "";
        this.sender = "";
    }

    public Schedule(String id, String title, String notes, boolean useTime, String time,
                    String locationName, double lat, double lng, boolean arrive, double radius,
                    String priority, String repeat, boolean isCompleted, String userName, String sender){
        this.Id = id;
        this.Title = title;
        this.Notes = notes;
        this.UseTime = useTime;
        this.Time = time;
        this.LocationName = locationName;
        this.Lat = lat;
        this.Lng = lng;
        this.Arrive = arrive;
        this.Radius = radius;
        this.Priority = priority;
        this.Repeat = repeat;
        this.isCompleted = isCompleted;
        this.userName = userName;
        this.sender = sender;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
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

    public String getTime() {
        return this.Time;
    }

    public void setTime(String time) {
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

    public String getPriority() {
        return this.Priority;
    }

    public void setPriority(String priority) {
        this.Priority = priority;
    }

    public String getRepeat() {
        return this.Repeat;
    }

    public void setRepeat(String repeat) {
        this.Repeat = repeat;
    }

    public boolean getCompleted() {
        return this.isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getSender(){
        return this.sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }
}

