package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OldMain extends Application {
	  @Override
	    public void start(Stage primaryStage) {
	        try {
	        	
	        	String path="/gui/sample.fxml";
	            // Read file fxml and draw interface.
	            Parent root = FXMLLoader.load(getClass().getResource(path));
	 
	            primaryStage.setTitle("My Application");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.show();
	         
	        } catch(Exception e) {
	            e.printStackTrace();
	     }
	 }
	    
	public static void main(String[] args) {
		launch(args);
	}
}
