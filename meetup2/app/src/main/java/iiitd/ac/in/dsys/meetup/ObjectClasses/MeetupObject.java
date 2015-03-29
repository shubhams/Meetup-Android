package iiitd.ac.in.dsys.meetup.ObjectClasses;

/**
 * Created by vedantdasswain on 24/03/15.
 */
public class MeetupObject {
    private String name,owner;
    private float lat,lon;
    private long timeOfArrival;
    private boolean isActive;

    public MeetupObject(String name,String owner,boolean isActive,
                        float lat,float lon,long timeOfArrival,String location){
        this.name=name;
        this.lat=lat;
        this.lon=lon;
        this.timeOfArrival=timeOfArrival;
        this.owner=owner;
        this.isActive=isActive;
    }

    public String getName(){
        return name;
    }

    public String getOwner(){
        return owner;
    }

    public float getLat(){
        return lat;
    }

    public float getLon(){
        return lon;
    }

    public long getTimeOfArrival(){
        return timeOfArrival;
    }

    public boolean getActive(){
        return isActive;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setOwner(String owner){
        this.owner=owner;
    }

    public void setLat(float lat){
        this.lat=lat;
    }

    public void setLon(float lon){
        this.lon=lon;
    }

    public void setTimeOfArrival(long timeOfArrival){
        this.timeOfArrival=timeOfArrival;
    }

    public void setActive(boolean isActive){
        this.isActive=isActive;
    }


}
