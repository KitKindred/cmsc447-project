package gui;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
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
import java.time.*;

// TODO: Auto-generated Javadoc
/**
 * The Class TimeOffRequestModifier.
 */
public class TimeOffRequestModifier {

	/** The priority slider. */
	@FXML
	Slider prioritySlider;

	/** The cancel button. */
	@FXML
	Button saveButton, cancelButton;
	
	/** The To year. */
	@FXML
	ComboBox FromMonth,FromDay,ToMonth,ToDay,FromYear,ToYear;

	/** The req. */
	public static TimeOffRequest req=null;


	/** The disable. */
	private ArrayList<Node> disable;

	/** The from month max. */
	private int fromMonthMax=0;
	
	/** The to month max. */
	private int toMonthMax=0;
	
	/** The fmm. */
	private int fmm;
	
	/** The tmm. */
	private int tmm;
	
	
	
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		disable=new ArrayList<Node>();
		System.out.println("initializing");

		fmm=0;
		tmm=0;

		String months[]= {"January","February","March","April","May","June","July","August","September","October","November","December"};

		FromMonth.getItems().addAll(months);
		ToMonth.getItems().addAll(months);

		int y=Year.now().getValue();
		int plus=y+2;
		int minus=y-1;

		for(int i=minus;i<plus;i++) {
			FromYear.getItems().add(i);
			ToYear.getItems().add(i);
		}

		System.out.println("test");
		
		disable.add(saveButton);
		disable.add(FromMonth);
		disable.add(FromDay);
		disable.add(ToMonth);
		disable.add(ToDay);

		System.out.println("test2");
		for(Node n:disable) {
			
			
			n.setDisable(true);
		}
		System.out.println("test3");
		
		
		
		
		int sel=TimeOffRequestWindow.getSelection();
		TimeOffRequest t=TimeOffRequestWindow.tor.get(sel);
		
