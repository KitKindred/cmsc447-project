package gui;
import java.net.URL;
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

import java.time.*;

public class dateStart {

	@FXML
	Button saveButton, cancelButton;
	@FXML
	ComboBox FromMonth,FromDay,FromYear;

	public static boolean saveDate=false;
	public static LocalDateTime req=null;


	private ArrayList<Node> disable;

	private int fromMonthMax=0;
	private int fmm;
	public void initialize() {
		disable=new ArrayList<Node>();
		System.out.println("initializing");

		fmm=0;


		String months[]= {"January","February","March","April","May","June","July","August","September","October","November","December"};

		FromMonth.getItems().addAll(months);

		int y=Year.now().getValue();
		int plus=y+1;
		int minus=y-1;

		for(int i=minus;i<plus;i++) {
			FromYear.getItems().add(i);
		}

		System.out.println("test");
		
		disable.add(saveButton);
		disable.add(FromMonth);
		disable.add(FromDay);
		
		for(Node n:disable) {		
			n.setDisable(true);
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
	public void actionSelectFromDay(ActionEvent event) {

		ComboBox b = (ComboBox) event.getSource();
		if(b.getValue()==null) {b.getSelectionModel().clearSelection();return;}
		int num=Integer.parseInt(b.getValue().toString());
		if(num>fromMonthMax || num<=0) {
			b.getSelectionModel().clearSelection();
		}
		else {System.out.println("date ok");}
		saveButton.setDisable(false);
	}

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

	public void actionSave(ActionEvent event) {
		int fy=Integer.parseInt(FromYear.getValue().toString());
		int fd=Integer.parseInt(FromDay.getValue().toString());

		saveDate=true;
		
		LocalDateTime from=LocalDateTime.of(fy, fmm, fd, 0, 0);
		System.out.println("from: "+from);
		
		req=from;
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();

	}
	public void actionCancel(ActionEvent event) {
		req=null;
		saveDate=false;
		System.out.println("cancel pressed");
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
	    
		
		
		
	}
	public void quit(ActionEvent event) {
		actionCancel(event);
	}
	public void close(ActionEvent event) {
		System.out.println("close pressed");
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
	}
}
