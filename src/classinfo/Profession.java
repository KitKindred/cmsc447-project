package classinfo;
import java.util.ArrayList;

public class Profession {
    protected int id, type, hoursWorked;
    protected boolean active;
    protected String name;
    protected ArrayList<Shift> personalShifts;
    protected ArrayList<TimeOffRequest> tor;

    protected Profession() {
        this.id = 0;
        this.type = 0;
        this.name = "";
    }

    protected Profession(int id, int type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.active = true;
        this.hoursWorked = 0;
        personalShifts = new ArrayList<Shift>();
        tor = new ArrayList<TimeOffRequest>();
    }

    public void addHours(int numHours) {
        this.hoursWorked += numHours;
    }

    public void setHoursWorked(int numHours) {
        this.hoursWorked = numHours;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setType(int t) {
        this.type = t;
    }

    public void setActive(boolean a) {
        this.active = a;
    }

    public void addShift(Shift sft) {
        personalShifts.add(sft);
    }

    public void addTimeOff(TimeOffRequest t) {
        tor.add(t);
    }

    public int getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public int getHoursWorked() {
        return this.hoursWorked;
    }

    public boolean getActive() {
        return active;
    }

    public String getName() {
        return this.name;
    }
    
    public String toString() {
    	
    	return getId()+" "+getType()+" "+getHoursWorked()+" "+getName()+" "+getActive()+" "+personalShifts.size()+" "+tor.size();
    }
    
    public ArrayList<Shift> getShifts(){
    	return this.personalShifts;
    }
    public ArrayList<TimeOffRequest> getTimeOffRequests(){
    	return this.tor;
    }
}