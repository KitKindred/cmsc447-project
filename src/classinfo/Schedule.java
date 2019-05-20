package classinfo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;

import javax.print.Doc;

// TODO: Auto-generated Javadoc
/**
 * The Class Schedule.
 */
public class Schedule {

    /** The start. */
    private LocalDate start;

    /** The end. */
    private LocalDate end;

    /** The other shifts. */
    //private ArrayList<Shift> shifts;
    private HashMap<Integer, Shift> otherShifts; // Not sure if list or hash is better yet

    /** The weekend shifts. */
    private int weekdayShifts, weekendShifts; // Number of shifts on weekdays and weekends

    /** The is valid schedule. */
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
     * Constructor for using different numbers of shifts.
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
     * Start date accessor.
     *
     * @return The first day to be scheduled
     */
    public LocalDate getStart() {
        return this.start;
    }

    /**
     * End date accessor.
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

    /**
     * Adds the to shifts.
     *
     * @param varMap the var map
     * @param varMapWeekend the var map weekend
     * @param docs the docs
     */
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
     * Creates a list of shifts from a time off request
     *
     * @param tor the time off request to be converted
     * */
    private ArrayList<Integer> torToShifts(TimeOffRequest tor) {
        System.out.println("torToShifts");
        ArrayList<Integer> shiftList = new ArrayList<Integer>();

        LocalDateTime start = tor.getShift().getStartTime();

        int index = start.getDayOfYear()*3;

        for (int i = 0; i < tor.getShift().getLength(); i++) {
            shiftList.add(index);
            index++;
            shiftList.add(index);
            index++;
            shiftList.add(index);
            index++;
        }

        return shiftList;
    }

    /**
     * Converts tors from being a list of dates connected to a doctor to being a hashmap indexed by shift with
     * lists of doctors that cannot work that shift
     *
     * @param docs Hashmap of doctors for tor source
     * */
    private HashMap<Integer, ArrayList<Integer>> offShifts(HashMap<Integer, Profession> docs) {
        System.out.println("Off Shifts");
        HashMap<Integer, ArrayList<Integer>> timeOffByShift = new HashMap<Integer, ArrayList<Integer>>();

        for (Integer empl: docs.keySet()) { // Loop through all doctors
            Profession employee = docs.get(empl);
            ArrayList<TimeOffRequest> tor = employee.getTimeOffRequests();
            for (int i = 0; i < tor.size(); i++) { // Loop through every time off request
                ArrayList<Integer> dates = torToShifts(tor.get(i)); // A list of shifts included in the tor
                System.out.println("Shifts" + dates);
                for (int j = 0; j < dates.size(); j++) { // Loop through the shifts that are busy
                    if (timeOffByShift.containsKey(dates.get(j))) { // Another doctor already started this list

                        // Get list, modify list, push list. Java is silly
                        ArrayList<Integer> entry = timeOffByShift.get(dates.get(j));
                        System.out.println(entry);
                        entry.add(employee.getId());
                        timeOffByShift.put(dates.get(j), entry);
                    }
                    else { // First tor on this shift

                        // New list, add to list, push list
                        ArrayList<Integer> entry = new ArrayList<Integer>();
                        entry.add(employee.getId());
                        timeOffByShift.put(dates.get(j), entry);
                    }
                }
            }
        }
            //else if (docs.get(empl) instanceof Doctor) {
            //    Doctor employee = (Doctor) docs.get(empl);
            //}

        return timeOffByShift;
    }

    /**
     * Puts the start of every doctor's attending week in the <shift, doc> form used elsewhere
     *
     * @param docs Hashmap of doctors
     * */
    HashMap<Integer, Integer> attends(HashMap<Integer, Profession> docs) {
        HashMap<Integer, Integer> dates = new HashMap<Integer, Integer>();
        for (Integer id: docs.keySet()) {
            Profession employee = docs.get(id);
            if (employee instanceof Doctor) {
                System.out.println("IS DOC");
                if (((Doctor) employee).getAttendingDate() != null) {
                    System.out.println("HAS ATTENDING");
                    LocalDateTime start = ((Doctor) employee).getAttendingDate();
                    int shift = start.getDayOfYear()*3;
                    dates.put(shift, employee.getId());
                }
            }
        }

        return dates;
    }

