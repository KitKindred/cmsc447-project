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
	TextField nameBox;

	private static Profession emp=null;
	private static boolean returnEmployee;//=true;
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		returnEmployee=true;
		activeBox.getItems().addAll("Active","Inactive","Maternity");
		professionBox.getItems().addAll("Doctor","Moonlighter","Intern");

		activeBox.setValue(activeBox.getItems().get(0));
		professionBox.setValue(professionBox.getItems().get(0));

		
		
	}

public static boolean getClose() {return returnEmployee;}
	public void actionAddEmployee(ActionEvent event) {
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
		String t=professionBox.getValue().toString();
		String name=nameBox.getText().replace("~", "");
		
		int type=0;
		
		switch(t) {
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
		
		ProgramDriver.addDoctor(type, name,ProgramDriver.getID());
		//emp=getEmployeeWhenClose();
		System.out.println("closing "+name);//+emp.getName());

		st.close();
		//actionQuit(event);
		}
	}
/*
	public Profession getEmployeeWhenClose() {
		Profession p = new Doctor(ProgramDriver.getID(),0,"name name");
		p.setName(nameBox.getText());

		Object o=professionBox.getValue();
		if(o==null) {System.out.println("professionbox uninitialized?");}
		else {
			System.out.println("professionBox.getValue().toString(): "+professionBox.getValue().toString());


			switch(o.toString()) {
			case "Doctor":
				p.setType(0);
				break;
			case "Moonlighter":
				p.setType(1);
				break;
			case "Intern":
				p.setType(2);
				break;
			default:
				System.out.println("ERROR UNSELECTED PROFESSION");
				break;
			}
		}

		o=activeBox.getValue();
		if(o==null) {
			System.out.println("activebox uninitialized?");
		}
		else {
			switch(o.toString()) {
			case "Active":
				p.setActive(true);
				break;
			case "Inactive":
				p.setActive(false);
				break;
			case "Maternity":
				p.setActive(false);
				break;
			default:
				System.out.println("ERROR UNSELECTED ACTIVITY");
				break;
			}
		}
		return p;
		
		
	}
	public static Profession getEmp() {
		Profession e=emp;
		emp=null;
		return e;
	}*/
}
