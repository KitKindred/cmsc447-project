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

    // For testing.
    public void printShifts() {
        //System.out.println(otherShifts);
        for (Integer name: otherShifts.keySet()){

            String key = name.toString();
            String value = otherShifts.get(name).toString();
            System.out.println(key + ": " + value);


        }
    }

    /**
     Creates a HashMap of shifts that represent every shift in the date range

     HashMap<k, v> has the form <Unique int id, Shift> where the unique id is equal to the max number of shifts per day
     (so, for three shifts on weekdays and two on weekends, three) multiplied by the day of the year added to the number
     of the shift that day. For example, Jan 1st is day 1 of the year. Shift two on 1/1 would have an id of 1*3+1, or 4.
     */
    public void createShifts() {
        LocalDate d = this.start;

        while (!(d.equals(this.end.plusDays(1)))) {
            int id = d.getDayOfYear()*3;
            if(d.getDayOfWeek().equals(DayOfWeek.SATURDAY) || d.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                otherShifts.put(id, new Shift(d, 12));
                id++;
                otherShifts.put(id, new Shift(d, 12));
            }
            else {
                otherShifts.put(id, new Shift(d, 8));
                id++;
                otherShifts.put(id, new Shift(d, 8));
                id++;
                otherShifts.put(id, new Shift(d, 8));
            }
            d = d.plusDays(1);
        }
    }
}
