import java.time.LocalDateTime;

public class Shift {
    private Profession employee;
    private int length;
    private LocalDateTime startTime;

    public Shift(LocalDateTime start, int length) {
        this.length = length;
        this.startTime = start;
    }

    public Shift(Shift sh) {
        this.length = sh.getLength();
        this.startTime = sh.getStartTime();

    }

    public void setLength(int l) {
        this.length = l;
    }

    public void setStartTime(LocalDateTime d) {
        this.startTime = d;
    }

    public void setEmployee(Profession e) {
        this.employee = e;
    }

    public int getLength() {
        return this.length;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public Profession getEmployee() {
        return this.employee;
    }

    @Override
    public String toString() {
        return startTime + ": " + length + " hours";
    }
}
