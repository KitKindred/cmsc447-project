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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

// TODO: Auto-generated Javadoc
/**
 * The Class AddEmployeeWindow.
 */
public class AddEmployeeWindow {
	
	/** The manage date range 1. */
	@FXML
	Button addButton,cancelButton,manageDateRange1;
	
	/** The profession box. */
	@FXML
	ComboBox activeBox, professionBox;
	
	/** The manage date start. */
	@FXML
	TextField nameBox,emailBox,manageDateStart;
	
	/** The invis date range. */
	ArrayList<Node> invisDateRange;
	
	/** The emp. */
	private static Profession emp=null;
	
	/** The return employee. */
	private static boolean returnEmployee=false;//=true;
	
	/** The inactive. */
	private LocalDateTime inactive;
	
	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
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
		
		nameBox.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.indexOf('~')!=-1) {
					nameBox.setText(nameBox.getText().replace("~",""));
					return;
				}
	
			}
		});
		
		emailBox.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.indexOf('~')!=-1) {
					emailBox.setText(emailBox.getText().replace("~",""));
					return;
				}
				if(newValue.indexOf("@")==-1) {
					
					emailBox.setStyle("-fx-control-inner-background: tomato");
				}
				else {
					emailBox.setStyle("-fx-control-inner-background: white;");
				}				
			}
		});
		
	}

	/**
	 * Action launch date range window.
	 *
	 * @param event the event
	 */
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
			
			st.setResizable(false);
			st.showAndWait();
			

			if(dateStart.saveDate) {
				ldtInactive=dateStart.req;
				System.out.println("t");
				System.out.println(ldtInactive.toString());
				DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				manageDateStart.setText(ldtInactive.format(format));
				inactive=(ldtInactive);
			}
		}catch(Exception e) {System.out.println("error? "+e.toString());}

	}	
	
/**
 * Gets the close.
 *
 * @return the close
 */
public static boolean getClose() {return returnEmployee;}
	
	/**
	 * Action add employee.
	 *
	 * @param event the event
	 */
	public void actionAddEmployee(ActionEvent event) {
		System.out.println("save employee?");
		returnEmployee=true;
		quit(event);
	}
	
	/**
	 * Action enter name.
	 *
	 * @param event the event
	 */
	public void actionEnterName(ActionEvent event) {}
	
	/**
	 * Action select profession.
	 *
	 * @param event the event
	 */
	public void actionSelectProfession(ActionEvent event) {}
	
	
	/**
	 * Action select activity.
	 *
	 * @param event the event
	 */
	public void actionSelectActivity(ActionEvent event) {
		String op = activeBox.getValue().toString();
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
	
	/**
	 * Action quit.
	 *
	 * @param event the event
	 */
	public void actionQuit(ActionEvent event) {	quit(event);}
	
	/**
	 * Action cancel.
	 *
	 * @param event the event
	 */
	public void actionCancel(ActionEvent event) {
		returnEmployee=false;
		quit(event);
	}
	
	/**
	 * Quit.
	 *
	 * @param event the event
	 */
	public void quit(ActionEvent event) {
		Stage st=(Stage)cancelButton.getScene().getWindow();
		if(returnEmployee==false) {
			System.out.println("closing empwindow without saving");
			st.close();
			return;
		}
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
				alert.setResizable(false);
				alert.showAndWait();
				
				returnEmployee=false;
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
	
				alert.setResizable(false);
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
				alert.setResizable(false);
				alert.showAndWait();
				returnEmployee = false;
				return;
			}
			
			ProgramDriver.addDoctor(type, name,ProgramDriver.getID(),email);
			System.out.println(ProgramDriver.getEmployees());
			System.out.println(ProgramDriver.getEmployees().size());
			HashMap<Integer, Profession> hmap=ProgramDriver.getEmployees();
			
			emp=(Profession)hmap.values().toArray()[hmap.size()-1];
			
			
			//emp = hmap.get(hmap.get(hmap.size()-1).getId());//ProgramDriver.getEmployees().get((ProgramDriver.getEmployees().size()-1));
			
			System.out.println(sel);
			System.out.println(emp.toString());
			
			emp.setActive(sel);
			if(inactive!=null && true)
				emp.setInactiveDate(inactive);
			else {emp.setInactiveDate(null);}
			System.out.println("closing "+name+" "+returnEmployee);//+emp.getName());
	
			st.close();
		}
	}
}