public class TimeOffRequest {
    private Shift s;
    private int priority;

    public TimeOffRequest(Shift s, int p) {
        this.s = s;
        this.priority = p;
    }

    Shift getShift() {
        return this.s;
    }

    int getPriority() {
        return this.priority;
    }
}
