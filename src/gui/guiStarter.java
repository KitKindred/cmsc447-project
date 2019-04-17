package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sysfiles.IOFunctions;
import classinfo.*;

public class guiStarter extends Application {
	  @Override
	    public void start(Stage primaryStage) {
	        try {
	        	
	        	String path="/gui/sample.fxml";
	            // Read file fxml and draw interface.
	            Parent root = FXMLLoader.load(getClass().getResource(path));
	 
	            primaryStage.setTitle("Schedule Builder");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.show();
	         
	        } catch(Exception e) {
	            e.printStackTrace();
	     }
	 }
	  
	 @Override
	 public void stop() {
		 IOFunctions.saveAllEmployees(ProgramDriver.getEmployees());	 
	 }
	 
	 public static void go(String[] args) {
		 launch(args);
	 }

}
