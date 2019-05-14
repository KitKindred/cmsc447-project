package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
			// System.out.println("saveEmployees() test");
			 boolean choice=false;
			 System.out.println("saved? "+controller.getEditWithoutSave());
			 if(controller.getEditWithoutSave()) {
				 System.out.println("would you like to save");
				 
				 try {
						String path="/gui/yesno.fxml";
						Parent root = FXMLLoader.load(getClass().getResource(path));
						Stage st = new Stage();
						Scene scene = new Scene(root);
						st.setScene(scene);
						st.initModality(Modality.APPLICATION_MODAL);
						st.setTitle("Unsaved Changes!");
						
						st.showAndWait();
						
						choice=yesno.getClose();
						
					}catch(Exception e) {System.out.println("error?"+e.toString());}
				 
				 if(choice) {
					 IOFunctions.saveEmployees();		 
					 
				 }
				 else {
					 System.out.println("choice: not saving");
				 }
				 
			 }			
		} catch (IOException e) {
			System.out.println("Error saving employees: " + e.toString());
		}
		 	 
	 }
	 
	 public static void go(String[] args) {
		 launch(args);
	 }

}
