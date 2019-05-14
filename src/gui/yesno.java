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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import classinfo.*;
import classinfo.Schedule;

public class yesno {
	@FXML
	Button yes,no;
	AnchorPane anchor;
	
	private static boolean save=false;

	@FXML
	public void initialize() {//when starts gui starts up, initializes all the needed variables
		yes.setText("Yes");
		no.setText("No");
	}

	public static boolean getClose() {return save;}

	public void actionAddEmployee(ActionEvent event) {
		quit(event);

		//actionQuit(event);
	}
	public void actionYes(ActionEvent event) {
		save=true;
		quit(event);
	}
	public void actionNo(ActionEvent event) {
		save=false;
		quit(event);
	}
	public void actionQuit(ActionEvent event) {
		actionNo(event);
	}

	public void quit(ActionEvent event) {
		Stage st=(Stage)yes.getScene().getWindow();
		st.close();
		//actionQuit(event);
	}
}
