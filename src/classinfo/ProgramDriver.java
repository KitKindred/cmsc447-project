package classinfo;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramDriver {
	private static HashMap<Integer, Profession> employees;
	private static HashMap<String, Integer> empID;
	private static int id;

	public ProgramDriver() {
		employees = new HashMap<Integer, Profession>();
		empID = new HashMap<String, Integer>();
		id = 0;
	}

	/**
	 * Add a doctor to the system
	 *
	 * @param t The type of employee
	 * @param n The name of the employee
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
		empID.put(n, id);
		id++;
		System.out.println("add "+id);
	}

	public static void addDoctor(int t, String n, int i) {
		switch (t) {
		case 0:
			employees.put(i, new Doctor(i, t, n));
			break;
		case 1:
			employees.put(id, new Moonlighter(i, t, n));
			break;
		case 2:
			employees.put(id, new Intern(i, t, n));
			break;
		default:
			throw new IllegalArgumentException();
		}
		empID.put(n, i);
		id=Math.max(i+=1, id+=1);
		System.out.println("add "+id);

	}

	/**
	 * Gets the IDs of all of the currently active employees
	 *
	 * @return A list of all active IDs
	 */
	public static ArrayList<Integer> getActiveID() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (Integer ident: employees.keySet()){
			if (employees.get(ident).getActive()) {
				ids.add(ident);
			}
		}
		return ids;
	}

	public static HashMap<Integer, Profession> getEmployees() {
		return employees;
	}
	public static HashMap<String, Integer> getNameID(){return empID;}

	public void printEmployees() {
		//System.out.println(otherShifts);
		for (Integer name: employees.keySet()){

			String key = name.toString();
			String value = employees.get(name).getName();
			System.out.println(key + ": " + value);
		}
	}
	public static int getID() {return id;}
}
