import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
    private LocalDate start;
    private LocalDate end;
    private ArrayList<Shift> shifts;
    private HashMap<Integer, Shift> otherShifts; // Not sure if list or hash is better yet

    Schedule(LocalDate s, LocalDate e) {
        this.start = s;
        this.end = e;
        shifts = new ArrayList<Shift>();
        otherShifts = new HashMap<Integer, Shift>();
    }

    public LocalDate getStart() {
        return this.start;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    public void printShifts() {
        System.out.println(otherShifts);
    }

    /**
     Creates a HashMap of shifts that represent every shift in the date range
     Currently does not work around year-wrap

     HashMap<k, v> has the form <Unique int id, Shift>
     */
    public void createShifts() {
        int dayOfYear = this.start.getDayOfYear();
        int year = this.start.getYear();

        LocalDate d = LocalDate.ofYearDay(year, dayOfYear);

        while (dayOfYear != this.end.getDayOfYear()) {

            if(d.getDayOfWeek().equals(DayOfWeek.SATURDAY) || d.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                int id = d.getDayOfYear()*3;
                otherShifts.put(id, new Shift(d, 12));
                id++;
                otherShifts.put(id, new Shift(d, 12));
            }
            else {
                int id = d.getDayOfYear()*3;
                otherShifts.put(id, new Shift(d, 8));
                id++;
                otherShifts.put(id, new Shift(d, 8));
                id++;
                otherShifts.put(id, new Shift(d, 8));
            }
            dayOfYear++;
            d = LocalDate.ofYearDay(year, dayOfYear);
        }
    }
}
