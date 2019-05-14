package gui;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sysfiles.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import classinfo.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sysfiles.IOFunctions;
import classinfo.*;
public class controller {
	private void print() {print("");}
	private void println() {println("");}
	private void print(String s) {System.out.print(s);}
	private void println(String s) {System.out.println(s);}

	private static boolean editedWithoutSave = false; 

	//GENERATE SCHEDULE TAB SECTION VARIABLES 
	@FXML
	Button genSchedExpoButton;


	//MANAGE EMPLOYEES TAB SECTION VARIABLES

	@FXML 
	Button manageSaveButton,createEmployeeButton,GetTimeOffButton;

	@FXML
	TextField manageEmployeeNameText,manageDateRange1,manageEmployeeEmail;
	@FXML
	ComboBox manageActivityField, manageSelectEmployee;

	@FXML
	Text manageEmployeeIDField;

	static int currentID;
	private ArrayList<Node> invisSelectEmployee, invisDateRange;

	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		invisSelectEmployee = new ArrayList<Node>();
		invisDateRange = new ArrayList<Node>();

		invisSelectEmployee.add(manageEmployeeIDField);
		invisSelectEmployee.add(manageEmployeeNameText);
		invisSelectEmployee.add(manageEmployeeEmail);
		invisSelectEmployee.add(manageActivityField);
		invisSelectEmployee.add(manageSaveButton);
		invisSelectEmployee.add(GetTimeOffButton);
		
		
		invisDateRange.add(manageDateRange1);
		//invisDateRange.add(manageDateRange2);
		//invisDateRange.add(manageDateRange3);

		manageSelectEmployee.setVisible(true);



		manageEmployeeNameText.setText("FIRSTNAME LASTNAME");
		manageEmployeeEmail.setText("");
		
		manageActivityField.getItems().addAll(
				"Active",
				"Inactive",
				"Maternity"
				);

		
		for(Node a: invisDateRange) {
			a.setVisible(false);
		}

		
		for(Node a: invisSelectEmployee) {
			a.setVisible(false);
		}

		populate();//IOFunctions.readAllEmployees());
		//manageDateRange1.setVisible(false);
	}


	/*GUI ACTION FUNCTIONS*/

	//will generate schedule
	public void generate(ActionEvent event) {
		System.out.println("SCHEDULE GENERATED");

		LocalDate d1 = LocalDate.of(2018, 7, 1);
		LocalDate d2 = LocalDate.of(2018, 10, 1);

		Schedule s = new Schedule(d1, d2);

		//ProgramDriver drive = new ProgramDriver();

		if(ProgramDriver.getEmployees().size()==0) {
			System.out.println("No employees to schedule for!");
			return;
		}
		
		s.createSchedule(ProgramDriver.getActiveID(), ProgramDriver.getEmployees());
		s.printShifts();
	}
