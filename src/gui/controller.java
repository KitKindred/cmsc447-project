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

import com.sun.prism.paint.Color;

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

// TODO: Auto-generated Javadoc
/**
 * The Class controller.
 */
public class controller {

	/** The current selected date. */
	private static LocalDateTime currentSelectedDate=null;
	
	/** The quarter delete button. */
	@FXML
	Button quarterDateButton,quarterDeleteButton;

	/** The quarter date select. */
	@FXML
	ComboBox quarterDateSelect;

	//@FXML
	//TextField quarterDateTextField;

	/** The file new employee. */
	@FXML
	MenuItem fileNewEmployee;

	/** The ldts. */
	private static ArrayList<LocalDateTime> ldts;
	
	/** The newfile. */
	private boolean newfile=false;
	
	/**
	 * Gets the current date.
	 *
	 * @return the current date
	 */
	public static LocalDateTime getCurrentDate() {return currentSelectedDate;}
	
	/**
	 * Gets the formatted current date.
	 *
	 * @return the formatted current date
	 */
	public static String getFormattedCurrentDate() {
		DateTimeFormatter format=DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return currentSelectedDate.format(format);
	}

	/**
	 * Gets the file current date.
	 *
	 * @return the file current date
	 */
	public static String getFileCurrentDate() {
		DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
		return currentSelectedDate.format(format);
	}

	/**
	 * Quarter delete.
	 *
	 * @param event the event
	 */
	public void quarterDelete(ActionEvent event){
		int index=quarterDateSelect.getSelectionModel().getSelectedIndex();

		System.out.println(dateConversion.keySet());

		String fp="./src/sysfiles/profiles/";//+dateConversion.keySet().toArray()[index]+".txt";
		fp+=getFileCurrentDate()+".txt";

		try {
			if(IOFunctions.killFile(fp)<0) {return;}
			ldts.remove(index);

			editedWithoutSave=false;
			disableBeforeDate();

			dateConversion.remove(dateConversion.keySet().toArray()[index]);
			quarterDeleteButton.setDisable(true);

			quarterDateSelect.getSelectionModel().clearSelection();
			quarterDateSelect.getItems().remove(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		clearAll();
		ProgramDriver.reset();
	}

	/**
	 * Load dates.
	 */
	private void loadDates() {
		
		String fp=IOFunctions.path;//"./profiles";
		
		
		File f=new File(fp);
		if(!f.exists()) {
			f.mkdirs();
		}
		
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
			dateConversion.put(ldt.toString(), getFormattedCurrentDate());
			quarterDateSelect.getItems().add(getFormattedCurrentDate());//.split(".")[0]);
			currentSelectedDate=null;

		}
		System.out.println(f+" "+f.listFiles());

	}
	

	/** The date conversion. */
	private HashMap<String, String> dateConversion=new HashMap<String, String>();
	
	/**
	 * Quarter action.
	 *
	 * @param event the event
	 */
	public void quarterAction(ActionEvent event) {
		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}
		}

