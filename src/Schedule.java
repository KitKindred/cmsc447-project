import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;

public class Schedule {
    private LocalDate start;
    private LocalDate end;
    private ArrayList<Shift> shifts;
    private HashMap<Integer, Shift> otherShifts; // Not sure if list or hash is better yet
    private int weekdayShifts, weekendShifts; // Number of shifts on weekdays and weekends

    /**
     * Constructor that takes only a start and end date.
     * Uses 3 shift weekdays and 2 shift weekends by default

     * @param s The first day to be scheduled
     * @param e The last day to be scheduled
     */
    Schedule(LocalDate s, LocalDate e) {
        this.start = s;
        this.end = e;
        shifts = new ArrayList<Shift>();
        otherShifts = new HashMap<Integer, Shift>();
        this.weekdayShifts = 3;
        this.weekendShifts = 2;
    }

    /**
     * Constructor for using different numbers of shifts
     *
     * @param s The first day to be scheduled
     * @param e The last day to be scheduled
     * @param ds The number of weekday shifts
     * @param ws The number of weekend shifts
     */
    Schedule(LocalDate s, LocalDate e, int ds, int ws) {
        this.start = s;
        this.end = e;
        shifts = new ArrayList<Shift>();
        otherShifts = new HashMap<Integer, Shift>();
        this.weekendShifts = ws;
        this.weekdayShifts = ds;
    }

    /**
     * Start date accessor
     *
     * @return The first day to be scheduled
     */
    public LocalDate getStart() {
        return this.start;
    }

    /**
     * End date accessor
     *
     * @return The last day to be scheduled
     */
    public LocalDate getEnd() {
        return this.end;
    }

    /**
     * Shift list accessor
     *
     * @return The list of shifts
     */
    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    /**
     * Method for testing. Prints out the shifts from the HashMap in no particular order
     */
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
        LocalDate d = this.start; //Beginning of schedule. Will be incremented until end date
        int shiftOffset = Math.max(weekdayShifts, weekendShifts); // Max number of shifts per day
        int weHours = 24/weekendShifts; // Number of hours per weekend shift
        int wdHours = 24/weekdayShifts; // Number of hours per weekday shift

        while (!(d.equals(this.end.plusDays(1)))) {
            int id = d.getDayOfYear()*shiftOffset;
            if(d.getDayOfWeek().equals(DayOfWeek.SATURDAY) || d.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                otherShifts.put(id, new Shift(d, weHours));
                id++;
                otherShifts.put(id, new Shift(d, weHours));
            }
            else {
                otherShifts.put(id, new Shift(d, wdHours));
                id++;
                otherShifts.put(id, new Shift(d, wdHours));
                id++;
                otherShifts.put(id, new Shift(d, wdHours));
            }
            d = d.plusDays(1);
        }
    }

    public void createSchedule(ArrayList<Integer> ids) {
        this.createShifts();
        //Integer[] arrIds = new Integer[ids.size()];
        //arrIds = ids.toArray(arrIds);
        int[] arrIds = ids.stream().mapToInt(i->i).toArray();
        Model model = new Model("Scheduler");
        IntVar[] vars = new IntVar[otherShifts.size()];
        int i = 0;
        for (Integer shift: otherShifts.keySet()) {
            vars[i] = model.intVar(shift.toString(), arrIds);
        }

    }
}
