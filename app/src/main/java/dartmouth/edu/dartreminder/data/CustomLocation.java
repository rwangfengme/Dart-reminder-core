package dartmouth.edu.dartreminder.data;

/**
 * Created by LeoZhu on 2/28/16.
 */
public class CustomLocation {
    private long id;
    private String title;
    private double latitude;
    private double longitude;
    private String detail;
    private Integer icon;


    public CustomLocation(String title){
        this.title = title;
        this.latitude = 0;
        this.longitude = 0;
        this.detail = "";
        this.icon = 0;
    }

    public CustomLocation(String title, int icon){
        this.title = title;
        this.latitude = 0;
        this.longitude = 0;
        this.detail = "";
        this.icon = icon;
    }

    public CustomLocation(String title, double latitude, double longitude, String detail, int id){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detail = detail;
        this.icon = id;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongtitude(double longitude){
        this.longitude = longitude;
    }

    public String getDetail(){
        return this.detail;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public boolean hasDetail(){
        return (this.detail != null) && (!this.detail.isEmpty());
    }

    public int getIcon(){
        return this.icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}
