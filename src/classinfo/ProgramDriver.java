package classinfo;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgramDriver.
 */
public class ProgramDriver {
	
	/** The employees. */
	private static HashMap<Integer, Profession> employees;
	
	/** The emp ID. */
	//private static HashMap<String, Integer> empID;
	private static ArrayList<Integer> empID;
	
	/** The id. */
	private static int id;

	/**
	 * Instantiates a new program driver.
	 */
	public ProgramDriver() {
		employees = new HashMap<Integer, Profession>();
		empID = new ArrayList<Integer>();//HashMap<String, Integer>();
		id = 0;
	}
	
	/**
	 * Reset.
	 */
	public static void reset() {
		
		empID.clear();
		employees.clear();
		id=0;
		
	}

	/**
	 * Add a doctor to the system.
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
		empID.add(id);//empID.put(n, id);
		id++;
		System.out.println("adding as "+ id);
	}

	/**
	 * Adds the doctor.
	 *
	 * @param type the type
	 * @param name the name
	 * @param i_d the i d
	 */
	public static void addDoctor(int type, String name, int i_d) {
		switch (type) {
		case 0:
			employees.put(i_d, new Doctor(i_d, type, name));
			break;
		case 1:
			employees.put(i_d, new Moonlighter(i_d, type, name));
			break;
		case 2:
			employees.put(i_d, new Intern(i_d, type, name));
			break;
		default:
			throw new IllegalArgumentException();
		}
		empID.add(i_d);
//		empID.put(name, i_d);
		id=Math.max(i_d+=1, id+=1);
		System.out.println("adding "+ id);

	}

	/**
	 * Adds the doctor.
	 *
	 * @param type the type
	 * @param name the name
	 * @param i_d the i d
	 * @param em the em
	 */
	public static void addDoctor(int type, String name, int i_d, String em) {
		switch (type) {
		case 0:
			Doctor doc=new Doctor(i_d,type,name);
			doc.setEmail(em);
			employees.put(i_d, doc);
			break;
		case 1:
			Moonlighter moon=new Moonlighter(i_d,type,name);
			moon.setEmail(em);
			employees.put(i_d, moon);
			break;
		case 2:
			Intern in=new Intern(i_d, type, name);
			in.setEmail(em);
			employees.put(i_d, in);
			break;
		default:
			throw new IllegalArgumentException();
		}
		empID.add(i_d);
		//empID.put(name, i_d);
		id=Math.max(i_d+=1, id+=1);
		System.out.println("adding "+ id);

	}
	
	/**
	 * Gets the IDs of all of the currently active employees.
	 *
	 * @return A list of all active IDs
	 */
	public static ArrayList<Integer> getActiveID() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (Integer ident: employees.keySet()){
			if (employees.get(ident).getActive()==0) {
				ids.add(ident);
			}
		}
		return ids;
	}

	/**
	 * Gets the employees.
	 *
	 * @return the employees
	 */
	public static HashMap<Integer, Profession> getEmployees() {
		return employees;
	}
	
	/**
	 * Gets the name ID.
	 *
	 * @return the name ID
	 */
	public static ArrayList<Integer> getNameID(){return empID;}

	/**
	 * Prints the employees.
	 */
	public void printEmployees() {
		
		for (Integer name: employees.keySet()){

			String key = name.toString();
			String value = employees.get(name).getName();
			System.out.println(key + ": " + value);
		}
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public static int getID() {return id;}
}
