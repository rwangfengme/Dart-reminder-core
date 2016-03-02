package dartmouth.edu.dartreminder.data;

import com.daimajia.easing.bounce.BounceEaseOut;

/**
 * Created by LeoZhu on 2/28/16.
 */
public class CustomLocation {
    private long id;
    private String title;
    private double latitude;
    private double longitude;
    private String detail;

    public CustomLocation(String title){
        this.title = title;
        this.latitude = 0;
        this.longitude = 0;
        this.detail = "";
    }

    public CustomLocation(String title, double latitude, double longitude, String detail){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detail = detail;
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

}