/*
	//create a new employee and add to end of list
	public void makeNewEmployee() {
		println("new employee");
		//in place of a new gui, placeholder employee yay!
		manageSelectEmployee.getItems().add("new employee");
		ProgramDriver.addDoctor(0, "new employee", ProgramDriver.getID());

		//need to make the AddDoctor GUI
		//will return a created doctor to add to the map and stuff
		//ProgramDriver.addDoctor(, , i);

	}*/

	//the currently selected employee
	public void checkID(ActionEvent event) {
		System.out.println("checkID: emp selected: ");
		String name="Example Name";
		String email="Example Email";
		int active = 0;
		// fix nullpointerexception, when checkID is called and there is nothing selected
		// no idea what is actually causing the extra call though
		if (manageSelectEmployee.getValue() == null) {
			return;
		}

		
		//int ID=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		
		name = manageSelectEmployee.getValue().toString();
		
		int ID=ProgramDriver.getNameID().get(name);
		currentID=ID;
		//println("\n\n\tcurrentID: "+currentID);
		Profession emp=ProgramDriver.getEmployees().get(currentID);
		name=emp.getName();
		active=emp.getActive();
		email=emp.getEmail();
		System.out.println(name+" "+active+"\n");
		for(Node a: invisSelectEmployee) {
			a.setVisible(true);
		}

		manageEmployeeNameText.setText(name);//setText to be whatever the employee's name is
		manageEmployeeIDField.setText("ID: "+ID);//setText to be whatever the employee's id is
		//println("\temp activity: "+active);
		manageActivityField.getSelectionModel().select(active);
		
		manageEmployeeEmail.setText(email);
		//manageActivityField.setValue("Active");//setValue to be whatever the employee's value is

	}

	/*eventually add Employee to the comboBox and not a string representation of one*///maybe
	//as it is right now, it's ugly but it works
	public void saveEmployee(ActionEvent event) {
	
		String name = manageSelectEmployee.getValue().toString();
		String newName = manageEmployeeNameText.getText().replace("~", "");
		
		String email = manageEmployeeEmail.getText().replace(" ","");
		
		int active=manageActivityField.getSelectionModel().getSelectedIndex();
		
		int id=Integer.parseInt(manageEmployeeIDField.getText().split(" ")[1]);
		ProgramDriver.getNameID().put(newName, ProgramDriver.getNameID().remove(name));
		println("saving current employee "+name);

		//to properly update the combobox text selection
		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().remove(id);
		manageSelectEmployee.setValue(newName);
		manageSelectEmployee.getItems().add(id, newName);
		manageSelectEmployee.getSelectionModel().select(id);
		

		Profession worker;
		try {
			
			println(name+" "+id);
			worker= Main.getP().get(id);
			worker.setName(manageEmployeeNameText.getText());
			worker.setEmail(email);
			worker.setActive(active);


			IOFunctions.saveEmployees();
			editedWithoutSave=false;

				
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}		
		
		println(manageActivityField.getSelectionModel().getSelectedIndex()+"");
		
	}

	//currently unused?
	public void checkIDMenu(ActionEvent event) {
		System.out.println("menu select");
		//		manageSelectEmployee.setText("EMP");

	}

	//when certain Active combobox option is selected, either enables or disables view of the daterange that would be used beside it
	//currently, that range is unused
	public void showDateRange(ActionEvent event) {
		println("Clicked on ComboBox Option");
		String op = manageActivityField.getValue().toString();
		println(manageActivityField.getValue().toString());
		manageDateRange1.setText("");
		switch(op) {
		case "Active":
			for(Node a: invisDateRange) {
				a.setVisible(false);
			}break;
		default:
			for(Node a: invisDateRange) {
				a.setVisible(true);
			}break;		
		}		

	}

	//unused
	public void stuff(ActionEvent event) {
		System.out.println("stuff menu pressed");
	}

	//populate the employee selection box with employees read from files 
	public void populate() {//HashMap<Integer, Profession> hp) {
		manageSelectEmployee.getItems().clear();
		String name = "Sample Employee";
		HashMap<Integer, Profession> hp=ProgramDriver.getEmployees();
		for(Map.Entry<Integer, Profession>entry:hp.entrySet()) {
			println(entry.toString());
			println(entry.getValue().toString());
			println(entry.getValue().getName().toString());
			name=entry.getValue().getName();
			println(name);
			manageSelectEmployee.getItems().add(name);
		}
	}

	public static int getSelectedID() {
		System.out.println("Selected ID may be: "+currentID);
		return currentID;}



	public void actionSelectTimeOff(ActionEvent event) {
		try {
			String path="/gui/TimeOffRequests.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st = new Stage();
			Scene scene = new Scene(root);
			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle(manageSelectEmployee.getSelectionModel().getSelectedItem().toString()+"'s Time Off Requests");

			System.out.println("before show");

			st.showAndWait();
			System.out.println("after show");

			if(TimeOffRequestWindow.changed) {
				System.out.println("tors changed");

				ProgramDriver.getEmployees().get(manageSelectEmployee.getSelectionModel().getSelectedIndex()).setTimeOff(TimeOffRequestWindow.tor);

				editedWithoutSave=true;
				TimeOffRequestWindow.changed=false;
			}
		}catch(Exception e) {System.out.println("error?"+e.toString());}

	}

	public void actionLaunchEmployeeCreationWindow(ActionEvent event) {
		//		Profession newEmp=null;
		try {

			String path="/gui/addEmployee.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st= new Stage();
			Scene scene = new Scene(root);

			st.setScene(scene);

			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Create a New Employee");
			st.showAndWait();

			if(AddEmployeeWindow.getClose()) {
				addEmployee(ProgramDriver.getEmployees().get(ProgramDriver.getID()-1));
				editedWithoutSave=true;	
			}

			//newEmp=AddEmployeeWindow.getEmp();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addEmployee(Profession emp) {
		if (emp == null) {
			System.out.println("empwindow closed without saving");
			return;
		}
		println("\tid: "+emp.getId()+" "+emp.getActive());
		manageSelectEmployee.getItems().add(emp.getName());
		manageSelectEmployee.getSelectionModel().select(emp.getId());
		manageEmployeeEmail.setText(emp.getEmail());
		manageActivityField.getSelectionModel().select(emp.getActive());;
		
	}

	// exits without saving, need to add in save flag check
	public void quit(ActionEvent event) {
		println("quit pressed");
		if (!editedWithoutSave) {
			Platform.exit();
		}
		else {
			println("would you like to save?");
			println("Trying to exit without saving!");
		}
	}

	public static boolean getEditWithoutSave() {
		return editedWithoutSave;
	}

}