		System.out.println(t.getData()+"");
		

	}
	
	/**
	 * Update day.
	 *
	 * @param box the box
	 * @param days the days
	 */
	private void updateDay(ComboBox box,int days) {
		box.getItems().clear();
		for(int i=1;i<=days;i++) {
			box.getItems().add(i);
		}
	}

	/**
	 * Action select from month.
	 *
	 * @param event the event
	 */
	public void actionSelectFromMonth(ActionEvent event) {
		FromDay.getItems().clear();
		switch(FromMonth.getValue().toString()) {
		case "January":
			updateDay(FromDay,fromMonthMax=31);
			fmm=1;
			break;
		case "February":
			fmm=2;
			if(FromYear.getValue()!=null&&Integer.parseInt(FromYear.getValue().toString())%4==0) {
				System.out.println("LEAP YEAR!!");
				fromMonthMax=29;
			}
			else {
				fromMonthMax=28;
			}

			updateDay(FromDay,fromMonthMax);
			break;
		case "March":
			fmm=3;
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "April":
			fmm=4;
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "May":
			fmm=5;
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "June":
			fmm=6;
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "July":
			fmm=7;
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "August":
			fmm=8;
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "September":
			fmm=9;
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "October":
			fmm=10;
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "November":
			fmm=11;
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "December":
			fmm=12;
			updateDay(FromDay,fromMonthMax=31);
			break;
		default:
			fmm=-1;
			System.out.println("ERROR WITH ");
			break;
		}

		FromDay.setDisable(false);

	}
	
	/**
	 * Action select from day.
	 *
	 * @param event the event
	 */
	public void actionSelectFromDay(ActionEvent event) {

		ComboBox b = (ComboBox) event.getSource();
		if(b.getValue()==null) {b.getSelectionModel().clearSelection();return;}
		int num=Integer.parseInt(b.getValue().toString());
		if(num>fromMonthMax || num<=0) {
			b.getSelectionModel().clearSelection();
		}
		else {System.out.println("date ok");}
	}
	
	/**
	 * Action select to day.
	 *
	 * @param event the event
	 */
	public void actionSelectToDay(ActionEvent event) {

		ComboBox b = (ComboBox) event.getSource();
		if(b.getValue()==null) {b.getSelectionModel().clearSelection();return;}
		int num=Integer.parseInt(b.getValue().toString());
		if(num>toMonthMax || num<=0) {
			b.getSelectionModel().clearSelection();
		}
		else {System.out.println("date ok");}
		
		saveButton.setDisable(false);
	}
	
	/**
	 * Action select from year.
	 *
	 * @param event the event
	 */
	public void actionSelectFromYear(ActionEvent event) {
		System.out.println("\t"+FromDay.getItems().size());
		FromMonth.setDisable(false);
		if(FromMonth.getValue()==null) {return;}
		boolean feb=FromMonth.getValue().toString().equals("February");
		if(FromYear.getValue()!=null&&Integer.parseInt(FromYear.getValue().toString())%4==0) {
			if(feb) {
				if(FromDay.getItems().size()<=28) {
					System.out.println("asd\t"+FromDay.getSelectionModel().getSelectedIndex());
					fromMonthMax=29;
					FromDay.getItems().add(29);
				}
			}
		}
		else {
			if(feb) {
				if(FromDay.getItems().size()>=29) {
					System.out.println(FromDay.getItems().size());
					fromMonthMax=28;
					FromDay.getItems().remove(28);
				}
			}
		}
		
	}
	
	/**
	 * Action select to year.
	 *
	 * @param event the event
	 */
	public void actionSelectToYear(ActionEvent event) {
		System.out.println("\t"+ToDay.getItems().size());
		ToMonth.setDisable(false);
		if(ToMonth.getValue()==null) {return;}
		boolean feb=ToMonth.getValue().toString().equals("February");
		if(ToYear.getValue()!=null&&Integer.parseInt(ToYear.getValue().toString())%4==0) {
			if(feb) {
				if(ToDay.getItems().size()<=28) {
					System.out.println("asd\t"+ToDay.getSelectionModel().getSelectedIndex());
					toMonthMax=29;
					ToDay.getItems().add(29);
				}
			}
		}
		else {
			if(feb) {
				if(ToDay.getItems().size()>=29) {
					System.out.println(ToDay.getItems().size());
					toMonthMax=28;
					ToDay.getItems().remove(28);
				}
			}
		}
	}
	
	/**
	 * Action select to month.
	 *
	 * @param event the event
	 */
	public void actionSelectToMonth(ActionEvent event) {
		ToDay.getItems().clear();

		switch(ToMonth.getValue().toString()) {
		case "January":
			tmm=1;
			updateDay(ToDay,toMonthMax=31);
			break;
		case "February":		
			tmm=2;
			if(ToYear.getValue()!=null&&Integer.parseInt(ToYear.getValue().toString())%4==0) {System.out.println("LEAP YEAR!!");
			toMonthMax=29;}
			else {
				toMonthMax=28;
			}
			updateDay(ToDay,toMonthMax);
			break;
		case "March":
			tmm=3;
			updateDay(ToDay,toMonthMax=31);
			break;
		case "April":
			tmm=4;
			updateDay(ToDay,toMonthMax=30);
			break;
		case "May":
			tmm=5;
			updateDay(ToDay,toMonthMax=31);
			break;
		case "June":
			tmm=6;
			updateDay(ToDay,toMonthMax=30);
			break;
		case "July":
			tmm=7;
			updateDay(ToDay,toMonthMax=31);
			break;
		case "August":
			tmm=8;
			updateDay(ToDay,toMonthMax=31);
			break;
		case "September":
			tmm=9;
			updateDay(ToDay,toMonthMax=30);
			break;
		case "October":
			tmm=10;
			updateDay(ToDay,toMonthMax=31);
			break;
		case "November":
			tmm=11;
			updateDay(ToDay,toMonthMax=30);
			break;
		case "December":
			tmm=12;
			updateDay(ToDay,toMonthMax=31);
			break;
		default:
			System.out.println("ERROR");
			break;
		}

		ToDay.setDisable(false);

	}

	/**
	 * Convert date to int.
	 *
	 * @param ldt1 the ldt 1
	 * @param ldt2 the ldt 2
	 * @return the int
	 */
	private int convertDateToInt(LocalDateTime ldt1 , LocalDateTime ldt2){
		
		System.out.println("year: "+ldt1.minusYears(ldt2.getYear()));
		System.out.println("month: "+ldt1.minusMonths(ldt2.getMonthValue()));
		System.out.println("days: "+ldt1.minusDays(ldt2.getDayOfMonth()));
		
		return -1;
	}
	
	/**
	 * Action save.
	 *
	 * @param event the event
	 */
	public void actionSave(ActionEvent event) {
		int fy=Integer.parseInt(FromYear.getValue().toString());
		int fd=Integer.parseInt(FromDay.getValue().toString());
		int ty=Integer.parseInt(ToYear.getValue().toString());
		int td=Integer.parseInt(ToDay.getValue().toString());


		LocalDateTime from=LocalDateTime.of(fy, fmm, fd, 0, 0);
		LocalDateTime to=LocalDateTime.of(ty, tmm, td,0,0);
		
		Duration duration = Duration.between(from, to);
		long diff = Math.abs(duration.toDays());		
		
		Shift sh=new Shift(from, (int)diff);
	
		req= new TimeOffRequest(sh, (int)prioritySlider.getValue());
		
		System.out.println("save pressed");
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();

	}
	
	/**
	 * Action cancel.
	 *
	 * @param event the event
	 */
	public void actionCancel(ActionEvent event) {
		req=null;

		System.out.println("cancel pressed");
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
		
	}
	
	/**
	 * Quit.
	 *
	 * @param event the event
	 */
	public void quit(ActionEvent event) {actionCancel(event);
		
	}
	
	/**
	 * Close.
	 *
	 * @param event the event
	 */
	public void close(ActionEvent event) {
		System.out.println("close pressed");
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
	}
}
