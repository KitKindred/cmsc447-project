import java.util.ArrayList;
import java.util.HashMap;

public class ProgramDriver {
    private HashMap<Integer, Profession> employees;
    private int id;

    public ProgramDriver() {
        employees = new HashMap<Integer, Profession>();
        id = 0;
    }

    /**
     * Add a doctor to the system
     *
     * @param t The type of employee
     * @param n The name of the employe
     */
    public void addDoctor(int t, String n) {
        switch (t) {
            case 0:
                employees.put(id, new Doctor(id, t, n));
                break;
            case 1:
                employees.put(id, new Moonlighter(id, t, n));
                break;
            case 2:
                employees.put(id, new Intern(id, t, n));
                break;
            default:
                throw new IllegalArgumentException();
        }
        id++;

    }

    /**
     * Gets the IDs of all of the currently active employees
     *
     * @return A list of all active IDs
     */
    public ArrayList<Integer> getActiveID() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Integer ident: employees.keySet()){
            if (employees.get(ident).getActive()) {
                ids.add(ident);
            }
        }
        return ids;
    }

    public void printEmployees() {
        //System.out.println(otherShifts);
        for (Integer name: employees.keySet()){

            String key = name.toString();
            String value = employees.get(name).getName();
            System.out.println(key + ": " + value);


        }
    }
}
