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
import classinfo.*;
import classinfo.Schedule;
import java.time.*;

// TODO: Auto-generated Javadoc
/**
 * The Class TimeOffRequestWindow.
 */
public class TimeOffRequestWindow {

	/** The close button. */
	@FXML
	Button addTORButton, removeSelectButton,editSelectButton,closeButton;
	
	/** The To year. */
	@FXML
	ComboBox FromMonth,FromDay,ToMonth,ToDay,FromYear,ToYear;

	/** The Request box. */
	@FXML
	ListView RequestBox;

	/** The tor. */
	static ArrayList<TimeOffRequest> tor;
	
	/** The from month max. */
	private int fromMonthMax=0;
	
	/** The to month max. */
	private int toMonthMax=0;

	/** The changed. */
	static boolean changed=false;
	
	/** The selected. */
	private static int selected=-1;
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		tor=new ArrayList<TimeOffRequest>();
		Profession p=ProgramDriver.getEmployees().get(controller.getSelectedID());
		
		tor.addAll(p.getTimeOffRequests());
		
		for(TimeOffRequest t:tor)
			RequestBox.getItems().add(t.toString());
		
		selected=-1;
	}

	/**
	 * Action close time off window.
	 *
	 * @param event the event
	 */
	public void actionCloseTimeOffWindow(ActionEvent event) {
		
		System.out.println("close pressed");
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * Action add request.
	 *
	 * @param event the event
	 */
	public void actionAddRequest(ActionEvent event) {
		System.out.println("add req");
		try {
			
			String path="/gui/TimeOffRequestBuilder.fxml";
			System.out.println("add path "+path);
			Stage st = new Stage();
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource(path)));
			
			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Create New Time Off Request");
			
			st.setResizable(false);
			st.showAndWait();			
			
			if (TimeOffRequestBuilder.req != null) {
				tor.add(TimeOffRequestBuilder.req);
				RequestBox.getItems().add(TimeOffRequestBuilder.req.toString());
				changed=true;
			}
			
		}catch(Exception e) {
			System.out.println("ERROR SHOWING "+e.toString());
		}
	}
	
	/**
	 * Action remove selected request.
	 *
	 * @param event the event
	 */
	public void actionRemoveSelectedRequest(ActionEvent event) {
		int index=0;
		
		if(RequestBox.getItems().size()<=0) {
			return;
		}
		
		index = RequestBox.getSelectionModel().getSelectedIndex();
		if(index==-1) {
			System.out.println("cannot remove index -1");
			return;
		}
		RequestBox.getSelectionModel().clearSelection();
		RequestBox.getItems().remove(index);
		tor.remove(index);
		changed=true;
		
	}
	
	/**
	 * Gets the selection.
	 *
	 * @return the selection
	 */
	public static int getSelection() {return selected;}
	
	/**
	 * Action edit selected request.
	 *
	 * @param event the event
	 */
	public void actionEditSelectedRequest(ActionEvent event) {	
		selected=RequestBox.getSelectionModel().getSelectedIndex();
		if(selected==-1) {
			System.out.println("cannot select a non-selected index.");
			return;
		}
		
		try {
			String path="/gui/TimeOffRequestModifier.fxml";
			Stage st = new Stage();
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource(path)));

			st.setScene(scene);
			st.initModality(Modality.APPLICATION_MODAL);
			st.setTitle("Edit Time Off Request");
			
			st.setResizable(false);
			st.showAndWait();
			
			TimeOffRequest time=TimeOffRequestModifier.req;
			if (time != null) {
				tor.set(selected, time);
				
				RequestBox.getSelectionModel().clearSelection();
				RequestBox.getItems().remove(selected);
				RequestBox.getItems().add(selected, TimeOffRequestModifier.req);
				RequestBox.getSelectionModel().select(selected);
				
				changed=true;
			}
			
		}catch(Exception e) {
			System.out.println("ERROR SHOWING "+e.toString());
		}
	}
}
