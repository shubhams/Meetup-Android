package iiitd.ac.in.dsys.meetup.ObjectClasses;

/**
 * Created by vedantdasswain on 07/04/15.
 */
public class ContactObject {
    private String name;
    private boolean isInvited;

    public ContactObject(String name, Boolean isInvited){
        this.name=name;
        this.isInvited=isInvited;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())){
            ContactObject co=(ContactObject)o;
            if(co.getName().equals(this.name) &&
                    co.getInvited()==co.isInvited)
                return true;
        }
        return false;
    }

    public String getName(){
        return  this.name;
    }

    public void setName(String name){
        this.name=name;
    }

    public boolean getInvited(){
        return  this.isInvited;
    }

    public void setInvited(boolean state){
        this.isInvited=state;
    }
}
