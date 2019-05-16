package classinfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String getData() {return startTime.toString().replace("T", " ")+","+length;}
    
    @Override
    public String toString() {
    	//return startTime.toString() + ": " + length + " hours";
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return startTime.format(format) + " until " + startTime.plusHours(length).format(format) + ".";
    }
}
