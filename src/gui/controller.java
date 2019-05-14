package gui;
import java.awt.Event;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sysfiles.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
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
	Button manageSaveButton,createEmployeeButton,GetTimeOffButton,manageDateRange1;


	@FXML
	TextField manageEmployeeNameText,manageEmployeeEmail,manageDateStart;
	@FXML
	ComboBox manageActivityField, manageSelectEmployee;

	@FXML
	Text manageEmployeeIDField,manageEmployeeProfession;

	static int currentID;
	private ArrayList<Node> invisSelectEmployee, invisDateRange;

	private boolean clicked=false;

	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		invisSelectEmployee = new ArrayList<Node>();
		invisDateRange = new ArrayList<Node>();

		invisSelectEmployee.add(manageEmployeeIDField);
		invisSelectEmployee.add(manageEmployeeNameText);
		invisSelectEmployee.add(manageEmployeeEmail);
		invisSelectEmployee.add(manageEmployeeProfession);
		invisSelectEmployee.add(manageActivityField);
		invisSelectEmployee.add(manageSaveButton);
		invisSelectEmployee.add(GetTimeOffButton);


		invisDateRange.add(manageDateRange1);
		invisDateRange.add(manageDateStart);
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


		manageEmployeeNameText.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				editedWithoutSave=true;
				System.out.println("employee name changed");
			}
		});
		manageEmployeeEmail.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				editedWithoutSave=true;
				System.out.println("employee email changed");
			}
		});

		manageDateStart.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				editedWithoutSave=true;
				System.out.println("inactivity date start changed");
			}
		});
	}


	/*GUI ACTION FUNCTIONS*/

	public void actionChanged(ActionEvent event) {
		System.out.println("changed is now true");
		editedWithoutSave=true;
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
			st.setTitle("Set "+manageSelectEmployee.getSelectionModel().getSelectedItem().toString()+"'s Inactivity Date");

			st.showAndWait();

			if(dateStart.saveDate) {

				editedWithoutSave=true;
				ldtInactive=dateStart.req;

				manageDateStart.setText(ldtInactive.toString());
				ProgramDriver.getEmployees().get(currentID).setInactiveDate(ldtInactive);
			}
		}catch(Exception e) {System.out.println("error?"+e.toString());}

	}

	public static boolean changeBox() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Unsaved Changes to Employee");
		alert.setHeaderText("Unsaved Changes to Employee");
		alert.setContentText("Would you like to save changes to "+ProgramDriver.getEmployees().get(currentID).getName());

		alert.initModality(Modality.APPLICATION_MODAL);

		ButtonType yesB= new ButtonType("Yes");
		ButtonType noB= new ButtonType("No");

		alert.getButtonTypes().setAll(yesB,noB);
		Optional<ButtonType> result=alert.showAndWait();

		if(result.get()==yesB) {
			editedWithoutSave=false;
			return true;			
		}

		return false;
	}


	//will generate schedule
	public void generate(ActionEvent event) {
		System.out.println("ATTEMPTING TO GEN SCHEDULE");

		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}	
		}

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

	public void currentEmpSave(MouseEvent event) {
		//if(clicked)clicked=false;
		//else clicked=true;


		System.out.println("in currentEmpSave");
		if(editedWithoutSave) {
			System.out.println("edw/osave");
			if(changeBox()) {
				saveEmployee(new ActionEvent());
				editedWithoutSave=false;
				System.out.println("finished saved\n~~~~~~~~~~~~~~~~~~~~~~~\n");
			}	
			else {
				//checkID(new ActionEvent(manageSelectEmployee,manageSelectEmployee));				
			}
		}
		else {			
			System.out.println("gets stuck here for first click after confirmation gui");
			//checkID(new ActionEvent(manageSelectEmployee,manageSelectEmployee));
		}
		//manageSelectEmployee.getSelectionModel().clearAndSelect(manageSelectEmployee.getSelectionModel().getSelectedIndex());
	}

	//the currently selected employee
	public void checkID(ActionEvent event) {
		//System.out.println(""+event.getSource()+" "+event.getTarget());

		/*if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}	
		}*/

		System.out.println("checkID: emp selected: ");
		String name="Example Name";
		String email="Example Email";
		int active = 0;

		if (manageSelectEmployee.getValue() == null) {
			return;
		}
		System.out.println("test");

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
		manageEmployeeProfession.setText("Profession: "+getProf(emp.getType()));
		//println("\temp activity: "+active);
		manageActivityField.getSelectionModel().select(active);

		manageEmployeeEmail.setText(email);

		manageDateRange1.setText("Set "+getAct(emp.getActive())+" Date");
		manageDateStart.setText(emp.getInactiveDate().toString());

		//manageActivityField.setValue("Active");//setValue to be whatever the employee's value is
		editedWithoutSave=false;
	}

	private String getAct(int act) {
		switch(act) {
		case 0:
			return "Active";
		case 1:
			return "Inactive";
		case 2:
			return "Maternity";
		default:
			return"";

		}
	}
	private String getProf(int type) {
		switch(type) {
		case 0:
			return "Doctor";
		case 1:
			return "Moonlighter";
		case 2:
			return "Intern";
		default: 
			return null;


		}


	}

	/*eventually add Employee to the comboBox and not a string representation of one*///maybe
	//as it is right now, it's ugly but it works
	public void saveEmployee(ActionEvent event) {

		String name = manageSelectEmployee.getValue().toString();
		String newName = manageEmployeeNameText.getText().replace("~", "");

		String email = manageEmployeeEmail.getText().replace(" ","").replace("~", "");
		if(!email.contains("@")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Email Error");
			alert.setHeaderText("Email Formatting Error");
			alert.setContentText("Email's must have the @ symbol!");

			alert.showAndWait();
			return;

		}

		int active=manageActivityField.getSelectionModel().getSelectedIndex();

		int id=Integer.parseInt(manageEmployeeIDField.getText().split(" ")[1]);
		ProgramDriver.getNameID().put(newName, ProgramDriver.getNameID().remove(name));
		println("saving current employee "+name);

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
		System.out.println(active);
		manageActivityField.getSelectionModel().select(active);
		//println(manageActivityField.getSelectionModel().getSelectedIndex()+"");
		//to properly update the combobox text selection
		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().remove(id);
		manageSelectEmployee.setValue(newName);
		manageSelectEmployee.getItems().add(id, newName);
		manageSelectEmployee.getSelectionModel().select(id);
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
		manageDateRange1.setText("Set "+op+" Date");
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
		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}	
		}

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
		manageDateRange1.setText("Set "+getProf(emp.getType())+" Date");
		manageDateStart.setText(emp.getInactiveDate().toString());
	}

	// exits without saving, need to add in save flag check
	public void quit(ActionEvent event) {

		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}
			else {
				Platform.exit();
			}
		}
		else {
			Platform.exit();
		}


	}

	public static boolean getEditWithoutSave() {
		return editedWithoutSave;
	}

}