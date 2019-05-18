package classinfo;

import java.io.IOException;
import java.io.*;
import java.time.LocalDate;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import sysfiles.IOFunctions;
import gui.guiStarter;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        
        // the main driver of most of the non-gui sections of the employees
        ProgramDriver drive = new ProgramDriver();

        // attempt to load the employees
        try {
        	System.out.println("loadEmployees()");
			IOFunctions.loadEmployees();
			drive.printEmployees();
		} catch (IOException e) {
			System.out.println("Error loading employees!");
		}
        

        
        // start the main gui of the program
        guiStarter.go(args);   
    }
}
