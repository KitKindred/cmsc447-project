package classinfo;

import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/**
 * The Class Doctor.
 */
public class Doctor extends Profession {
	
	/** The att week time. */
	// the start time of the doctors attending week
	private LocalDateTime attWeekTime = null;
	
	/** The had attending week. */
	// whether or not they have had the attending week
    private boolean hadAttendingWeek;

    /**
     * Instantiates a new doctor.
     *
     * @param id = the internal id of the doctor
     * @param type = the type of the doctor, should be 0 for doctors
     * @param name = the name of the doctor
     */
    public Doctor(int id, int type, String name){
        super(id, type, name);
        hadAttendingWeek = false;
    }
    
    /**
     * Sets the start date for the attending week.
     *
     * @param att = the time of the start of the attending week
     */
    public void setAttendingDate(LocalDateTime att) {
    	this.attWeekTime=att;
    }
    
    /**
     * Gets the attending date.
     *
     * @return the time of the start of the doctors attending week
     */
    public LocalDateTime getAttendingDate() {
    	return this.attWeekTime;
    }
    
    /**
     * sets whether the doctor has had an attending week or not.
     *
     * @param t = whether or not the doctor has had an attending week
     */
    public void setAttending(boolean t) {
    	this.hadAttendingWeek=t;
    }
    
    /**
     * Gets the attending.
     *
     * @return whether or not the doctor has had an attending week
     */
    public boolean getAttending() {
        return this.hadAttendingWeek;
    }

}
