package gui;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import sysfiles.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import classinfo.*;
import classinfo.Schedule;

public class AddEmployeeWindow {
	@FXML
	Button addButton,cancelButton;
	@FXML
	ComboBox activeBox, professionBox;
	@FXML
	TextField nameBox,emailBox;

	private static Profession emp=null;
	private static boolean returnEmployee=false;//=true;
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		//returnEmployee=true;
		activeBox.getItems().addAll("Active","Inactive","Maternity");
		professionBox.getItems().addAll("Doctor","Moonlighter","Intern");

		activeBox.setValue(activeBox.getItems().get(0));
		professionBox.setValue(professionBox.getItems().get(0));

		emailBox.setText("");
		
	}

public static boolean getClose() {return returnEmployee;}
	public void actionAddEmployee(ActionEvent event) {
		returnEmployee=true;
		quit(event);

		//actionQuit(event);
	}
	public void actionEnterName(ActionEvent event) {}
	public void actionSelectProfession(ActionEvent event) {}
	public void actionSelectActivity(ActionEvent event) {}
	public void actionQuit(ActionEvent event) {
		//Stage st=(Stage)cancelButton.getScene().getWindow();
		quit(event);
		//st.close();


	}
	public void actionCancel(ActionEvent event) {
		returnEmployee=false;
		quit(event);
	}
	public void quit(ActionEvent event) {
		Stage st=(Stage)cancelButton.getScene().getWindow();
		if(returnEmployee==false) {System.out.println("closing empwindow without saving");st.close();return;}
		else {
		String professionType=professionBox.getValue().toString();
		String name=nameBox.getText().replace("~", "");
		System.out.println(emailBox);
		String email=emailBox.getText().replace("~", "");
		
		if(!email.contains("@")) {
			System.out.println("error: email must contain '@'.");
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Email Error");
			alert.setHeaderText("Email Formatting Error");
			alert.setContentText("Email's must have the @ symbol!");

			alert.showAndWait();
			
			returnEmployee=false;
			st.close();
			return;
		}
		
		int type=0;
		
		switch(professionType) {
		case "Doctor":
			type=0;
			break;
		case "Moonlighter":
			type=1;
			break;
		case "Intern":
			type=2;
			break;
		default:
			System.out.println("ERROR UNSELECTED PROFESSION");
			break;
		
		}
		
		ProgramDriver.addDoctor(type, name,ProgramDriver.getID(),email);
		int sel=activeBox.getSelectionModel().getSelectedIndex();
		System.out.println("selected active window: "+sel);
		ProgramDriver.getEmployees().get(ProgramDriver.getEmployees().size()-1).setActive(sel);
		
		
		//emp=getEmployeeWhenClose();
		System.out.println("closing "+name);//+emp.getName());

		st.close();
		//actionQuit(event);
		}
	}
}
