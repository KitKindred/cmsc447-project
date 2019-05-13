package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sysfiles.IOFunctions;

import java.io.IOException;

import classinfo.*;

public class guiStarter extends Application {
	  @Override
	    public void start(Stage primaryStage) {
	        try {
	        	
	        	String path="/gui/scheduler.fxml";
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
		 try {
			 System.out.println("saveEmployees()");
			IOFunctions.saveEmployees();
		} catch (IOException e) {
			System.out.println("Error saving employees: " + e.toString());
		}
		 	 
	 }
	 
	 public static void go(String[] args) {
		 launch(args);
	 }

}
