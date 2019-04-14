package classinfo;
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
            String nam = otherShifts.get(name).getEmployee().getName();
            System.out.println(key + ": " + value + ": " + nam);


        }
    }

    public void addToShifts(HashMap<Integer, IntVar> varMap, HashMap<Integer, Profession> docs) {
        for (Integer shift: varMap.keySet()) {
            Shift s = otherShifts.get(shift);
            s.setEmployee(docs.get(varMap.get(shift).getValue()));
            this.otherShifts.put(shift, s);
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
                otherShifts.put(id, new Shift(d.atTime(7,0), weHours));
                id++;
                otherShifts.put(id, new Shift(d.atTime(19, 0), weHours));
            }
            else {
                otherShifts.put(id, new Shift(d.atTime(7,0), wdHours));
                id++;
                otherShifts.put(id, new Shift(d.atTime(15, 0), wdHours));
                id++;
                otherShifts.put(id, new Shift(d.atTime(23,0), wdHours));
            }
            d = d.plusDays(1);
        }
    }

    public void createSchedule(ArrayList<Integer> ids, HashMap<Integer, Profession> docs) {
        this.createShifts();
        //Integer[] arrIds = new Integer[ids.size()];
        //arrIds = ids.toArray(arrIds);
        int[] arrIds = ids.stream().mapToInt(i->i).toArray(); // int array of active doctors
        Model model = new Model("Scheduler"); // Create solver
        //IntVar[] vars = new IntVar[otherShifts.size()];
        HashMap<Integer, IntVar> varMap = new HashMap<Integer, IntVar>(); // HashMap of IntVars for the solver
        //int i = 0;
        for (Integer shift: otherShifts.keySet()) {
            varMap.put(shift, model.intVar(shift.toString(), arrIds)); // Add an IntVar for every shift
        }
        // Makes sure that a doctor doesn't work a shift that starts less than 24 hours
        // after the start of their last shift. Because of the way shifts are created, the keys on each day are
        // consecutive. There are gaps on days with fewer schedules though. Ex: If there are three shifts on Friday, and
        // two on Saturday and Sunday, those seven shifts would have the values F(0, 1, 2), S(3, 4), Sun(6, 7). The
        // first shift on Monday would then be shift 9. Therefore, checking that a shift does not have the
        // same value as the following two shifts is sufficient to make sure that the doctor is not scheduled too soon,
        // but that a doctor will still be able to work Monday morning after working Sunday morning.
        //
        // This is currently hardcoded to work based on three weekday shifts, but could be upgraded later.
        for (Integer shift: varMap.keySet()) {
            if (varMap.containsKey(shift+1)) {
                model.arithm(varMap.get(shift), "!=",varMap.get(shift+1)).post();
            }
            else { // If this is true, then this is a weekend evening shift
                if (varMap.containsKey(shift+4)) { // This is a sunday evening shift. That means the first two Monday
                    // shifts can not be worked.
                    model.arithm(varMap.get(shift), "!=",varMap.get(shift+3)).post();
                }
            }
            if (varMap.containsKey(shift+2)) { // If this is not true, then this is a weekend morning shift.
                model.arithm(varMap.get(shift), "!=",varMap.get(shift+2)).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
            this.addToShifts(varMap, docs);
            //for (Integer val: varMap.keySet()) {
            //    System.out.println(varMap.get(val).getValue());
            //}
        }
        //this.printShifts();

    }
}
