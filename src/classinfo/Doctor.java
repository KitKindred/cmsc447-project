package classinfo;

import java.time.LocalDateTime;

public class Doctor extends Profession {
	private LocalDateTime attWeekTime;
    private boolean hadAttendingWeek;

    public Doctor(int id, int type, String name){
        super(id, type, name);
        hadAttendingWeek = false;
    }
    public void setAttendingDate(LocalDateTime att) {
    	this.attWeekTime=att;
    }
    public LocalDateTime getAttendingDate() {
    	return this.attWeekTime;
    }
    
    public void setAttending(boolean t) {
    	this.hadAttendingWeek=t;
    }
    public boolean getAttending() {
        return this.hadAttendingWeek;
    }

}
