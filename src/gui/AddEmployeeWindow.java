package gui;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sysfiles.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import classinfo.*;

public class AddEmployeeWindow {
	@FXML
	Button addButton,cancelButton,manageDateRange1;
	@FXML
	ComboBox activeBox, professionBox;
	@FXML
	TextField nameBox,emailBox,manageDateStart;
	
	ArrayList<Node> invisDateRange;
	
	private static Profession emp=null;
	private static boolean returnEmployee=false;//=true;
	private LocalDateTime inactive;
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		//returnEmployee=true;
		invisDateRange = new ArrayList<Node>();
		activeBox.getItems().addAll("Active","Inactive","Maternity");
		professionBox.getItems().addAll("Doctor","Moonlighter","Intern");

		activeBox.setValue(activeBox.getItems().get(0));
		professionBox.setValue(professionBox.getItems().get(0));
		manageDateRange1.setText("");

		emailBox.setText("");
		invisDateRange.add(manageDateRange1);
		invisDateRange.add(manageDateStart);
		
		for(Node n:invisDateRange) {
			n.setVisible(false);
			
		}
		
	}


	public void actionLaunchDateRangeWindow(ActionEvent event) {
		LocalDateTime ldtInactive;
		try {
			String path="/gui/dateStart.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st = new Stage();
			Scene scene = new Scene(root);
			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Set "+activeBox.getSelectionModel().getSelectedItem().toString()+"'s Inactivity Date");

			st.showAndWait();

			if(dateStart.saveDate) {
				ldtInactive=dateStart.req;
				System.out.println("t");
				System.out.println(ldtInactive.toString());
				DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				manageDateStart.setText(ldtInactive.format(format));
				
//				ProgramDriver.getEmployees().get(currentID).setInactiveDate(ldtInactive);
				inactive=(ldtInactive);
			}
		}catch(Exception e) {System.out.println("error? "+e.toString());}

	}	
	
public static boolean getClose() {return returnEmployee;}
	public void actionAddEmployee(ActionEvent event) {
		System.out.println("save employee?");
		returnEmployee=true;
		quit(event);
	}
	public void actionEnterName(ActionEvent event) {}
	public void actionSelectProfession(ActionEvent event) {}
	
	
	public void actionSelectActivity(ActionEvent event) {
		//println("Clicked on ComboBox Option");
		String op = activeBox.getValue().toString();
		
		//println(activeBox.getValue().toString());
		manageDateRange1.setText("Set "+op+" Date");
		switch(op) {
		case "Active":
			for(Node a: invisDateRange) {
				a.setVisible(false);
			}break;
		case "Inactive":
			for(Node a: invisDateRange) {
				a.setVisible(false);
			}break;
		default:
			for(Node a: invisDateRange) {
				a.setVisible(true);
			}break;		
		}		
	}
	
	public void actionQuit(ActionEvent event) {	quit(event);}
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
			//st.close();
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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Profession Error");
			alert.setHeaderText("No Profession Selected");
			alert.setContentText("Please select a profession!");

			alert.showAndWait();
			returnEmployee = false;
			return;
		}
		
		int sel=activeBox.getSelectionModel().getSelectedIndex();
		System.out.println("selected active window: "+sel);
		if (sel == 2 && inactive == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Maternity Error");
			alert.setHeaderText("No Maternity Start Date Selected");
			alert.setContentText("Please select a start date for Maternity Leave!");

			alert.showAndWait();
			returnEmployee = false;
			return;
		}
		
		ProgramDriver.addDoctor(type, name,ProgramDriver.getID(),email);
		emp = ProgramDriver.getEmployees().get(ProgramDriver.getEmployees().size()-1);
		
		emp.setActive(sel);
		if(inactive!=null && true)
			emp.setInactiveDate(inactive);
		else {emp.setInactiveDate(null);}
		//ProgramDriver.getEmployees().get(ProgramDriver.getEmployees().size()-1).setActive(sel);
		
		
		//emp=getEmployeeWhenClose();
		System.out.println("closing "+name+" "+returnEmployee);//+emp.getName());

		st.close();
		//actionQuit(event);
		}
	}
}