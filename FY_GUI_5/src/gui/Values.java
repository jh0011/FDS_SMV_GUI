package gui;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Values {
	
	//Store all the values that the user has initialised
	//ALl are static variables.
	
	//HEAD 
	protected static String chid = "";
	protected static String title = "";
	
	//TIME
	protected static String T_END = "";
	protected static String DT = "";
	protected static String DT_END_FILL = "";
	protected static String DT_END_MINIMUM = "";
	protected static String EVAC_DT_FLOWFIELD = "";
	protected static String EVAC_DT_STEADY_STATE = "";
	protected static String LIMITING_DT_RATIO = "";
	protected static String LOCK_TIME_STEP = "";
	protected static String RESTRICT_TIME_STEP = "";
	protected static String T_BEGIN = "";
	protected static String TIME_SHRINK_FACTOR = "";
	protected static String WALL_INCREMENT = "";
	protected static String WALL_INCREMENT_HT3D = "";
	
	//CATF
	protected static String files = "";
	
	
	protected static void cancelForm(){
		chid = "";
		title = "";
		T_END = "";
		DT = "";
		DT_END_FILL = "";
		DT_END_MINIMUM = "";
		EVAC_DT_FLOWFIELD = "";
		EVAC_DT_STEADY_STATE = "";
		LIMITING_DT_RATIO = "";
		LOCK_TIME_STEP = "";
		RESTRICT_TIME_STEP = "";
		T_BEGIN = "";
		TIME_SHRINK_FACTOR = "";
		WALL_INCREMENT = "";
		WALL_INCREMENT_HT3D = "";
		files = "";
	}
	
	protected static boolean cancelWarning(){
		Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
		cancelAlert.setTitle("Cancel");
		cancelAlert.setContentText("Clicking on OK would cancel the creation of the input file. Would you like to cancel?");
		cancelAlert.setHeaderText(null);
		Optional<ButtonType> userOption = cancelAlert.showAndWait();
		if (userOption.get() == ButtonType.OK){
			return true;
		}
		else{
			return false;
		}
	}
	
}
