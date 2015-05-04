package iiitd.ac.in.dsys.meetup.ObjectClasses;

/**
 * Created by vedantdasswain on 24/03/15.
 */
public class MeetupObject {
    private String name,owner;
    private double lat=0,lon=0;
    private long timeOfArrival=0;
    private boolean isActive,isAccepted;

    public MeetupObject(String name,String owner,boolean isActive,boolean isAccepted){
        this.name=name;
//        this.lat=lat;
//        this.lon=lon;
//        this.timeOfArrival=timeOfArrival;
        this.owner=owner;
        this.isActive=isActive;
        this.isAccepted=isAccepted;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())) {
            MeetupObject mo=(MeetupObject)o;
            if(this.name.equals(mo.getName()) &&
                    this.owner.equals(mo.getOwner()) &&
                    this.lat==mo.getLat() &&
                    this.lon==mo.getLon() &&
                    this.timeOfArrival== mo.getTimeOfArrival()
//                    &&
//                    this.isActive==mo.getActive()
                    ) {
                return true;
            }
        }
        return false;
    }

    public String getName(){
        return name;
    }

    public String getOwner(){
        return owner;
    }

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }

    public long getTimeOfArrival(){
        return timeOfArrival;
    }

    public boolean getActive(){
        return isActive;
    }

    public boolean getAccepted(){
        return isAccepted;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setOwner(String owner){
        this.owner=owner;
    }

    public void setLat(double lat){
        this.lat=lat;
    }

    public void setLon(double lon){
        this.lon=lon;
    }

    public void setTimeOfArrival(long timeOfArrival){
        this.timeOfArrival=timeOfArrival;
    }

    public void setActive(boolean isActive){
        this.isActive=isActive;
    }

    public void setAccepted(boolean isAccepted){
        this.isAccepted=isAccepted;
    }

}
