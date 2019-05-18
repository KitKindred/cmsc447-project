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

// TODO: Auto-generated Javadoc
/**
 * The Class guiStarter.
 */
public class guiStarter extends Application {
	  
  	/* (non-Javadoc)
  	 * @see javafx.application.Application#start(javafx.stage.Stage)
  	 */
  	@Override
	    public void start(Stage primaryStage) {
	        try {
	        	
	        	String path="/gui/scheduler.fxml";
	            // Read file fxml and draw interface.
	            Parent root = FXMLLoader.load(getClass().getResource(path));
	            primaryStage.setResizable(false);
	            primaryStage.setTitle("Schedule Builder");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.show();
	         
	        } catch(Exception e) {
	            e.printStackTrace();
	     }
	 }
	  
	 /* (non-Javadoc)
 	 * @see javafx.application.Application#stop()
 	 */
 	@Override
	 public void stop() {
		 if(controller.getEditWithoutSave()){
			 if(controller.changeBox()) {
				 try {
					IOFunctions.saveEmployees();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
		 }
		 System.out.println("stopping");
	 }
	 
	 /**
 	 * Go.
 	 *
 	 * @param args the args
 	 */
 	public static void go(String[] args) {
		 launch(args);
	 }

}