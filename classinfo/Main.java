package classinfo;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import sysfiles.IOFunctions;
import gui.guiStarter;
public class Main {

    public static void main(String[] args) {
        /*
    	LocalDate d = LocalDate.of(2018, 11, 2);
        LocalDate d2 = LocalDate.of(2018, 11, 9);

        Schedule s = new Schedule(d, d2);

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
        }
        ProgramDriver drive = new ProgramDriver();
        drive.addDoctor(0, "John Smith");
        drive.addDoctor(0, "Jane Doe");
        drive.addDoctor(0, "John Doe");

        drive.printEmployees();

        s.createSchedule(drive.getActiveID(), drive.getEmployees());
        s.printShifts();

        */
    	ProgramDriver d = new ProgramDriver();
    	
        //boolean saved=IOFunctions.saveAllEmployees(drive.getEmployees());
        HashMap<Integer,Profession> p=IOFunctions.readAllEmployees();
        
        //System.out.println(saved+p.toString());
        //guiStarter g=new guiStarter();
        guiStarter.go(args);
        
        
        
    }


}
