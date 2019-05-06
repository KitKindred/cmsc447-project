package classinfo;
public class TimeOffRequest {
    private Shift s;
    private int priority;

    public TimeOffRequest(Shift s, int p) {
        this.s = s;
        this.priority = p;
    }

    public Shift getShift() {
        return this.s;
    }

    public int getPriority() {
        return this.priority;
    }
    
    public String getData() {
    	return this.s.getData().replace("T", " ")+";"+this.priority;
    }
    public String toString() {
    	//System.out.println("TEST");
    	return this.s+"\t"+this.priority;
    	
    }
}
