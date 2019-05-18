package classinfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Shift.
 */
public class Shift {
    
    /** The employee. */
    private Profession employee;
    
    /** The length. */
    private int length;
    
    /** The start time. */
    private LocalDateTime startTime;

    /**
     * Instantiates a new shift.
     *
     * @param start the start
     * @param length the length
     */
    public Shift(LocalDateTime start, int length) {
        this.length = length;
        this.startTime = start;
    }

    /**
     * Instantiates a new shift.
     *
     * @param sh the sh
     */
    public Shift(Shift sh) {
        this.length = sh.getLength();
        this.startTime = sh.getStartTime();

    }

    /**
     * Sets the length.
     *
     * @param l the new length
     */
    public void setLength(int l) {
        this.length = l;
    }

    /**
     * Sets the start time.
     *
     * @param d the new start time
     */
    public void setStartTime(LocalDateTime d) {
        this.startTime = d;
    }

    /**
     * Sets the employee.
     *
     * @param e the new employee
     */
    public void setEmployee(Profession e) {
        this.employee = e;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * Gets the employee.
     *
     * @return the employee
     */
    public Profession getEmployee() {
        return this.employee;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
    	return startTime.toString().replace("T", " ")+","+length;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return startTime.format(format) + " until " + startTime.plusHours(length).format(format) + ".";
    }
}
