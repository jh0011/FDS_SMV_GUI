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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class for Dump.fxml
 * 
 */
public class DumpController implements Initializable{
	
	
	//dump
	@FXML ComboBox massCombo;
    @FXML ComboBox smokeCombo;
    @FXML TextField framesText; //int (+)
    @FXML TextField dtDevcText; //float (+)
    
    //matl
    @FXML TextField specificText; //float (+)
    @FXML TextField reactionText; //float (+)
    @FXML TextField specIdText; //string
    @FXML TextField idText; //string
    @FXML TextField referenceText; //float (+)
    @FXML TextField reactionsText; //int (+)
    @FXML TextField densityText; //float (+)
    @FXML TextField conductivityText; //float (+)

    @FXML Button addMatlBtn;
    
    boolean checkIntPos;
    boolean checkFloatPos;
    
    boolean checkIntPosMatl;
    boolean checkFloatPosMatl;
    
    static String massSelection = "";
    static String smokeSelection = "";
    static int mainMatlId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip matlTooltip = new Tooltip("Click to add another MATL field.");
		addMatlBtn.setTooltip(matlTooltip);
		
		ObservableList<String> massList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		massCombo.setItems(massList);
		
		ObservableList<String> smokeList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		smokeCombo.setItems(smokeList);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException If database access error
	 * @throws IOException If cannot display the page
	 */
	@FXML
	public void cancelOption(ActionEvent event) throws IOException, SQLException { //CANCEL
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
	 * Go to the previous page (OBST) + input validation
	 * @param event Back button is clicked
	 * @throws IOException If cannot display the page
	 * @throws SQLException If database access error
	 */
    @FXML
    public void goToObst(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if(checkIntPos && checkFloatPos && checkIntPosMatl && checkFloatPosMatl) {
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
    	} catch(Exception e) {
			Values.showError();
		}
    	
    }
    
    /**
	 * Go to the next page (MULT) + input validation
	 * @param event Next button is clicked
	 * @throws IOException If cannot display the page
	 * @throws SQLException If database access error
	 */
    @FXML
    public void goToMult(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if(checkIntPos && checkFloatPos && checkIntPosMatl && checkFloatPosMatl) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mult.fxml"));
	    		Parent root = loader.load();
	    		
	    		MultController multCont = loader.getController(); //Get the next page's controller
	    		multCont.showInfo(); //Set the values of the page 
	    		Scene multScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(multScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Add a new line for MATL namelist
	 * @param event The add button is clicked
	 * @throws SQLException If database access error
	 */
    @FXML
    public void newMatlLine(ActionEvent event) throws SQLException { //ADD NEW MATL LINE
    	doCheckingMatl();
    	
    	if(checkIntPosMatl && checkFloatPosMatl) {
    		//store the values
    		storeValuesMatl();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("MATL", true);
    		
    		mainMatlId++;
        	String mainMatlIdString = Integer.toString(mainMatlId);
        	String sqlMatl = "INSERT INTO matl (mainID, SPECIFIC_HEAT, HEAT_OF_REACTION, SPEC_ID, ID, REFERENCE_TEMPERATURE, N_REACTIONS, DENSITY, CONDUCTIVITY) "
    				+ "VALUES ('" + mainMatlIdString + "', '', '', '', '', '', '', '', '')";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement = connection.createStatement();
    		statement.executeUpdate(sqlMatl);
    		
    		showInfoMatl();
    	}
    	else {
    		//confirmation message for failure
			Values.printConfirmationMessage("MATL", false);
    		//System.out.println("Unable to add new MATL line");
    	}
    }
    
    /**
	 * Description of DUMP namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openDumpDesc(MouseEvent event) {
    	String content = "The DUMP namelist parameters control the rate at which output files are written, and various other "
    			+ "global parameters associated with output files. \n\n"
    			+ "Dt_devc: Device data.\n\n"
    			+ "Mass_file: If TRUE, produce an output file listing the total masses of all gas species as a function of "
    			+ "time. It is FALSE by default because the calculation of all gas species in all mesh cells is timeconsuming.\n\n"
    			+ "Nframes: Number of output dumps per calculation. The default is 1000.\n\n"
    			+ "Smoke_3D: If FALSE, do not produce an animation of the smoke and fire. It is TRUE by default.";
    	String namelist = "DUMP";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Description of MATL namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openMatlDesc(MouseEvent event) {
    	String content = "The MATL namelist is used to define the properties of the materials that make up boundary solid surfaces.\n\n"
    			+ "Conductivity: For any solid material, specify its thermal CONDUCTIVITY (W/(m.K)), DENSITY (kg/m3), SPECIFIC_HEAT (kJ/(kg.K))\n\n"
    			+ "Density: For any solid material, specify its thermal CONDUCTIVITY (W/(m.K)), DENSITY (kg/m3), SPECIFIC_HEAT (kJ/(kg.K))\n\n"
    			+ "Specific_heat: For any solid material, specify its thermal CONDUCTIVITY (W/(m.K)), DENSITY (kg/m3), SPECIFIC_HEAT (kJ/(kg.K))\n\n"
    			+ "Heat of reaction: The amount of energy consumed, per unit mass of reactant that is converted into reaction products, in units of kJ/kg.\n\n"
    			+ "Reference tmp: It is the temperature at which the mass fraction of the material decreases at its maximum rate within the context of "
    			+ "a thermogravimetric analysis (TGA) or similar experimental apparatus.\n\n"
    			+ "N_reactions: Number of reactions.";
    	String namelist = "MATL";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Set the value chosen for mass_file
	 * @param event A value is chosen for mass_file
	 */
    @FXML
    public void massSelect(ActionEvent event) {
    	massSelection = massCombo.getSelectionModel().getSelectedItem().toString();
    	massCombo.setValue(massSelection);
    }

    /**
	 * Set the value chosen for smoke_3D
	 * @param event A value is chosen for smoke_3D
	 */
    @FXML
    public void smokeSelect(ActionEvent event) {
    	smokeSelection = smokeCombo.getSelectionModel().getSelectedItem().toString();
    	smokeCombo.setValue(smokeSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingDump();
    	doCheckingMatl();
    }
    
    /**
     * Checking for the DUMP name list
     */
    public void doCheckingDump() {
    	checkIntPos = true;
    	checkFloatPos = true;
    	if(!framesText.getText().equals("")) {
    		checkIntPos = checkIntPos && checkPosIntValues(framesText);
    	}
    	if(!dtDevcText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkPosFloatValues(dtDevcText);
    	}
    }
    
    /**
     * Checking for the MATL name list
     */
    public void doCheckingMatl() {
    	checkIntPosMatl = true;
    	checkFloatPosMatl = true;
    	if(!reactionsText.getText().equals("")) {
    		checkIntPosMatl = checkIntPosMatl && checkPosIntValues(reactionsText);
    	}
    	if(!specificText.getText().equals("")) {
    		checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(specificText);
    	}
		if(!reactionText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(reactionText);		
    	}
		if(!referenceText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(referenceText);
		}
		if(!densityText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(densityText);
		}
		if(!conductivityText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(conductivityText);
		}
    }
    
    /**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPosIntValues(TextField tempField) { //check if integer is positive
    	String param = "Nframes and N_reactions";
    	return Values.checkPosIntValues(param, tempField);
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPosFloatValues(TextField tempField) { //check if float is positive
    	String param = "Dt_devc, Specific_heat, heat of reaction, reference tmp., density and conductivity";
    	return Values.checkPosFloatValues(param, tempField);
    }
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException If database access error
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesDump();
    	storeValuesMatl();
    }

    /**
     * Store the DUMP line and its values into the database
     * @throws SQLException If database access error
     */
    public void storeValuesDump() throws SQLException{ //store DUMP values into the database
    	String sqlDump = "INSERT INTO dump VALUES ('" + massSelection + "', '" + smokeSelection + "', '" + framesText.getText() + "', '" + dtDevcText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlDump);
    }
    
    /**
     * Store the MATL line and its values into the database
     * @throws SQLException If database access error
     */
    public void storeValuesMatl() throws SQLException{ //store MATL values into the database
    	String mainMatlIdString = Integer.toString(mainMatlId);
    	String sqlMatl = "INSERT INTO matl VALUES ('" + mainMatlIdString + "', '" + specificText.getText() + "', '" + reactionText.getText() + "', '" + specIdText.getText() + "', '" +
    			idText.getText() + "', '" + referenceText.getText() + "', '" + reactionsText.getText() + "', '" + densityText.getText() + "', '" + conductivityText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMatl);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException If database access error
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoDump();
    	showInfoMatl();
    }
    
    public void showInfoDump() throws SQLException { //to show the info when the page is loaded
    	String sqlDump = "SELECT * FROM dump;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlDump);
		while (rs.next()) {
			massSelection = rs.getString(1);
			massCombo.setValue(massSelection);
			smokeSelection = rs.getString(2);
			smokeCombo.setValue(smokeSelection);
			framesText.setText(rs.getString(3));
			dtDevcText.setText(rs.getString(4));
		}
    }
    
    public void showInfoMatl() throws SQLException { //to show the info when the page is loaded
    	String sqlMatl = "SELECT * FROM matl;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlMatl);
		while (rs.next()) {
			specificText.setText(rs.getString(2));
			reactionText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			idText.setText(rs.getString(5));
			referenceText.setText(rs.getString(6));
			reactionsText.setText(rs.getString(7));
			densityText.setText(rs.getString(8));
			conductivityText.setText(rs.getString(9));
		}
    }
}
