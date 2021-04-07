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
 * Controller class for Prop.fxml
 *  
 */
public class PropController implements Initializable{
	
	
	//prop
	@FXML TextField idText; //string
	@FXML TextField partIdText; //string
	@FXML TextField qtyText; //string
	@FXML TextField smvIdText; //string
	@FXML TextField offsetText; //float
	@FXML ComboBox integrateCombo; //boolean
    @FXML ComboBox normaliseCombo; //boolean
	@FXML TextField pressureText; //float
	@FXML TextField partSecText; //integer
	@FXML TextField partVelText; //float
	
	//spec
	@FXML TextField specIdText; //string
	@FXML ComboBox backgroundCombo; //boolean
	
	@FXML Button addPropBtn;
	@FXML Button addSpecBtn;
	
	boolean checkFloat;
	boolean checkInteger;
	
	static String integrateSelection = "";
	static String normaliseSelection = "";
	static String backgroundSelection = "";
	
	static int mainPropId = 1;
	static int mainSpecId = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip propTooltip = new Tooltip("Click to add another PROP field.");
		addPropBtn.setTooltip(propTooltip);
		Tooltip specTooltip = new Tooltip("Click to add another SPEC field.");
		addSpecBtn.setTooltip(specTooltip);
		
		ObservableList<String> integrateList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		integrateCombo.setItems(integrateList);
		
		ObservableList<String> normaliseList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		normaliseCombo.setItems(normaliseList);
		
