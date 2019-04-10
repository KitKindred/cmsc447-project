package gui;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;

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
	public void initialize() {
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
		
		String name = "Joey Parsley";
		
		manageSelectEmployee.getItems().add(name);
		
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
		
		//manageDateRange1.setVisible(false);
	}
	
	public void generate(ActionEvent event) {
		System.out.println("generate pressed");
		
	}
	
	public void makeNewEmployee() {
		println("new employee");
		manageSelectEmployee.getItems().add("new employee");
		
		
	}
	
	
	public void checkID(ActionEvent event) {
		System.out.println("emp selected");
		String name="Joey Parsley";
		int ID=1;
		name=manageSelectEmployee.getValue().toString();
		
		
		for(Node a: invisSelectEmployee) {
			a.setVisible(true);
			
		}
		
		manageEmployeeNameText.setText(name);//setText to be whatever the employee's name is
		manageEmployeeIDField.setText("ID: "+ID);//setText to be whatever the employee's id is
		manageActivityField.setValue("Active");//setValue to be whatever the employee's value is
		
	}
	/*eventually add Employee to the comboBox and not a string representation of one*/
	public void saveEmployee(ActionEvent event) {
		String name;
		name = manageSelectEmployee.getValue().toString();
		println("saving current employee "+name);
		/*manageSelectEmployee.getItems().add(new Employee(ID,"name"));*/
		/*.toString() ==>ID - Employee Name*/
		
	}
	
	
	public void checkIDMenu(ActionEvent event) {
		System.out.println("menu select");
//		manageSelectEmployee.setText("EMP");
		
	}
	
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
	
	public void stuff(ActionEvent event) {
		System.out.println("menu pressed");
		
		
	}
}
