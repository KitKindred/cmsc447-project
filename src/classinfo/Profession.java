package classinfo;
import java.time.LocalDateTime;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Profession.
 */
public class Profession {
	
	// id is the id of the employee used in the hashmap.
	/** The type. */
	// type is the type of employee, 0 = Doctor, 1 = Moonlighter, 2 = Intern.
    protected int id, type; 
    
    /** The week evening. */
    // these store information about how much of specific types of shifts the employee has worked
    protected int hoursWorked, weekendDay, weekendNight, weekDay, weekNight, weekEvening;
    
    /** The active. */
    // active is the state of the worker, 0 = active, 1 = inactive, 2 = on maternity leave
    protected int active;
    
    /** The name. */
    // standard employee information 
    protected String name;
    
    /** The email. */
    protected String email;
    
    // personalShifts is the shifts assigned to an employee over the time period, is populated
    /** The personal shifts. */
    // when schedule is generated
    protected ArrayList<Shift> personalShifts;
    
    /** The tor. */
    // tor is the time off requests that the employee has submitted that are in the system
    protected ArrayList<TimeOffRequest> tor;

    /** The ldt inactive. */
    // ldtInactive is the date that the employee went to an inactive state, if applicable
    protected LocalDateTime ldtInactive=null;
    
    
    /**
     * Default Constructor.
     */
    protected Profession() {
        this.id = 0;
        this.type = 0;
        this.name = "";
        this.email = "";
        
        this.weekendDay=0;
        this.weekendNight=0;
        this.weekDay=0;
        this.weekEvening=0;
        this.weekNight=0;  
    }

    /**
     * Regular Constructor.
     *
     * @param id = id of employee used in the hashmap
     * @param type = type of employee, doctor, moonlighter, or intern
     * @param name = name of employee
     */
    protected Profession(int id, int type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
        
        // set the active state to active and clear hours worked, these will be manually set later
        this.active = 0;
        this.hoursWorked = 0;
        
        personalShifts = new ArrayList<Shift>();
        tor = new ArrayList<TimeOffRequest>();
    }
    
    /** 
     * Adds the number of hours worked to the employees current hour count.
     * @param numHours = number of hours worked to be added
     */
    public void addHours(int numHours) {
        this.hoursWorked += numHours;
    }

    /**
     * Directly sets the number of hours worked by the employee.
     *
     * @param numHours = number of hours the employee has worked
     */
    public void setHoursWorked(int numHours) {
        this.hoursWorked = numHours;
    }

    
    /**
     * Directly sets the employees name.
     *
     * @param name = name of the employee
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Directly sets the employees email.
     *
     * @param email = email of the employee
     */
    public void setEmail(String email) {
    	this.email=email;
    }

    /**
     * Directly sets the type of the employee.
     *
     * @param type = type of the employee, 0 = doctor, 1 = moonlighter, 2 = intern
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Directly sets the active state of the employee.
     *
     * @param active = active state of the employee, 0 = active, 1 = inactive, 2 = maternity leave
     */
    public void setActive(int active) {
        this.active = active;
    }

    
    /**
     * Sets when the employee started being inactive.
     *
     * @param a = the time the employee started being inactive
     */
    public void setInactiveDate(LocalDateTime a) {
        this.ldtInactive = a;
    }
    
    /**
     * Adds a shift to the employees list of shifts.
     *
     * @param sft = the shift to be added
     */
    public void addShift(Shift sft) {
        personalShifts.add(sft);
    }

    /**
     * Adds a time off request to the employees list of time off requests.
     *
     * @param t = the time off request to be added
     */
    public void addTimeOff(TimeOffRequest t) {
        tor.add(t);
    }
    
    /**
     * Clears the employees time off requests, replacing them with ones sent in in a list.
     *
     * @param t = the array list of time off requests to replace the current ones
     */
    public void setTimeOff(ArrayList<TimeOffRequest> t) {
    	tor.clear();
    	for(TimeOffRequest r:t) {
    		this.addTimeOff(r);
    	}
    }
    
    /**
     * Gets the inactive date.
     *
     * @return the time the employee started being inactive
     */
    public LocalDateTime getInactiveDate() {
    	return this.ldtInactive;
    }
    
    /**
     * Gets the id.
     *
     * @return the internal ID of the employee
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the type.
     *
     * @return the type of the employee, 0 = doctor, 1 = moonlighter, 2 = intern
     */
    public int getType() {
        return this.type;
    }

    /**
     * Gets the hours worked.
     *
     * @return the number of hours the employee has worked
     */
    public int getHoursWorked() {
        return this.hoursWorked;
    }

    /**
     * Gets the active.
     *
     * @return the active state of the employee, 0 = active, 1 = inactive, 2 = maternity
     */
    public int getActive() {
        return active;
    }

    /**
     * Gets the name.
     *
     * @return the employees name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the email.
     *
     * @return the employees email
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * Gets the shifts.
     *
     * @return the list of shifts that the employee has
     */
    public ArrayList<Shift> getShifts(){
    	return this.personalShifts;
    }
    
    /**
     * Gets the time off requests.
     *
     * @return the list of time off requests the employee has
     */
    public ArrayList<TimeOffRequest> getTimeOffRequests(){
    	return this.tor;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	String str=getId()+" "+getType()+" "+getHoursWorked()+" "+getName()+" "+getActive()+" "+personalShifts.size()+" "+tor.size();
    	if(ldtInactive!=null) {
    		str+=" "+ldtInactive.toString();	
    	}
    	return str;
    }
}
