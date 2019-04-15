package gui;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import sysfiles.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import classinfo.*;
import classinfo.Schedule;

public class controller {
	private void print() {print("");}
	private void println() {println("");}
	private void print(String s) {System.out.print(s);}
	private void println(String s) {System.out.println(s);}
	
	//GENERATE SCHEDULE TAB SECTION VARIABLES 
	@FXML
	Button genSchedExpoButton;
	
	
	//MANAGE EMPLOYEES TAB SECTION VARIABLES
	
	@FXML 
	Button manageSaveButton;

	@FXML
	TextField manageEmployeeNameText;
	
	@FXML
	ComboBox manageActivityField, manageSelectEmployee;
	
	@FXML
	Text manageEmployeeIDField,manageDateRange2;
	
	@FXML
	TextField manageDateRange1,manageDateRange3;
	
	
	private ArrayList<Node> invisSelectEmployee, invisDateRange;
	
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		invisSelectEmployee = new ArrayList<Node>();
		invisDateRange = new ArrayList<Node>();
		
		invisSelectEmployee.add(manageEmployeeIDField);
		invisSelectEmployee.add(manageEmployeeNameText);
		invisSelectEmployee.add(manageActivityField);
		invisSelectEmployee.add(manageSaveButton);

		invisDateRange.add(manageDateRange1);
		invisDateRange.add(manageDateRange2);
		invisDateRange.add(manageDateRange3);
		
		manageSelectEmployee.setVisible(true);
		
		
		
		manageEmployeeNameText.setText("FIRSTNAME LASTNAME");

		manageActivityField.getItems().addAll(
				"Active",
				"Inactive",
				"Maternity"
				);
		
		
		for(Node a: invisDateRange) {
			print("a");
			a.setVisible(false);
		}
		println();
		for(Node a: invisSelectEmployee) {
			print("a");
			a.setVisible(false);
			
		}
		println();
		populate();//IOFunctions.readAllEmployees());
		//manageDateRange1.setVisible(false);
	}
	
	
	/*GUI ACTION FUNCTIONS*/
	
	//will generate schedule
	public void generate(ActionEvent event) {
		System.out.println("SCHEDULE GENERATED");

		LocalDate d1 = LocalDate.of(2018, 11, 2);
		LocalDate d2 = LocalDate.of(2018, 11, 9);

		Schedule s = new Schedule(d1, d2);

		//ProgramDriver drive = new ProgramDriver();

		s.createSchedule(ProgramDriver.getActiveID(), ProgramDriver.getEmployees());
		s.printShifts();
	}
	
	//create a new employee and add to end of list
	public void makeNewEmployee() {
		println("new employee");
		//in place of a new gui, placeholder employee yay!
		manageSelectEmployee.getItems().add("new employee");
		ProgramDriver.addDoctor(0, "new employee", ProgramDriver.getID());
		
		//need to make the AddDoctor GUI
		//will return a created doctor to add to the map and stuff
		//ProgramDriver.addDoctor(, , i);
		
	}
	
	//the currently selected employee
	public void checkID(ActionEvent event) {
		System.out.println("checkID: emp selected");
		String name="Joey Parsley";


		name = manageSelectEmployee.getValue().toString();//some null ptr error here?????
		int ID=ProgramDriver.getNameID().get(name);

		for(Node a: invisSelectEmployee) {
			a.setVisible(true);
			
		}
		
		manageEmployeeNameText.setText(name);//setText to be whatever the employee's name is
		manageEmployeeIDField.setText("ID: "+ID);//setText to be whatever the employee's id is
		manageActivityField.setValue("Active");//setValue to be whatever the employee's value is
		
	}
	
	/*eventually add Employee to the comboBox and not a string representation of one*///maybe
	//as it is right now, it's ugly but it works
	public void saveEmployee(ActionEvent event) {
		String name = manageSelectEmployee.getValue().toString();
		String newName = manageEmployeeNameText.getText();
		int id=Integer.parseInt(manageEmployeeIDField.getText().split(" ")[1]);
		ProgramDriver.getNameID().put(newName, ProgramDriver.getNameID().remove(name));
		println("saving current employee "+name);
		
		//to properly update the combobox text selection
		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().remove(id);
		manageSelectEmployee.setValue(newName);
		manageSelectEmployee.getItems().add(id, newName);
		manageSelectEmployee.getSelectionModel().select(id);
		//manageSelectEmployee.Items[manageSelectEmployee.FindStringExact(name)] = newName;

		
		Profession joey;
		try {
		//IOFunctions.saveEmployeeToFile(joey);
			println(name+" "+id);
			joey= Main.getP().get(id);
			joey.setName(manageEmployeeNameText.getText());
			//joey.set
			
			IOFunctions.saveEmployeeToFile(id, joey);
			//manageSelectEmployee
			println("testst"+manageSelectEmployee.getEditor().getText());	
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}		
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

		String name = "Joey Parsley";
		HashMap<Integer, Profession> hp=ProgramDriver.getEmployees();
		for(Map.Entry<Integer, Profession>entry:hp.entrySet()) {//hp.entrySet()) {
			println(entry.toString());
			println(entry.getValue().toString());
			println(entry.getValue().getName().toString());
			name=entry.getValue().getName();
			println(name);
			manageSelectEmployee.getItems().add(name);
		}
		
		
		
	}
}