		LocalDateTime ldtAtt;
		try {			
			calendarDateStart.sendQuarters(dateConversion);
			
			String path="/gui/calendarDateStart.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Stage st = new Stage();
			Scene scene = new Scene(root);
			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Set Calendar Quarter Start Date");
			
			st.setResizable(false);
			st.showAndWait();





			if(calendarDateStart.saveDate) {

				if(calendarDateStart.req!=null) {
					ldtAtt=calendarDateStart.req;

					for(LocalDateTime local: ldts) {
						if(local.equals(ldtAtt)) {
							System.out.println("TEST: CALENDAR ALREADY MADE");
							return;
						}

					}

					calendarDateStart.req=null;

					System.out.println("BEFORE FORMAT: "+ldtAtt);
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					String formatted=ldtAtt.format(format);

					currentSelectedDate=ldtAtt;

					ldts.add(ldtAtt);
					System.out.println(formatted+" "+ldts);

					quarterDateSelect.getItems().add(formatted);


					dateConversion.put(ldtAtt.toString(),formatted);
					newfile=true;
					quarterDateSelect.getSelectionModel().select(ldts.size()-1);
					IOFunctions.saveEmployees();
				}
			}

			//quarterSelect(null);
		}catch(Exception e) {System.out.println("error?"+e.toString());}
	}
	
	/**
	 * Sets the current date.
	 *
	 * @param l the new current date
	 */
	public static void setCurrentDate(LocalDateTime l) {
		currentSelectedDate=l;
	}
	
	/**
	 * Quarter select.
	 *
	 * @param event the event
	 */
	public void quarterSelect(ActionEvent event) {
		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}
		}
		if(quarterDateSelect.getSelectionModel().getSelectedIndex()==-1) {return;}
		currentSelectedDate=ldts.get(quarterDateSelect.getSelectionModel().getSelectedIndex());
		enableAfterDate();
		quarterDeleteButton.setDisable(false);
		try {
			clearAll();
			if(newfile) {
				IOFunctions.saveEmployees();
				newfile=false;
			}
			else {
				IOFunctions.loadEmployees();
			}

			System.out.println(ProgramDriver.getEmployees());
			populate();
			System.out.println(ProgramDriver.getEmployees());

		} catch (IOException e) {
			System.out.println("error loading files");

			e.printStackTrace();
		}

	}

	/** *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~**. */

	private static boolean editedWithoutSave = false; 
	
	/** The is switching emps. */
	private static boolean isSwitchingEmps = false;

	/** The gen sched expo button. */
	//GENERATE SCHEDULE TAB SECTION VARIABLES 
	@FXML
	Button genSchedExpoButton;

	/** The delete button. */
	@FXML 
	Button manageSaveButton,createEmployeeButton,GetTimeOffButton,manageDateRange1,attendingWeekButton, deleteButton;


	/** The attending week text. */
	@FXML
	TextField manageEmployeeNameText,manageEmployeeEmail,manageDateStart,attendingWeekText;
	
	/** The manage select employee. */
	@FXML
	ComboBox manageActivityField, manageSelectEmployee;

	/** The email text. */
	@FXML
	Text manageEmployeeIDField,manageEmployeeProfession,activityText,attendText,maternityText,emailText;

	/** The me tab. */
	@FXML
	Tab meTab;

	/** The current ID. */
	static int currentID=-1;
	
	/** The disable until load. */
	private ArrayList<Node> invisSelectEmployee, invisDateRange, invisDoctor,disableUntilLoad;
	
	/** The disable before date. */
	private ArrayList disableBeforeDate;

	/** The oldindex. */
	private int oldindex=-1;


	/**
	 * Gets the last employee.
	 *
	 * @return the last employee
	 */
	private Profession getLastEmployee() {
		return (Profession)ProgramDriver.getEmployees().values().toArray()[ProgramDriver.getEmployees().size()-1];
	}
	/*
	private Profession getLastEmployee() {
		return (Profession)ProgramDriver.getEmployees().values().toArray()[ProgramDriver.getEmployees().size()-1];}
	 */

	/**
	 * Disable before date.
	 */
	private void disableBeforeDate() {
		meTab.setDisable(true);
		fileNewEmployee.setDisable(true);}

	/**
	 * Enable after date.
	 */
	private void enableAfterDate() {
		meTab.setDisable(false);
		fileNewEmployee.setDisable(false);
	}

	/**
	 * Blank spots.
	 */
	private void blankSpots() {
		for(Node a: invisDateRange) {a.setVisible(false);}
		for(Node a: invisSelectEmployee) {a.setVisible(false);}
		System.out.println("a"+invisDoctor);
		for(Node a: invisDoctor) {a.setVisible(false);}
	}

	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		ldts=new ArrayList<LocalDateTime>();

		loadDates();
		disableBeforeDate();

		invisSelectEmployee = new ArrayList<Node>();
		invisDateRange = new ArrayList<Node>();
		invisDoctor= new ArrayList<Node>();
		disableUntilLoad=new ArrayList<Node>();

		quarterDeleteButton.setDisable(true);

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

		deleteButton.setVisible(false);


		populate();

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
					if(newValue.indexOf("@")==-1) {

						manageEmployeeEmail.setStyle("-fx-control-inner-background: tomato");
					}
					else {
						manageEmployeeEmail.setStyle("-fx-control-inner-background: white;");
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
		manageSaveButton.setDisable(false);

	}

	/*GUI ACTION FUNCTIONS*/

	/**
	 * Action changed.
	 *
	 * @param event the event
	 */
	public void actionChanged(ActionEvent event) {
		if (!isSwitchingEmps) {
			System.out.println("Changed is now true");
			editedWithoutSave=true;
		}
	}

	/**
	 * Clear all.
	 */
	private void clearAll() {
		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().clear();
		blankSpots();
		oldindex=-1;
		currentID=-1;

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


	/**
	 * Action launch attending window.
	 *
	 * @param event the event
	 */
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

	/**
	 * Change box.
	 *
	 * @return true, if successful
	 */
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


	/**
	 * Generate.
	 *
	 * @param event the event
	 */
	//will generate schedule
	public void generate(ActionEvent event) {
		System.out.println("ATTEMPTING TO GEN SCHEDULE");

		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}	
		}

		LocalDate d1 = currentSelectedDate.toLocalDate();
		LocalDate d2 = d1.plusMonths(3);
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

	/**
	 * Check ID.
	 *
	 * @param event the event
	 */
	//the currently selected employee
	public void checkID(ActionEvent event) {

		System.out.println("checkID: emp selected: ");
		String name="Example Name";
		String email="Example Email";
		int active = 0;
		int index=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		if(index==-1) {return;}
		if (manageSelectEmployee.getValue() == null) {return;}

		System.out.println("oldindex: "+oldindex);
		isSwitchingEmps = true;
		System.out.println("test");


		Profession Pro=ProgramDriver.getEmployees().get(ProgramDriver.getNameID().get(index));
		System.out.println(Pro);

		name = manageSelectEmployee.getValue().toString();

		ArrayList<Integer> nameID=ProgramDriver.getNameID();
		//currentID=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		currentID=nameID.get(manageSelectEmployee.getSelectionModel().getSelectedIndex());
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

	/**
	 * Gets the act.
	 *
	 * @param act the act
	 * @return the act
	 */
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
	
	/**
	 * Gets the prof.
	 *
	 * @param type the type
	 * @return the prof
	 */
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


	/**
	 * Save employee.
	 *
	 * @param event the event
	 */
	public void saveEmployee(ActionEvent event) {
		if(currentID==-1) {
			try {
				IOFunctions.saveEmployees();
				editedWithoutSave = false;
				System.out.println("\nSaving when no one is shown on screen");
				return;
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return;
			}
		}

		//if(currentID==-1) {return;}


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


		ProgramDriver.getNameID().get(oldindex);//.put(newName,id);
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
			manageSelectEmployee.getItems().add(id, worker.getId()+": "+newName);
			manageSelectEmployee.getSelectionModel().select(id);

		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}		

	}

	/**
	 * Delete employee.
	 *
	 * @param event the event
	 */
	// TODO: implement
	public void deleteEmployee(ActionEvent event) {
		System.out.println("delete pressed");

		int id;
		int index;
		String name=manageSelectEmployee.getSelectionModel().getSelectedItem().toString();
		//HashMap<String, Integer> nameID=ProgramDriver.getNameID();
		ArrayList<Integer> nameID=ProgramDriver.getNameID();

		index=manageSelectEmployee.getSelectionModel().getSelectedIndex();
		id=nameID.get(index);

		manageSelectEmployee.getSelectionModel().clearSelection();
		manageSelectEmployee.getItems().remove(index);

		ProgramDriver.getEmployees().remove(id);
		ProgramDriver.getNameID().remove(index);
		if(ProgramDriver.getActiveID().contains(id)) {ProgramDriver.getActiveID().remove(id);}

		editedWithoutSave=true;
		currentID=-1;

		for(Node a:invisSelectEmployee) {a.setVisible(false);}
		for(Node a:invisDoctor) {a.setVisible(false);}
		for(Node a:invisDateRange) {a.setVisible(false);}
		deleteButton.setVisible(false);
	}


	/**
	 * Show date range.
	 *
	 * @param event the event
	 */
	//when certain Active combobox option is selected, either enables or disables view of the daterange that would be used beside it
	public void showDateRange(ActionEvent event) {
		String op = manageActivityField.getValue().toString();
		System.out.println(manageActivityField.getValue().toString());
		manageDateRange1.setText("Set "+op+" Date");
		int index=manageActivityField.getSelectionModel().getSelectedIndex();


		Profession prof=null;//=ProgramDriver.getEmployees().get(currentID);

		if(currentID!=-1) {

			prof=ProgramDriver.getEmployees().get(currentID);
			prof.setActive(manageActivityField.getSelectionModel().getSelectedIndex());

		}
		switch(op) {
		case "Active":
			for(Node a: invisDateRange) {
				a.setVisible(false);
			}
			if(currentID!=-1) {
				if(prof.getType()==0) {
					for(Node a:invisDoctor) {
						a.setVisible(true);
					}
				}
			}
			//			System.out.println("CURRENT ID: "+currentID);
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
			//	System.out.println("CURRENT ID: "+currentID);
			ProgramDriver.getEmployees().get(currentID).setActive(index);
			ProgramDriver.getEmployees().get(currentID).setInactiveDate(null);
			break;
		default:
			for(Node a: invisDateRange) {
				a.setVisible(true);
			}
			if(currentID!=-1) {
				if(prof.getType()==0) {
					for(Node a:invisDoctor) {
						a.setVisible(true);
					}
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

	/**
	 * Populate.
	 */
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
			manageSelectEmployee.getItems().add(entry.getValue().getId()+": "+name);
		}
	}

	/**
	 * Gets the selected ID.
	 *
	 * @return the selected ID
	 */
	public static int getSelectedID() {
		System.out.println("Selected ID may be: "+currentID);
		return currentID;}



	/**
	 * Action select time off.
	 *
	 * @param event the event
	 */
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

	/**
	 * Action launch employee creation window.
	 *
	 * @param event the event
	 */
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

	/**
	 * Adds the employee.
	 *
	 * @param emp the emp
	 */
	public void addEmployee(Profession emp) {
		if (emp == null) {
			System.out.println("empwindow closed without saving");
			return;
		}
		System.out.println("\tid: "+emp.getId()+" "+emp.getActive());
		manageSelectEmployee.getItems().add(emp.getId()+": "+emp.getName());

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


		//		manageActivityField.getSelectionModel().select(emp.getActive());


		manageDateRange1.setText("Set "+getAct(emp.getType())+" Date");
		currentID=emp.getId();

		if(emp.getActive()==2) {
			manageDateStart.setText(emp.getInactiveDate().toString());
		}

		showDateRange(null);


	}

	/**
	 * Quit.
	 *
	 * @param event the event
	 */
	public void quit(ActionEvent event) {

		if(editedWithoutSave) {
			if(changeBox()) {
				saveEmployee(event);
			}
		}
		Platform.exit();


	}

	/**
	 * Gets the edits the without save.
	 *
	 * @return the edits the without save
	 */
	public static boolean getEditWithoutSave() {
		return editedWithoutSave;
	}

}