		ObservableList<String> backgroundList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		backgroundCombo.setItems(backgroundList);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML 
	public void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
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
	 * Go to the previous page (PART) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToPart(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		try {
			if (checkFloat && checkInteger) {
				//store the values
				storeValues();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Part.fxml"));
				Parent root = loader.load();
				
				PartController partCont = loader.getController(); //Get the next page's controller
				partCont.showInfo(); //Set the values of the page 
				Scene partScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(partScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Go to the next page (DEVC) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToDevc(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		try {
			if (checkFloat && checkInteger) {
				//store the values
				storeValues();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Devc.fxml"));
				Parent root = loader.load();
				
				DevcController devcCont = loader.getController(); //Get the next page's controller
				devcCont.showInfo(); //Set the values of the page 
				Scene devcScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(devcScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Add a new line for PROP namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void newPropLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW PROP LINE
		doCheckingProp();
		
		if (checkFloat && checkInteger) {
			//store the values
			storeValuesProp();
			
			//confirmation message for success
			Values.printConfirmationMessage("PROP", true);
			
			mainPropId++;
			String mainPropIdString = Integer.toString(mainPropId);
			String sqlProp = "INSERT INTO prop (mainID, ID, PART_ID, QUANTITY, SMOKEVIEW_ID, OFFSET, PDPA_INTEGRATE, PDPA_NORMALIZE"
					+ ", OPERATING_PRESSURE, PARTICLES_PER_SECOND, PARTICLE_VELOCITY) VALUES ('" + mainPropIdString + "', '', '', '', '', '', '', '', '', '', '');";
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlProp);
			
			showInfoProp();
		}
		else {
			//confirmation message for failure
			Values.printConfirmationMessage("PROP", false);
			//System.out.println("Unable to add new PROP line");
		}
	}
	
	/**
	 * Add a new line for SPEC namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void newSpecLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW SPEC LINE
		//store the values
		storeValuesSpec();
		
		//confirmation message for success
		Values.printConfirmationMessage("SPEC", true);
		
		mainSpecId++; //no need to do checking
		String mainSpecIdString = Integer.toString(mainSpecId);
		String sqlSpec = "INSERT INTO spec (mainID, ID, BACKGROUND) VALUES ('" + mainSpecIdString + "', '', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlSpec);
		
		showInfoSpec();
	}
	
	/**
	 * Description of PROP namelist
	 * @param event Open the description label
	 */
	@FXML
    public void openPropDesc(MouseEvent event) {
		String content = "The PROP namelist can be used to list the properties of a device, which can be referenced to by other namelists.\n\n"
				+ "ID: To identify the PROP line.\n\n"
				+ "Part_ID: To specify the particle ID.\n\n"
				+ "Smokeview_ID: The name of a drawing of a sprinkler to include in the Smokeview animation.\n\n"
				+ "Offset: Radius (m) of a sphere surrounding the sprinkler where the water droplets are initially placed in the simulation.\n\n"
				+ "Pdpa_integrate: PDPA (Phase Doppler Particle Analysis). A logical parameter for choosing between time integrated or "
				+ "instantaneous values. Set to TRUE by default.\n\n"
				+ "Operating Pressure: The gauge pressure at the sprinkler, in units of bar.\n\n"
				+ "Particles per second: It is the number of droplets inserted every second per active sprinkler or nozzle.\n\n"
				+ "Particle velocity: Initial droplet velocity.";
		String namelist = "PROP";
		Values.openDesc(namelist, content);
    }

	/**
	 * Description of SPEC namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openSpecDesc(MouseEvent event) {
    	String content = "The SPEC namelist is used to define gas species. This input group is used to define "
    			+ "both primitive gas species and lumped species.\n\n"
    			+ "ID: To identify the SPEC line.\n\n"
    			+ "Background: If a species is to be used as the background species, Background should be set to TRUE.";
    	String namelist = "SPEC";
    	Values.openDesc(namelist, content);
    }
	
	@FXML
	public void backgroundSelect(ActionEvent event) {
		backgroundSelection = backgroundCombo.getSelectionModel().getSelectedItem().toString();
		backgroundCombo.setValue(backgroundSelection);
    }
	
	@FXML
	public void integrateSelect(ActionEvent event) {
		integrateSelection = integrateCombo.getSelectionModel().getSelectedItem().toString();
		integrateCombo.setValue(integrateSelection);
    }
	
	@FXML
	public void normaliseSelect(ActionEvent event) {
		normaliseSelection = normaliseCombo.getSelectionModel().getSelectedItem().toString();
		normaliseCombo.setValue(normaliseSelection);
    }
	
	/**
	 * Call the checking methods for the different namelists
	 */
	public void doChecking() {
		doCheckingProp();
	}
	
	public void doCheckingProp() {
		checkFloat = true;
		checkInteger = true;
		
		if (!offsetText.getText().equals("")) {
			checkFloat = checkFloat && checkFloatValues(offsetText);
		}
		if (!pressureText.getText().equals("")) {
			checkFloat = checkFloat && checkFloatValues(pressureText);
		}
		if (!partVelText.getText().equals("")) {
			checkFloat = checkFloat && checkFloatValues(partVelText);
		}
		if (!partSecText.getText().equals("")) {
			checkInteger = checkInteger && checkIntegerValues(partSecText);
		}
	}
	
	/**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkFloatValues(TextField tempField){
		String param = "Offset, pressure and particle velocity";
    	return Values.checkPosFloatValues(param, tempField);
		
	}
	
	/**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkIntegerValues(TextField tempField) {
		String param = "Particles per second";
    	return Values.checkPosIntValues(param, tempField);
		
	}
	
	/**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
	public void storeValues() throws SQLException { //store values into the database
		storeValuesProp();
		storeValuesSpec();
	}
	
	public void storeValuesProp() throws SQLException { //store PROP values into the database
		String mainPropIdString = Integer.toString(mainPropId);
		String sqlProp = "INSERT INTO prop VALUES('" + mainPropIdString + "', '" + idText.getText() + "', '" + partIdText.getText() + "', '" +
				qtyText.getText() + "', '" + smvIdText.getText() + "', '" + offsetText.getText() + "', '" + integrateSelection + "', '" +
				normaliseSelection + "', '" + pressureText.getText() + "', '" + partSecText.getText() + "', '" + partVelText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlProp);
	}
	
	public void storeValuesSpec() throws SQLException { //store SPEC values into the database
		String mainSpecIdString = Integer.toString(mainSpecId);
		String sqlSpec = "INSERT INTO spec VALUES ('" + mainSpecIdString + "', '" + specIdText.getText() + "', '" + backgroundSelection + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlSpec);
	}
	
	/**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
	public void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoProp();
		showInfoSpec();
	}
	
	public void showInfoProp() throws SQLException { //to show the info when the page is loaded
		String sqlProp = "SELECT * FROM prop;";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlProp);
		while (rs.next()){
			idText.setText(rs.getString(2));
			partIdText.setText(rs.getString(3));
			qtyText.setText(rs.getString(4));
			smvIdText.setText(rs.getString(5));
			offsetText.setText(rs.getString(6));
			integrateSelection = rs.getString(7);
			integrateCombo.setValue(integrateSelection);
			normaliseSelection = rs.getString(8);
			normaliseCombo.setValue(normaliseSelection);
			pressureText.setText(rs.getString(9));
			partSecText.setText(rs.getString(10));
			partVelText.setText(rs.getString(11));
		}
	}
	
	public void showInfoSpec() throws SQLException { //to show the info when the page is loaded
		String sqlSpec = "SELECT * FROM spec;";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		
		ResultSet rs2 = statement.executeQuery(sqlSpec);
		while(rs2.next()) {
			specIdText.setText(rs2.getString(2));
			backgroundSelection = rs2.getString(3);
			backgroundCombo.setValue(backgroundSelection);
		}
	}

}
