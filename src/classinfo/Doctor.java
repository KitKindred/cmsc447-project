package classinfo;

public class Doctor extends Profession {

    private boolean hadAttendingWeek;

    public Doctor(int id, int type, String name){
        super(id, type, name);
        hadAttendingWeek = false;
    }

    public void setAttending(boolean t) {
    	this.hadAttendingWeek=t;
    }
    public boolean getAttending() {
        return hadAttendingWeek;
    }

}
