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
import java.time.*;

public class TimeOffRequestWindow {

	@FXML
	Button addTORButton, removeSelectButton,editSelectButton;
	@FXML
	ComboBox FromMonth,FromDay,ToMonth,ToDay,FromYear,ToYear;

	@FXML
	ListView RequestBox;

	private int fromMonthMax=0;
	private int toMonthMax=0;

	public void initialize() {
		FromDay.setDisable(true);
		ToDay.setDisable(true);

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

	}
	private void updateDay(ComboBox box,int days) {
		box.getItems().clear();
		for(int i=1;i<=days;i++) {
			box.getItems().add(i);
		}
	}

	public void actionSelectFromMonth(ActionEvent event) {
		FromDay.getItems().clear();

		switch(FromMonth.getValue().toString()) {
		case "January":
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "February":

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
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "April":
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "May":
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "June":
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "July":
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "August":
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "September":
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "October":
			updateDay(FromDay,fromMonthMax=31);
			break;
		case "November":
			updateDay(FromDay,fromMonthMax=30);
			break;
		case "December":
			updateDay(FromDay,fromMonthMax=31);
			break;
		default:
			System.out.println("ERROR WITH ");
			break;
		}

		FromDay.setDisable(false);



	}
	public void actionSelectFromDay(ActionEvent event) {

		ComboBox b = (ComboBox) event.getSource();
		if(b.getValue()==null) {b.getSelectionModel().clearSelection();return;}
		int num=Integer.parseInt(b.getValue().toString());
		if(num>fromMonthMax || num<=0) {
			b.getSelectionModel().clearSelection();
		}
		else {System.out.println("date ok");}
	}
	public void actionSelectToDay(ActionEvent event) {

		ComboBox b = (ComboBox) event.getSource();
		int num=Integer.parseInt(b.getValue().toString());
		if(num>toMonthMax || num<=0) {
			b.getSelectionModel().clearSelection();
		}
		else {System.out.println("date ok");}
	}
	public void actionSelectFromYear(ActionEvent event) {
		System.out.println("\t"+FromDay.getItems().size());
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
	public void actionSelectToYear(ActionEvent event) {
		System.out.println("\t"+ToDay.getItems().size());
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
	public void actionSelectToMonth(ActionEvent event) {
		ToDay.getItems().clear();

		switch(ToMonth.getValue().toString()) {
		case "January":
			updateDay(ToDay,toMonthMax=31);
			break;
		case "February":			
			if(ToYear.getValue()!=null&&Integer.parseInt(ToYear.getValue().toString())%4==0) {System.out.println("LEAP YEAR!!");
			toMonthMax=29;}
			else {
				toMonthMax=28;
			}
			updateDay(ToDay,toMonthMax);
			break;
		case "March":
			updateDay(ToDay,toMonthMax=31);
			break;
		case "April":
			updateDay(ToDay,toMonthMax=30);
			break;
		case "May":
			updateDay(ToDay,toMonthMax=31);
			break;
		case "June":
			updateDay(ToDay,toMonthMax=30);
			break;
		case "July":
			updateDay(ToDay,toMonthMax=31);
			break;
		case "August":
			updateDay(ToDay,toMonthMax=31);
			break;
		case "September":
			updateDay(ToDay,toMonthMax=30);
			break;
		case "October":
			updateDay(ToDay,toMonthMax=31);
			break;
		case "November":
			updateDay(ToDay,toMonthMax=30);
			break;
		case "December":
			updateDay(ToDay,toMonthMax=31);
			break;
		default:
			System.out.println("ERROR");
			break;
		}

		ToDay.setDisable(false);

	}

	public void actionAddRequest(ActionEvent event) {}
	public void actionRemoveSelectedRequest(ActionEvent event) {}
	public void actionEditSelectedRequest(ActionEvent event) {}

}
