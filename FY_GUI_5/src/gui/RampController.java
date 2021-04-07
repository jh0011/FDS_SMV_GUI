package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import connectivity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class for Ramp.fxml
 * 
 */
public class RampController implements Initializable{
	
	
	//ramp
	@FXML TextField fractionText; //float (+ / -)
    @FXML TextField timeText; //float (+)
    @FXML TextField rampIdText; //string
    
    //ctrl
    @FXML TextField inputIdText;
    @FXML TextField ctrlRampText;
    @FXML TextField ctrlIdText;
    @FXML ComboBox latchCombo;
    @FXML ComboBox functionCombo;
    
    //reac
    @FXML TextField ignitTempText; //float (+ / -)
    @FXML TextField sootText; //float (+)
    @FXML TextField fuelText; //string

    @FXML Button addRampBtn;
    @FXML Button addCtrlBtn;
    @FXML Button addReacBtn;
    
    boolean checkFloatPos;
    boolean checkFloat;
    boolean checkFloatReac;
    boolean checkFloatPosReac;
    
    static String latchSelection = "";
    static String functionSelection = "";
    static int mainRampId = 1;
    static int mainCtrlId = 1;
    static int mainReacId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip rampTooltip = new Tooltip("Click to add another RAMP field.");
		addRampBtn.setTooltip(rampTooltip);
		Tooltip ctrlTooltip = new Tooltip("Click to add another CTRL field.");
		addCtrlBtn.setTooltip(ctrlTooltip);
		Tooltip reacTooltip = new Tooltip("Click to add another REAC field.");
		addReacBtn.setTooltip(reacTooltip);
		
		ObservableList<String> latchList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		latchCombo.setItems(latchList);
		
		ObservableList<String> functionList = FXCollections.observableArrayList("", "ANY", "ALL", "ONLY", "AT_LEAST", "TIME_DELAY", "CUSTOM", "DEADBAND", "KILL", "RESTART", "SUM", 
				"SUBTRACT", "MULTIPLY", "DIVIDE", "POWER", "EXP", "LOG", "COS", "SIN", "ACOS", "ASIN", "MAX", "MIN", "PID");
		functionCombo.setItems(functionList);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException If database access error
	 * @throws IOException
	 */
	@FXML
	public void cancelOption(ActionEvent event) throws SQLException, IOException { //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
    }