    /**
     Uses the list of active doctors to create a valid schedule, then adds the correct ids to the HashMap of shifts.

     @param ids An ArrayList of the ids of active doctors
     @param docs The HashMap containing all of the doctor information
     */
    public void createSchedule(ArrayList<Integer> ids, HashMap<Integer, Profession> docs) {
        this.createShifts();

        ArrayList<Integer> mnls = new ArrayList<Integer>();
        ArrayList<Integer> fte = new ArrayList<Integer>();

        HashMap<Integer, ArrayList<Integer>> timeOffByShift = this.offShifts(docs);
        HashMap<Integer, Integer> attendingWeeks = this.attends(docs);

        for (int z = 0; z < ids.size(); z++) {
            Integer i = ids.get(z);
            if (docs.get(i) instanceof Moonlighter) {
                mnls.add(i);
            }
            else if (docs.get(i) instanceof Doctor) {
                fte.add(i);
            }
        }

        //int[] arrIds = ids.stream().mapToInt(i->i).toArray(); // int array of active doctors



        if (ids.size() == 0) {
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
            ArrayList<Integer> tempIds = new ArrayList<Integer>(ids);
            if (timeOffByShift.containsKey(shift)) {
                ArrayList<Integer> torlist = timeOffByShift.get(shift);
                // System.out.println("TorList" + torlist);
                for (int i = 0; i < torlist.size(); i++) {
                    tempIds.remove(torlist.get(i));
                }
            }

            int[] tempArrIds = tempIds.stream().mapToInt(i->i).toArray();

            //System.out.println("tempIds" + tempIds);
            //System.out.println("All ids" + ids);

            if (otherShifts.get(shift).getStartTime().getDayOfWeek().equals(DayOfWeek.SATURDAY) || otherShifts.get(shift).getStartTime().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {

                IntVar v = model.intVar(shift.toString(), tempArrIds);

                varMapWeekend.put(shift, v); // Add an IntVar for every weekend shift
            }
            else {
                IntVar v = model.intVar(shift.toString(), tempArrIds);
                varMap.put(shift, v); // Add an IntVar for every weekday shift
            }
        }

        for (Integer shift: attendingWeeks.keySet()) {
            //IntVar v = model.intVar(shift.toString(), new int[] {attendingWeeks.get(shift)});
            //varMap.put(shift, v);
            System.out.println("Shift: " + shift + " Doc: " + attendingWeeks.get(shift));
            model.arithm(varMap.get(shift), "=", attendingWeeks.get(shift)).post(); // Monday
            if (varMap.containsKey(shift+3)) {
                System.out.println("Shift: " + shift+3 + " Doc: " + attendingWeeks.get(shift));
                model.arithm(varMap.get(shift+3), "=",attendingWeeks.get(shift)).post(); // Tuesday
            }
            if (varMap.containsKey(shift+6)) {
                model.arithm(varMap.get(shift+6), "=",attendingWeeks.get(shift)).post(); // Wednesday
            }
            if (varMap.containsKey(shift+9)) {
                model.arithm(varMap.get(shift+9), "=",attendingWeeks.get(shift)).post(); // Thursday
            }
            if (varMap.containsKey(shift+12)) {
                model.arithm(varMap.get(shift+12), "=",attendingWeeks.get(shift)).post(); // Friday
            }

            if (varMapWeekend.containsKey(shift+15)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+15)).post(); // Sat morn
            }
            if (varMapWeekend.containsKey(shift+16)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+16)).post(); // Sat even
            }
            if (varMapWeekend.containsKey(shift+18)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+18)).post(); // Sun morn
            }
            if (varMapWeekend.containsKey(shift+19)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift+19)).post(); // Sun even
            }
            if (varMapWeekend.containsKey(shift-2)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift-2)).post(); // Prev Sun even
            }
            if (varMapWeekend.containsKey(shift-3)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift-3)).post(); // Prev Sun morn
            }
            if (varMapWeekend.containsKey(shift-5)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift-5)).post(); // Prev Sat even
            }
            if (varMapWeekend.containsKey(shift-6)) {
                model.arithm(varMap.get(shift), "!=",varMapWeekend.get(shift-6)).post(); // Prev Sat morn
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
        for (int id: fte) {
            model.globalCardinality(weekVars, new int[]{id}, new IntVar[]{model.intVar(id + "_Week_Shifts", 25, 40)}, false).post();
            model.globalCardinality(weekendVars, new int[]{id}, new IntVar[]{model.intVar(id + "_Weekend_Shifts", 7, 12)}, false).post();
        }

        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
            this.addToShifts(varMap, varMapWeekend, docs);
            isValidSchedule = true;
            //this.printShifts();
            //for (Integer val: varMap.keySet()) {
            //    System.out.println(varMap.get(val).getValue());
            //}
        }
        else {
        	System.out.println("Could not generate schedule!");
        	isValidSchedule = false;
        }
    }


    /**
     * Export.
     *
     * @return the string
     */
    public String export() {
    	
    	if (!isValidSchedule) {
        	return null;
        }
    	
    	String Cal =
                "BEGIN:VCALENDAR\r\n"+
                        "VERSION:2.0\r\n";
        String event = "";
        Object[] keys = otherShifts.keySet().toArray();
        Arrays.sort(keys);

        for (Object name: keys) {
        	LocalDateTime tempStart = otherShifts.get(name).getStartTime();
        	LocalDateTime tempEnd = tempStart.plusHours(otherShifts.get(name).getLength());
            String yearStart  = String.format("%04d", tempStart.getYear());
            String monthStart = String.format("%02d", tempStart.getMonth().getValue());
            String dayStart   = String.format("%02d", tempStart.getDayOfMonth());
            String hourStart  = String.format("%02d", tempStart.getHour()); //Start time for the employee
            String minStart   = String.format("%02d", tempStart.getMinute());
            String secStart   = String.format("%02d", tempStart.getSecond());

            String yearEnd  = String.format("%04d", tempEnd.getYear());
            String monthEnd = String.format("%02d", tempEnd.getMonth().getValue());
            String dayEnd   = String.format("%02d", tempEnd.getDayOfMonth());
            String hourEnd  = String.format("%02d", tempEnd.getHour()); //End time for the employee
            String minEnd   = String.format("%02d", tempEnd.getMinute());
            String secEnd   = String.format("%02d", tempEnd.getSecond());
            //end time for the employee

            String start = yearStart+monthStart+dayStart+"T"+hourStart+minStart+secStart;
            String end = yearEnd+monthEnd+dayEnd+"T"+hourEnd+minEnd+secEnd;
            String employee = "";
            try {
            	employee = otherShifts.get(name).getEmployee().getName().replace("\\", "\\\\").replace(",", "\\,");
            } catch (NullPointerException e) {
            	employee = "noName";
            }
            
            event=event +
                    "BEGIN:VEVENT\r\n" +
                    "DTSTART:" + start + "\r\n"+
                    "DTEND:"+ end + "\r\n" +
                    "LOCATION:4940 Eastern Ave\\, Baltimore\\, MD 21224\r\n" +
                    "DESCRIPTION:Work\r\n" +
                    "SUMMARY:"+employee+" Shift\r\n" +
                    "PRIORITY:3\r\n" +
                    "END:VEVENT\r\n";

        }

        String end = "END:VCALENDAR\r\n";
        String ical = Cal+event+end;
        return ical;
    }
}
