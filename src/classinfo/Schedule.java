package classinfo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;

public class Schedule {
    private LocalDate start;
    private LocalDate end;
    //private ArrayList<Shift> shifts;
    private HashMap<Integer, Shift> otherShifts; // Not sure if list or hash is better yet
    private int weekdayShifts, weekendShifts; // Number of shifts on weekdays and weekends
    private boolean isValidSchedule = false;
    

    /**
     * Constructor that takes only a start and end date.
     * Uses 3 shift weekdays and 2 shift weekends by default

     * @param s The first day to be scheduled
     * @param e The last day to be scheduled
     */
    public Schedule(LocalDate s, LocalDate e) {
        this.start = s;
        this.end = e;
        //shifts = new ArrayList<Shift>();
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
        //shifts = new ArrayList<Shift>();
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

    /*
     * Shift list accessor
     *
     * @return The list of shifts
     */
    //public ArrayList<Shift> getShifts() {
    //    return shifts;
    //}

    /**
     * Method for testing. Prints out the shifts from the HashMap in sorted order.
     */
    public void printShifts() {

    	if (isValidSchedule) {
    		Object[] keys = otherShifts.keySet().toArray();
    		Arrays.sort(keys);
    	
        	for (Object name: keys){

            	String key = name.toString();

            	String value = otherShifts.get(name).toString();
            	String nam = otherShifts.get(name).getEmployee().getName();
            
            	System.out.println(key + ": " + value + ": " + nam);

        	}
    	}
    	else {
    		System.out.println("Could not print schedule!");
    	}
    }

    public void addToShifts(HashMap<Integer, IntVar> varMap, HashMap<Integer, IntVar> varMapWeekend, HashMap<Integer, Profession> docs) {
        for (Integer shift: varMap.keySet()) {
            Shift s = otherShifts.get(shift);
            s.setEmployee(docs.get(varMap.get(shift).getValue()));
            this.otherShifts.put(shift, s);
        }
        for (Integer shift: varMapWeekend.keySet()) {
            Shift s = otherShifts.get(shift);
            s.setEmployee(docs.get(varMapWeekend.get(shift).getValue()));
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

    /**
     Uses the list of active doctors to create a valid schedule, then adds the correct ids to the HashMap of shifts.

     @param ids An ArrayList of the ids of active doctors
     @param docs The HashMap containing all of the doctor information
     */
    public void createSchedule(ArrayList<Integer> ids, HashMap<Integer, Profession> docs) {
        this.createShifts();

        int[] arrIds = ids.stream().mapToInt(i->i).toArray(); // int array of active doctors
        if (arrIds.length == 0) {
        	System.out.println("No active doctors! Cannot generate schedule.");
        	isValidSchedule = false;
        	return;
        }
        Model model = new Model("Scheduler"); // Create solver
        //IntVar[] vars = new IntVar[otherShifts.size()];
        HashMap<Integer, IntVar> varMap = new HashMap<Integer, IntVar>(); // HashMap of IntVars for the solver
        HashMap<Integer, IntVar> varMapWeekend = new HashMap<Integer, IntVar>();
        //int i = 0;
        for (Integer shift: otherShifts.keySet()) {
            if (otherShifts.get(shift).getStartTime().getDayOfWeek().equals(DayOfWeek.SATURDAY) || otherShifts.get(shift).getStartTime().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                varMapWeekend.put(shift, model.intVar(shift.toString(), arrIds)); // Add an IntVar for every weekend shift
            }
            else {
                varMap.put(shift, model.intVar(shift.toString(), arrIds)); // Add an IntVar for every weekday shift
            }
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
        for (Integer shift: otherShifts.keySet()) {
            /*if (varMap.containsKey(shift+1)) {
                model.arithm(varMap.get(shift), "!=",varMap.get(shift+1)).post();
            }
            else if (varMapWeekend.containsKey(shift+1)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+1)).post();
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
            else if (varMapWeekend.containsKey(shift+2)) {
                model.arithm(varMap.get(shift), "!=",varMap.get(shift+2)).post();
            }*/
            if (varMap.containsKey(shift)) { // This is a weekday shift
                if (varMap.containsKey(shift+1)) { // Normal weekday shift
                    model.arithm(varMap.get(shift), "!=",varMap.get(shift+1)).post();
                }
                else if (varMapWeekend.containsKey(shift+1)) { // Last Friday shift
                    model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+1)).post();
                }
                if (varMap.containsKey(shift+2)) { // Normal weekday shift
                    model.arithm(varMap.get(shift), "!=",varMap.get(shift+2)).post();
                }
                else if (varMapWeekend.containsKey(shift+2)) { // Second to last Friday shift
                    model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+2)).post();
                }
            }
            else { // This is a weekend shift
                if (varMapWeekend.containsKey(shift+1)) { // Weekend morning, can't work same day evening
                    model.arithm(varMapWeekend.get(shift), "!=",varMapWeekend.get(shift+1)).post();
                }
                else if (varMapWeekend.containsKey(shift+2)) { // Saturday evening
                    model.arithm(varMapWeekend.get(shift), "!=",varMapWeekend.get(shift+2)).post();
                }
                else if (varMap.containsKey(shift+2)) { // Sunday evening
                    model.arithm(varMapWeekend.get(shift), "!=",varMap.get(shift+2)).post();
                    if (varMap.containsKey(shift+3)) {
                        model.arithm(varMapWeekend.get(shift), "!=",varMap.get(shift+3)).post();
                    }
                }

            }
        }


        // Convert the shifts from a hashmap of IntVars to an array of IntVars
        IntVar[] weekVars = new IntVar[varMap.size()];
        IntVar[] weekendVars = new IntVar[varMapWeekend.size()];

        Object[] keys = varMap.keySet().toArray();  // Create an array out of the keys of the hash table
        Arrays.sort(keys);  // Sort the keys

        int k = 0;  // Counter for the array index
        for (Object name: keys){
            weekVars[k] = varMap.get(name);  // Add every IntVar from weekday hashmap to the weekday array
            k++;
        }

        Object[] keys2 = varMapWeekend.keySet().toArray();  // Create an array out of the keys of the hash table
        Arrays.sort(keys2);  // Sort the keys

        k = 0;  // Counter for the array index
        for (Object name: keys2){
            weekendVars[k] = varMapWeekend.get(name);  // Add every IntVar from weekend hashmap to the weekend array
            k++;
        }

        // Make sure that no doctor works more than 40 or fewer than 25 weekday shifts in the period, and no more than 12/less than 7 weekend shifts
        // globalCardinality args:  The collection of IntVars where the constraint is enforced
        //                          The value whose cardinality is being set (in this case the doctor id)
        //                          The range of number of times a value can appear
        // If id = 0 (doctor 0), globalCardinality(weekVars, ...id..., ...25, 40..., false) means that doctor 0 must be
        // assigned at least 25 and at most 40 weekday shifts in the period.
        //
        // The for loop assigns the weekday/weekend shift count constraints to all doctors
        for (int id: arrIds) {
            model.globalCardinality(weekVars, new int[]{id}, new IntVar[]{model.intVar(id + "_Week_Shifts", 25, 40)}, false).post();
            model.globalCardinality(weekendVars, new int[]{id}, new IntVar[]{model.intVar(id + "_Weekend_Shifts", 7, 12)}, false).post();
        }

        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
            this.addToShifts(varMap, varMapWeekend, docs);
            isValidSchedule = true;
            //for (Integer val: varMap.keySet()) {
            //    System.out.println(varMap.get(val).getValue());
            //}
        }
        else {
        	System.out.println("Could not generate schedule!");
        	isValidSchedule = false;
        }
    }
}