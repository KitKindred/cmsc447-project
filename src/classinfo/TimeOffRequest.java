package classinfo;
// TODO: Auto-generated Javadoc

/**
 * The Class TimeOffRequest.
 */
public class TimeOffRequest {
    
    /** The s. */
    private Shift s;
    
    /** The priority. */
    private int priority;

    /**
     * Instantiates a new time off request.
     *
     * @param s the s
     * @param p the p
     */
    public TimeOffRequest(Shift s, int p) {
        this.s = s;
        this.priority = p;
    }

    /**
     * Gets the shift.
     *
     * @return the shift
     */
    public Shift getShift() {
        return this.s;
    }

    /**
     * Gets the priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return this.priority;
    }
    
    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
    	return this.s.getData().replace("T", " ")+";"+this.priority;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return this.s+"\tPriority: "+this.priority;
    	
    }
}
