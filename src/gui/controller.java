package gui;
import java.awt.Event;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

	private static LocalDateTime currentSelectedDate=null;
	@FXML
	Button quarterDateButton;

	@FXML
	ComboBox quarterDateSelect;

	@FXML
	TextField quarterDateTextField;

	@FXML
	MenuItem fileNewEmployee;

	private static ArrayList<LocalDateTime> ldts;

	public static LocalDateTime getCurrentDate() {return currentSelectedDate;}
	public static String getFormattedCurrentDate() {
		DateTimeFormatter format=DateTimeFormatter.ofPattern("MM-dd-yyyy");
		return currentSelectedDate.format(format);
	}

	public static String getFileCurrentDate() {
		DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
		return currentSelectedDate.format(format);
	}
	
	private void loadDates() {

		
		String fp="./src/sysfiles/profiles";
		File f=new File(fp);
		
		LocalDateTime ldt;
		
		for(File file: f.listFiles()) {
			System.out.println(file.getName());
			for(String s:file.getName().split(".")) {
				System.out.println(s);
			}
			String name=file.getName().substring(0, file.getName().indexOf("."));
			
			System.out.println();
			//String name=file.getName().split(".")[0];
			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
			ldt=LocalDateTime.parse(name,format);
			System.out.println(ldt);
			
			currentSelectedDate=ldt;
			
			ldts.add(ldt);
			quarterDateSelect.getItems().add(getFormattedCurrentDate());//.split(".")[0]);
			currentSelectedDate=null;
			
		}
		System.out.println(f+" "+f.listFiles());

	}

	private HashMap<String, String> dateConversion=new HashMap<String, String>();
	public void quarterAction(ActionEvent event) {
		LocalDateTime ldtAtt;
		try {
			String path="/gui/dateStart.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st = new Stage();
			Scene scene = new Scene(root);
			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Set Calendar Quarter Start Date");

			st.setResizable(false);
			st.showAndWait();

			if(dateStart.saveDate) {
				if(dateStart.req!=null) {
					actionChanged(event);
					ldtAtt=dateStart.req;
					dateStart.req=null;

					System.out.println("BEFORE FORMAT: "+ldtAtt);
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					String formatted=ldtAtt.format(format);

					quarterDateTextField.setText(formatted);
					currentSelectedDate=ldtAtt;
					ldts.add(ldtAtt);
					System.out.println(formatted+" "+ldts);
					quarterDateSelect.getItems().add(formatted);

					dateConversion.put(ldtAtt.toString(),formatted);
					
					quarterDateSelect.getSelectionModel().select(ldts.size()-1);
				}
			}
			
			//quarterSelect(null);
		}catch(Exception e) {System.out.println("error?"+e.toString());}
	}
	public static void setCurrentDate(LocalDateTime l) {
		currentSelectedDate=l;
	}
	public void quarterSelect(ActionEvent event) {
		if(quarterDateSelect.getSelectionModel().getSelectedIndex()==-1) {return;}
		currentSelectedDate=ldts.get(quarterDateSelect.getSelectionModel().getSelectedIndex());
		enableAfterDate();

		try {
			clearAll();
			IOFunctions.loadEmployees();
			
			
			System.out.println(ProgramDriver.getEmployees());
			populate();
			System.out.println(ProgramDriver.getEmployees());
		} catch (IOException e) {
			System.out.println("error loading files");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	/***~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~***/

	private static boolean editedWithoutSave = false; 
	private static boolean isSwitchingEmps = false;

	//GENERATE SCHEDULE TAB SECTION VARIABLES 
	@FXML
	Button genSchedExpoButton;

	@FXML 
	Button manageSaveButton,createEmployeeButton,GetTimeOffButton,manageDateRange1,attendingWeekButton, deleteButton;


	@FXML
	TextField manageEmployeeNameText,manageEmployeeEmail,manageDateStart,attendingWeekText;
	@FXML
	ComboBox manageActivityField, manageSelectEmployee;

	@FXML
	Text manageEmployeeIDField,manageEmployeeProfession,activityText,attendText,maternityText,emailText;

	@FXML
	Tab meTab;

	static int currentID=-1;
	private ArrayList<Node> invisSelectEmployee, invisDateRange, invisDoctor,disableUntilLoad;
	private ArrayList disableBeforeDate;

	private int oldindex=-1;

	private Profession getLastEmployee() {
		return (Profession)ProgramDriver.getEmployees().values().toArray()[ProgramDriver.getEmployees().size()-1];}
	private void disableBeforeDate() {
		meTab.setDisable(true);
		fileNewEmployee.setDisable(true);}

	private void enableAfterDate() {
		meTab.setDisable(false);
		fileNewEmployee.setDisable(false);
	}

	private void blankSpots() {
		for(Node a: invisDateRange) {a.setVisible(false);}
		for(Node a: invisSelectEmployee) {a.setVisible(false);}
		System.out.println("a"+invisDoctor);
		for(Node a: invisDoctor) {a.setVisible(false);}
	}
	
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		ldts=new ArrayList<LocalDateTime>();

		loadDates();
		disableBeforeDate();

		invisSelectEmployee = new ArrayList<Node>();
		invisDateRange = new ArrayList<Node>();
		invisDoctor= new ArrayList<Node>();
		disableUntilLoad=new ArrayList<Node>();

		invisSelectEmployee.add(manageEmployeeIDField);
		invisSelectEmployee.add(manageEmployeeNameText);
		invisSelectEmployee.add(manageEmployeeEmail);
		invisSelectEmployee.add(manageEmployeeProfession);
		invisSelectEmployee.add(manageActivityField);
		//invisSelectEmployee.add(manageSaveButton);
		invisSelectEmployee.add(GetTimeOffButton);
		invisSelectEmployee.add(emailText);
		invisSelectEmployee.add(activityText);

		invisSelectEmployee.add(deleteButton);
		
		disableUntilLoad.add(manageSaveButton);
		disableUntilLoad.add(createEmployeeButton);
		disableUntilLoad.add(genSchedExpoButton);
		disableUntilLoad.add(manageSelectEmployee);

		for(Node a: disableUntilLoad) {a.setDisable(true);}

		invisDateRange.add(manageDateRange1);
		invisDateRange.add(manageDateStart);
		invisDateRange.add(maternityText);
		//invisDateRange.add(manageDateRange2);
		//invisDateRange.add(manageDateRange3);

		System.out.println(attendingWeekButton);
		invisDoctor.add(attendingWeekButton);
		invisDoctor.add(attendingWeekText);
		invisDoctor.add(attendText);
		manageSelectEmployee.setVisible(true);

		manageEmployeeNameText.setText("FIRSTNAME LASTNAME");
		manageEmployeeEmail.setText("");

		manageActivityField.getItems().addAll(
				"Active",
				"Inactive",
				"Maternity"
				);

		//manageSelectEmployee.setStyle("<color-stop>red 70%");

		for(Node a: invisDateRange) {a.setVisible(false);}
		for(Node a: invisSelectEmployee) {a.setVisible(false);}
		System.out.println("a"+invisDoctor);
		for(Node a: invisDoctor) {a.setVisible(false);}
		//deleteButton.setVisible(false);
		
		manageEmployeeNameText.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if(newValue.indexOf('~')!=-1) {
					manageEmployeeNameText.setText(manageEmployeeNameText.getText().replace("~",""));
					return;
				}
				if(oldindex!=-1) {
					String old=ProgramDriver.getEmployees().get(currentID).getName();
					String name=manageEmployeeNameText.getText();
					ProgramDriver.getEmployees().get(currentID).setName(name);
					ProgramDriver.getNameID().remove(old);
					ProgramDriver.getNameID().put(name, currentID);
					actionChanged(null);
				}
			}
		});
		manageEmployeeNameText.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
				if(oldindex!=-1) {
					if(newPropertyValue){
						String old=ProgramDriver.getEmployees().get(currentID).getName();
						String name=manageEmployeeNameText.getText();
						ProgramDriver.getEmployees().get(currentID).setName(name);
						ProgramDriver.getNameID().remove(old);
						ProgramDriver.getNameID().put(name, currentID);
					}
				}
			}
		});

		manageEmployeeEmail.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(currentID!=-1) {
					if(newValue.indexOf("~")!=-1) {
						manageEmployeeEmail.setText(manageEmployeeEmail.getText().replace("~",""));
						return;
					}
					String mail=manageEmployeeEmail.getText();
					ProgramDriver.getEmployees().get(currentID).setEmail(mail);
					actionChanged(null);
				}
			}
		});
		manageEmployeeEmail.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean o, Boolean n) {
				if(currentID!=-1) {
					if(n) {
						String mail=manageEmployeeEmail.getText();
						ProgramDriver.getEmployees().get(currentID).setEmail(mail);
					}
				}
			}
		});


		for(Node a: disableUntilLoad) {a.setDisable(false);}

	}

	/*GUI ACTION FUNCTIONS*/

	public void actionChanged(ActionEvent event) {
		if (!isSwitchingEmps) {
			System.out.println("Changed is now true");
			editedWithoutSave=true;
		}
	}

	private void clearAll() {
		//ProgramDriver.getActiveID().clear();
		//ProgramDriver.getEmployees().clear();
		//ProgramDriver.getNameID().clear();

		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().clear();
		blankSpots();
		oldindex=-1;
		currentID=-1;

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
			st.setResizable(false);
			st.showAndWait();

			if(dateStart.saveDate) {
				if(dateStart.req!=null) {
					actionChanged(event);
					ldtInactive=dateStart.req;
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					manageDateStart.setText(ldtInactive.format(format));
					ProgramDriver.getEmployees().get(currentID).setInactiveDate(ldtInactive);}
			}
		}catch(Exception e) {System.out.println("error?"+e.toString());}

	}


	public void actionLaunchAttendingWindow(ActionEvent event) {
		if(ProgramDriver.getEmployees().get(currentID).getType()!=0) {System.out.println("not a doctor");return;}
		LocalDateTime ldtAtt;
		try {
			String path="/gui/dateStart.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st = new Stage();
			Scene scene = new Scene(root);
			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Set "+manageSelectEmployee.getSelectionModel().getSelectedItem().toString()+"'s Attending Week Date");

			st.setResizable(false);
			st.showAndWait();

			if(dateStart.saveDate) {
				if(dateStart.req!=null) {
					actionChanged(event);
					ldtAtt=dateStart.req;

					/*just let them choose whatever day to start attending week for
					WeekFields wee=WeekFields.of(Locale.getDefault());
					System.out.println(ldtAtt.getDayOfWeek().getValue()+" "+wee.getFirstDayOfWeek().getValue());
					ldtAtt=ldtAtt.minusDays(ldtAtt.getDayOfWeek().getValue()-wee.getFirstDayOfWeek().getValue());
					 */

					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					attendingWeekText.setText(ldtAtt.format(format));
					((Doctor)ProgramDriver.getEmployees().get(currentID)).setAttendingDate(ldtAtt);
				}
			}
		}catch(Exception e) {System.out.println("error?"+e.toString());}

	}

	public static boolean changeBox() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Unsaved Changes to Employee");
		alert.setHeaderText("Unsaved Changes to Employee");
		alert.setContentText("Would you like to save changes to your Employees?");// to "+ProgramDriver.getEmployees().get(currentID).getName());

		alert.initModality(Modality.APPLICATION_MODAL);

		ButtonType yesB= new ButtonType("Yes");
		ButtonType noB= new ButtonType("No");

		alert.getButtonTypes().setAll(yesB,noB);
		alert.setResizable(false);
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


		if(ProgramDriver.getEmployees().size()==0) {
			System.out.println("No employees to schedule for!");
			return;
		}

		s.createSchedule(ProgramDriver.getActiveID(), ProgramDriver.getEmployees());
		s.printShifts();
		if (IOFunctions.exportCalendar(s.export())) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Calendar Exported");
			alert.setHeaderText("Calendar Exported Successfully");
			alert.setContentText("Calendar saved to cal.ics!");

			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Calendar Not Exported");
			alert.setHeaderText("Calendar Exporting Failed");
			alert.setContentText("Calendar failed to save!");

			alert.showAndWait();
		}
	}

	//the currently selected employee
	public void checkID(ActionEvent event) {

		System.out.println("checkID: emp selected: ");
		String name="Example Name";
		String email="Example Email";
		int active = 0;

		if(manageSelectEmployee.getSelectionModel().getSelectedIndex()==-1) {return;}
		if (manageSelectEmployee.getValue() == null) {return;}
		
		System.out.println("oldindex: "+oldindex);
		isSwitchingEmps = true;
		System.out.println("test");


		name = manageSelectEmployee.getValue().toString();

		HashMap<String, Integer> nameID=ProgramDriver.getNameID();
		//currentID=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		currentID=nameID.get(manageSelectEmployee.getSelectionModel().getSelectedItem());
		System.out.println("currentID: "+currentID);
		Profession emp=ProgramDriver.getEmployees().get(currentID);

		name=emp.getName();
		active=emp.getActive();
		email=emp.getEmail();
		System.out.println(name+" "+active+"\n");
		for(Node a: invisSelectEmployee) {
			a.setVisible(true);
		}
		deleteButton.setVisible(true);

		manageEmployeeNameText.setText(name);//setText to be whatever the employee's name is
		manageEmployeeIDField.setText("ID: "+currentID);//setText to be whatever the employee's id is
		manageEmployeeProfession.setText("Profession: "+getProf(emp.getType()));
		manageActivityField.getSelectionModel().select(active);

		manageEmployeeEmail.setText(email);

		manageDateRange1.setText("Set "+getAct(emp.getActive())+" Date");
		if(emp.getActive()==2) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			manageDateStart.setText(emp.getInactiveDate().format(format));
		}

		//oldindex=manageActivityField.getSelectionModel().getSelectedIndex();
		oldindex=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		if(emp.getType()==0) {

			/*for(Node a: invisDoctor) {
				a.setVisible(true);				
			}*/
			LocalDateTime ldt=((Doctor)emp).getAttendingDate();
			if(ldt!=null) {
				DateTimeFormatter format=DateTimeFormatter.ofPattern("MM/dd/yyyy");
				attendingWeekText.setText(ldt.format(format));
			}
			else {attendingWeekText.setText("");}
		}
		else {
			/*
			for(Node a:invisDoctor) {
				a.setVisible(false);
			}*/
			attendingWeekText.setText("");
		}
		showDateRange(null);
		isSwitchingEmps = false;
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


	public void saveEmployee(ActionEvent event) {

		if(currentID==-1) {return;}

		String name = manageSelectEmployee.getValue().toString();
		String newName = manageEmployeeNameText.getText();

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

		int id=oldindex;
		ProgramDriver.getNameID().put(newName, ProgramDriver.getNameID().remove(name));
		System.out.println("saving current employee "+name);

		Profession worker=ProgramDriver.getEmployees().get(currentID);
		try {
			System.out.println(name+" "+id);

			worker.setName(manageEmployeeNameText.getText());
			worker.setEmail(email);

			/*maternity leave check*/

			if(active==2) {
				if(worker.getInactiveDate()==null) {
					throw new Exception(worker.getName()+"'s Maternity Leave has no Associated Start Date!");
				}
			}

			IOFunctions.saveEmployees();
			editedWithoutSave=false;

			
			System.out.println(active);
			System.out.println("id: "+id);
			manageActivityField.getSelectionModel().select(active);
			manageSelectEmployee.getSelectionModel().clearSelection();
			manageSelectEmployee.getItems().remove(id);
			manageSelectEmployee.getItems().add(id, newName);
			manageSelectEmployee.getSelectionModel().select(id);

		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}		
		
	}

	// TODO: implement
	public void deleteEmployee(ActionEvent event) {
		System.out.println("delete pressed");

		int id;
		int index;
		String name=manageSelectEmployee.getSelectionModel().getSelectedItem().toString();
		HashMap<String, Integer> nameID=ProgramDriver.getNameID();

		index=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		id=nameID.get(name);

		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().remove(index);

		ProgramDriver.getEmployees().remove(id);
		ProgramDriver.getNameID().remove(name);
		if(ProgramDriver.getActiveID().contains(id)) {ProgramDriver.getActiveID().remove(id);}

		editedWithoutSave=true;
		currentID=-1;

		for(Node a:invisSelectEmployee) {a.setVisible(false);}
		for(Node a:invisDoctor) {a.setVisible(false);}
		for(Node a:invisDateRange) {a.setVisible(false);}
		deleteButton.setVisible(false);
	}


	//when certain Active combobox option is selected, either enables or disables view of the daterange that would be used beside it
	public void showDateRange(ActionEvent event) {
		String op = manageActivityField.getValue().toString();
		System.out.println(manageActivityField.getValue().toString());
		manageDateRange1.setText("Set "+op+" Date");
		int index=manageActivityField.getSelectionModel().getSelectedIndex();
		//System.out.println("new index");
		Profession prof=ProgramDriver.getEmployees().get(currentID);

		if(currentID!=-1) {

			prof.setActive(manageActivityField.getSelectionModel().getSelectedIndex());
		}
		switch(op) {
		case "Active":
			for(Node a: invisDateRange) {
				a.setVisible(false);
			}
			if(prof.getType()==0) {
				for(Node a:invisDoctor) {
					a.setVisible(true);
				}
			}
			System.out.println("CURRENT ID: "+currentID);
			ProgramDriver.getEmployees().get(currentID).setActive(index);
			ProgramDriver.getEmployees().get(currentID).setInactiveDate(null);
			break;
		case "Inactive":
			for(Node a: invisDateRange) {
				a.setVisible(false);
			}
			for(Node a: invisDoctor) {
				a.setVisible(false);
			}
			System.out.println("CURRENT ID: "+currentID);
			ProgramDriver.getEmployees().get(currentID).setActive(index);
			ProgramDriver.getEmployees().get(currentID).setInactiveDate(null);
			break;
		default:
			for(Node a: invisDateRange) {
				a.setVisible(true);
			}
			if(prof.getType()==0) {
				for(Node a:invisDoctor) {
					a.setVisible(true);
				}
			}
			System.out.println("maternity check");

			if(ProgramDriver.getEmployees().get(currentID).getInactiveDate()==null) {
				System.out.println("inactive date nonexist, using today");
				LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
				ProgramDriver.getEmployees().get(currentID).setInactiveDate(today);
				DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				manageDateStart.setText(today.format(format));
			}
			break;		
		}
		actionChanged(event);

		//oldindex=index; 
		System.out.println(oldindex);
	}

	//populate the employee selection box with employees read from files 
	public void populate() {
		System.out.println("populate: "+ProgramDriver.getEmployees());
		manageSelectEmployee.getItems().clear();
		String name = "Sample Employee";
		HashMap<Integer, Profession> hp=ProgramDriver.getEmployees();
		for(Map.Entry<Integer, Profession>entry:hp.entrySet()) {
			System.out.println(entry.toString());
			System.out.println(entry.getValue().toString());
			System.out.println(entry.getValue().getName().toString());
			name=entry.getValue().getName();
			System.out.println(name);
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
			st.setResizable(false);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle(manageSelectEmployee.getSelectionModel().getSelectedItem().toString()+"'s Time Off Requests");

			System.out.println("before show");

			st.showAndWait();
			System.out.println("after show");

			if(TimeOffRequestWindow.changed) {
				System.out.println("tors changed");

				ProgramDriver.getEmployees().get(manageSelectEmployee.getSelectionModel().getSelectedIndex()).setTimeOff(TimeOffRequestWindow.tor);

				actionChanged(event);
				TimeOffRequestWindow.changed=false;
			}
		}catch(Exception e) {System.out.println("error?"+e.toString());}

	}

	public void actionLaunchEmployeeCreationWindow(ActionEvent event) {

		try {

			String path="/gui/addEmployee.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st= new Stage();
			Scene scene = new Scene(root);

			st.setScene(scene);
			st.setResizable(false);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Create a New Employee");
			st.showAndWait();

			if(AddEmployeeWindow.getClose()) {

				//add employee to important stuff
				Profession prof=getLastEmployee();
				currentID=prof.getId();
				addEmployee(prof);
				actionChanged(event);

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addEmployee(Profession emp) {
		if (emp == null) {
			System.out.println("empwindow closed without saving");
			return;
		}
		System.out.println("\tid: "+emp.getId()+" "+emp.getActive());
		manageSelectEmployee.getItems().add(emp.getName());

		oldindex=ProgramDriver.getEmployees().size()-1;
		manageSelectEmployee.getSelectionModel().select(oldindex);

		System.out.println("email: "+emp.getEmail());

		manageEmployeeEmail.setText(emp.getEmail());
		//		manageActivityField.getSelectionModel().select(emp.getActive());


		manageDateRange1.setText("Set "+getAct(emp.getType())+" Date");
		currentID=emp.getId();

		if(emp.getActive()==2) {
			manageDateStart.setText(emp.getInactiveDate().toString());
		}

		showDateRange(null);

	}

	public void quit(ActionEvent event) {

		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}
		}
		Platform.exit();


	}

	public static boolean getEditWithoutSave() {
		return editedWithoutSave;
	}

}