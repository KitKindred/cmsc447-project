package classinfo;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import sysfiles.IOFunctions;
import gui.guiStarter;
public class Main {

	private static HashMap <Integer,Profession> p;
	
    public static void main(String[] args) {

    	LocalDate d1 = LocalDate.of(2018, 11, 2);
        LocalDate d2 = LocalDate.of(2018, 11, 9);

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
    	IOFunctions.readAllEmployees();

        drive.printEmployees();

        s.createSchedule(drive.getActiveID(), drive.getEmployees());
        s.printShifts();

        //System.out.println(saved+p.toString());
        //guiStarter g=new guiStarter();
        guiStarter.go(args);


        
    }

    public static HashMap<Integer,Profession> getP()
    {return ProgramDriver.getEmployees();}
}
