package iiitd.ac.in.dsys.meetup.ObjectClasses;

/**
 * Created by vedantdasswain on 04/05/15.
 */
public class LocationObject {
    private String username,meetupname;
    private double lat=0,lon=0;
    private long time=0;

    public LocationObject(long time,double lat,double lon,String uname,String mname){
        this.username=uname;
        this.lat=lat;
        this.lon=lon;
        this.time=time;
        this.meetupname=mname;
    }

    public String getUsername(){
        return username;
    }

    public String getMeetupname(){
        return meetupname;
    }

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }

    public long getTime(){
        return time;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setMeetupname(String meetupname){
        this.meetupname=meetupname;
    }

    public void setLat(double lat){
        this.lat=lat;
    }

    public void setLon(double lon){
        this.lon=lon;
    }

    public void setTime(long time){
        this.time=time;
    }

}
