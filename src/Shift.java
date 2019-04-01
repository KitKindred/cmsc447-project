import java.time.LocalDate;

public class Shift {
    private Profession employee;
    private int length;
    private LocalDate startTime;

    public Shift(LocalDate start, int length) {
        this.length = length;
        this.startTime = start;
    }

    public void setLength(int l) {
        this.length = l;
    }

    public void setStartTime(LocalDate d) {
        this.startTime = d;
    }

    public void setEmployee(Profession e) {
        this.employee = e;
    }

    public int getLength() {
        return this.length;
    }

    public LocalDate getStartTime() {
        return this.startTime;
    }

    public Profession getEmployee() {
        return this.employee;
    }
}
