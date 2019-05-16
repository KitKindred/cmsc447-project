package classinfo;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.*;
import java.time.LocalDate;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import sysfiles.IOFunctions;
import gui.guiStarter;
public class Main {

	private static HashMap <Integer,Profession> p;
	static int id=0;
    public static void main(String[] args) {

    	LocalDate d1 = LocalDate.of(2018, 7, 1);
        LocalDate d2 = LocalDate.of(2018, 10, 1);

        Schedule s = new Schedule(d1, d2);
        /*
        int n = 8;
        Model model = new Model(n + "-queens problem");
        IntVar[] vars = new IntVar[n];
        for(int q = 0; q < n; q++){
            vars[q] = model.intVar("Q_"+q, 1, n);
        }
        for(int i  = 0; i < n-1; i++){
            for(int j = i + 1; j < n; j++){
                model.arithm(vars[i], "!=",vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }*/
        ProgramDriver drive = new ProgramDriver();





    	
        //boolean saved=IOFunctions.saveAllEmployees(drive.getEmployees());
        /*HashMap<Integer,Profession> */
    	//p=
        try {
        	System.out.println("loadEmployees()");
			IOFunctions.loadEmployees();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	System.out.println("readAllEmployees()");
		//	IOFunctions.readAllEmployees();
		}

        drive.printEmployees();

        //s.createSchedule(drive.getActiveID(), drive.getEmployees());
        //s.printShifts();

        guiStarter.go(args);
	    
	StringBuilder builder = new StringBuilder(); // This creates the ics file after the gui window is closed.
        File file = new File("cal.ics");
        builder.append("cal");
        builder.append(".ics");

        String iCal = s.Export();

        if (!file.exists()) {
        file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(iCal);
        bw.close();
        
    }

    public static HashMap<Integer,Profession> getP()
    {return ProgramDriver.getEmployees();}
}
