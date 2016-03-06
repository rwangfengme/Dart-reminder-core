package dartmouth.edu.dartreminder.backend.data;

public class Schedule {
    private String Id;
    private String Title;
    private String Notes;
    private boolean UseTime;
    private Long Time;
    private String LocationName;
    private double Lat;
    private double Lng;
    private boolean Arrive;
    private double Radius;
    private int Priority;
    private int Repeat;
    private boolean isCompleted;
    private String userName;

    public Schedule() {
        this.Id = "";
        this.Title = "";
        this.Notes = "";
        this.UseTime = false;
        this.Time = System.currentTimeMillis();
        this.LocationName = "";
        this.Lat = 0;
        this.Lng = 0;
        this.Arrive = true;
        this.Radius = 0;
        this.Priority = 0;
        this.Repeat = 0;
        this.isCompleted = false;
        this.userName = "";
    }

    public Schedule(String title, String notes, String useTime, String time,
                    String locationName, String lat, String lng, String arrive, String radius,
                    String priority, String repeat, String isCompleted, String userName){
        this.Id = userName + "," + time;
        this.Title = title;
        this.Notes = notes;
        this.UseTime = Boolean.parseBoolean(useTime);
        this.Time = Long.parseLong(time);
        this.LocationName = locationName;
        this.Lat = Double.parseDouble(lat);
        this.Lng = Double.parseDouble(lng);
        this.Arrive = Boolean.parseBoolean(arrive);
        this.Radius = Double.parseDouble(radius);
        this.Priority = Integer.parseInt(priority);
        this.Repeat = Integer.parseInt(repeat);
        this.isCompleted = Boolean.parseBoolean(isCompleted);
        this.userName = userName;
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

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
}