	/**
	 * Go to the previous page (SURF) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException If database access error
	 */
    @FXML
    public void goToSurf(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if(checkFloatPos && checkFloat && checkFloatReac && checkFloatPosReac) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Surf.fxml"));
	    		Parent root = loader.load();
	    		
	    		SurfController surfCont = loader.getController(); //Get the next page's controller
	    		surfCont.showInfo(); //Set the values of the page 
	    		Scene surfScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(surfScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    	
    }
    
    /**
	 * Go to the next page (OBST) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException If database access error
	 */
    @FXML
    public void goToObst(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if(checkFloatPos && checkFloat && checkFloatReac && checkFloatPosReac) {
	    		//store the values
	    		storeValues();
	    	
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Obst.fxml"));
				Parent root = loader.load();
				
				ObstController obstCont = loader.getController(); //Get the next page's controller
				obstCont.showInfo(); //Set the values of the page 
				Scene obstScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(obstScene);
				mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Add a new line for RAMP namelist
	 * @param event The add button is clicked
	 * @throws SQLException If database access error
	 */
    @FXML
    public void newRampLine(ActionEvent event) throws SQLException { //ADD NEW RAMP LINE
    	doCheckingRamp();
    	
    	if(checkFloatPos && checkFloat) {
    		//store the values 
    		storeValuesRamp();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("RAMP", true);
    		
    		mainRampId++;
        	String mainRampIdString = Integer.toString(mainRampId);
        	String sqlRamp = "INSERT INTO ramp (mainID, FRACTION, TIME, ID) VALUES ('" + mainRampIdString + "', '', '', '');";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement = connection.createStatement();
    		statement.executeUpdate(sqlRamp);
    		
    		showInfoRamp();
    	}
    	else {
    		//confirmation message for failure
			Values.printConfirmationMessage("RAMP", false);
    		//System.out.println("Unable to add new RAMP line");
    	}
    	
    }
    
    /**
	 * Add a new line for CTRL namelist
	 * @param event The add button is clicked
	 * @throws SQLException If database access error
	 */
    @FXML
    public void newCtrlLine(ActionEvent event) throws SQLException { //ADD NEW CTRL LINE
    	//no checking required
    	
    	//store the values
    	storeValuesCtrl();
    	
    	//confirmation message for success
		Values.printConfirmationMessage("CTRL", true);
    	
    	mainCtrlId++;
    	String mainCtrlIdString = Integer.toString(mainCtrlId);
    	String sqlCtrl = "INSERT INTO ctrl (mainID, INPUT_ID, RAMP_ID, ID, LATCH, FUNCTION_TYPE) VALUES ('" + mainCtrlIdString + "', '', '', '', '', '');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlCtrl);
		
		showInfoCtrl();
    }
    
    /**
	 * Add a new line for REAC namelist
	 * @param event The add button is clicked
	 * @throws SQLException If database access error
	 */
    @FXML
    public void newReacLine(ActionEvent event) throws SQLException { //ADD NEW REAC LINE
    	doCheckingReac();
    	
    	if(checkFloatReac && checkFloatPosReac) {
    		//store the values
    		storeValuesReac();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("REAC", true);
			
    		mainReacId++;
    		String mainReacIdString = Integer.toString(mainReacId);
    		String sqlReac = "INSERT INTO reac (mainID, AUTO_IGNITION_TEMPERATURE, SOOT_YIELD, FUEL) VALUES ('" + mainReacIdString + "', '', '', '');";
    		ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement = connection.createStatement();
    		statement.executeUpdate(sqlReac);
    		
    		showInfoReac();
    	}
    	else {
    		//confirmation message for failure
			Values.printConfirmationMessage("REAC", false);
    		//System.out.println("Unable to add new REAC line");
    	}

    }
    
    /**
	 * Description of RAMP namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openRampDesc(MouseEvent event) {
    	String content = "The RAMP namelist allows you to specify a function with one independent variable (such as time) and one dependent variable (such as velocity).\n\n"
    			+ "Fraction: F indicates the fraction of the heat release rate, wall temperature, velocity, mass fraction, etc., to apply.";
    	String namelist = "RAMP";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Description of CTRL namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openCtrlDesc(MouseEvent event) {
    	String content = "The CTRL (advanced control functions) namelist can be used to define the more complicated behaviors. \n\n"
    			+ "Latch: If this logical value is set to TRUE the control function will only change state once. The default "
    			+ "value is TRUE.\n\n"
    			+ "Input_ID: A list of DEVC or CTRL IDs that are the inputs to the control function.\n\n"
    			+ "Function_type: The type of control function.";
    	String namelist = "CTRL";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Description of REAC namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openReacDesc(MouseEvent event) {
    	String content = "The REAC namelist can be used to specify parameters about a reaction. If you are modeling a fire, you should "
    			+ "specify the fuel and basic stoichiometry using a REAC line. \n\n"
    			+ "Fuel: A character string that identifies fuel species for the reaction. When using simple chemistry, "
    			+ "specifying FUEL will cause FDS to use the built-in thermophysical properties for that species "
    			+ "when computing quantities such as specific heat or viscosity.\n\n"
    			+ "Soot yield: The fraction of fuel mass converted into smoke particulate. Note that this parameter is "
    			+ "only appropriate when the simple chemistry model is applied.\n\n"
    			+ "Auto ignition temperature: To prevent spurious re-ignition from happening, you can set the "
    			+ "AUTO_IGNITION_TEMPERATURE on the REAC line or the COMB line, in °C, below which combustion "
    			+ "will not occur.";
    	String namelist = "REAC";
		Values.openDesc(namelist, content);
    }

    @FXML
    public void latchSelect(ActionEvent event) {
    	latchSelection = latchCombo.getSelectionModel().getSelectedItem().toString();
    	latchCombo.setValue(latchSelection);
    }
    
    @FXML
    public void functionSelect(ActionEvent event) {
    	functionSelection = functionCombo.getSelectionModel().getSelectedItem().toString();
    	functionCombo.setValue(functionSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingRamp();
    	doCheckingReac();
    }
    
    public void doCheckingRamp() {
    	checkFloatPos = true;
    	checkFloat = true;
    	
    	if(!timeText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkFloatPosValues(timeText);
    	}
    	if(!fractionText.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(fractionText);
    	}
    }
    
    public void doCheckingReac() {
    	checkFloatReac = true;
    	checkFloatPosReac = true;
    	if(!ignitTempText.getText().equals("")) {
    		checkFloatReac = checkFloatReac && checkFloatValues(ignitTempText);
    	}
    	if(!sootText.getText().equals("")) {
    		checkFloatPosReac = checkFloatPosReac && checkFloatPosValues(sootText);
    	}
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	String param = "Time and soot yield";
    	return Values.checkPosFloatValues(param, tempField);
    	
    }
    
    /**
	 * Check if the value is a float
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatValues(TextField tempField) { //check if the value is a float
    	String param = "Fraction and auto ignition temperature";
    	return Values.checkFloatValues(param, tempField);
    	
    }
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException If database access error
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesRamp();
    	storeValuesCtrl();
    	storeValuesReac();
    }
    
    public void storeValuesRamp() throws SQLException { //store RAMP values into the database
    	String mainRampIdString = Integer.toString(mainRampId);
    	String sqlRamp = "INSERT INTO ramp VALUES ('" + mainRampIdString + "', '" + fractionText.getText() + "', '" + timeText.getText() + "', '" + rampIdText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlRamp);
    }
    
    public void storeValuesCtrl() throws SQLException { //store CTRL values into the database
    	String mainCtrlIdString = Integer.toString(mainCtrlId);
    	String sqlCtrl = "INSERT INTO ctrl VALUES ('" + mainCtrlIdString + "', '" + inputIdText.getText() + "', '" + ctrlRampText.getText() + "', '" + 
    			ctrlIdText.getText() + "', '" + latchSelection + "', '" + functionSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlCtrl);
    }
    
    public void storeValuesReac() throws SQLException { //store REAC values into the database
    	String mainReacIdString = Integer.toString(mainReacId);
    	String sqlReac = "INSERT INTO reac VALUES ('" + mainReacIdString + "', '" + ignitTempText.getText() + "', '" + sootText.getText() + "', '" + fuelText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlReac);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException If database access error
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoRamp();
    	showInfoCtrl();
    	showInfoReac();
    }
    
    public void showInfoRamp() throws SQLException { //to show the info when the page is loaded
    	String sqlRamp = "SELECT * FROM ramp";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlRamp);
		while (rs.next()) {
			fractionText.setText(rs.getString(2));
			timeText.setText(rs.getString(3));
			rampIdText.setText(rs.getString(4));
		}
    }
    
    public void showInfoCtrl() throws SQLException { //to show the info when the page is loaded
    	String sqlCtrl = "SELECT * FROM ctrl";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlCtrl);
		while (rs.next()) {
			inputIdText.setText(rs.getString(2));
			ctrlRampText.setText(rs.getString(3));
			ctrlIdText.setText(rs.getString(4));
			latchSelection = rs.getString(5);
			latchCombo.setValue(latchSelection);
			functionSelection = rs.getString(6);
			functionCombo.setValue(functionSelection);
		}
    }
    
    public void showInfoReac() throws SQLException { //to show the info when the page is loaded
    	String sqlReac = "SELECT * FROM reac";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlReac);
		while (rs.next()) {
			ignitTempText.setText(rs.getString(2));
			sootText.setText(rs.getString(3));
			fuelText.setText(rs.getString(4));
		}
    }
}
