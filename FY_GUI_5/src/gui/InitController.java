package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.xml.datatype.Duration;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class InitController implements Initializable{
	/**
	 * Controller class for Init.fxml
	 * @author 
	 */
	
	//init
	@FXML TextField idText; //string
	@FXML TextField partIdText; //string
	@FXML TextField specIdText; //string
	@FXML TextField npartText; //int
	@FXML TextField npartCellText; //int
	@FXML TextField massTimeText; //float
	@FXML TextField massVolText; //float
	@FXML TextField massFracText; //float
	@FXML TextField xbText; //array
	
	//mesh
	@FXML TextField ijkText; //integer array
	@FXML TextField xbMeshText; //float array
	
	
	//button
	@FXML Button cancelBtn;
	@FXML Button initBackBtn;
	@FXML Button initNextBtn;
	@FXML Button addInitBtn;
	@FXML Button addMeshBtn;
	
	boolean xbFormat = true;
	boolean checkFloat = true;
	boolean checkInteger = true;
	boolean checkIJK = true;
	boolean checkMeshXBformat = true;
	
	static int mainInitId = 1;
	static int mainMeshId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Tooltip initTooltip = new Tooltip("Click to add another INIT field.");
		addInitBtn.setTooltip(initTooltip);
		Tooltip meshTooltip = new Tooltip("Click to add another MESH field.");
		addMeshBtn.setTooltip(meshTooltip);
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
	 * Go to the previous page (TIME) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToTime(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		try {
			if (xbFormat && checkFloat && checkInteger && checkIJK && checkMeshXBformat){
				//store values
				storeValues();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Time.fxml"));
				Parent root = loader.load();
				
				TimeController timeCont = loader.getController(); //Get the next page's controller
				timeCont.showInfo(); //Set the values of the page 
				Scene timeScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(timeScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Go to the next page (PART) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToPart(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		try {
			if (xbFormat && checkFloat && checkInteger && checkIJK && checkMeshXBformat){
				//store values
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
	 * Add a new line for INIT namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void newInitLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW INIT LINE
		doCheckingInit();
		
		if (xbFormat && checkFloat && checkInteger){
			//store values
			storeValuesInit();
		
			mainInitId++;
			String mainInitIdString = Integer.toString(mainInitId);
			String sqlInit = "INSERT INTO init(mainID, idText, partIdText, specIdText, npartText, "
					+ "npartCellText, massTimeText, massVolText, massFracText, xbText) "
					+ "VALUES ('" + mainInitIdString + "', '', '', '', '', '', '', '', '', '')";
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlInit);
			
			showInfoInit();
		}
		else {
			System.out.println("Unable to add a new INIT line");
		}
		
	}
	
	/**
	 * Add a new line for MESH namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void addMeshLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW MESH LINE
		doCheckingMesh();
		
		if (checkIJK && checkMeshXBformat){
			//store values
			storeValuesMesh();
			
			mainMeshId++;
			String mainMeshIdString = Integer.toString(mainMeshId);
			String sqlMesh = "INSERT INTO mesh (mainID, ijkText, xbText) VALUES ('"
					+ mainMeshIdString + "', '', '');";
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlMesh);
			
			showInfoMesh();
		}
		else {
			System.out.println("Unable to add a new MESH line");
		}
	}
	
	/**
	 * Call the checking methods for the different namelists
	 */
	public void doChecking(){
		doCheckingInit();
		doCheckingMesh();
	}
	
	/**
	 * Check the input fields for INIT
	 */
	public void doCheckingInit() {
		xbFormat = true;
		checkFloat = true;
		checkInteger = true;
		
		if (!massTimeText.getText().equals("")){
			checkFloat = checkFloat && checkFloatValues(massTimeText);
		}
		if (!massVolText.getText().equals("")){
			checkFloat = checkFloat && checkFloatValues(massVolText);
		}
		if (!massFracText.getText().equals("")){
			checkFloat = checkFloat && checkFloatValues(massFracText);
		}
		if (!npartText.getText().equals("")){
			checkInteger = checkInteger && checkIntValues(npartText);
		}
		if (!npartCellText.getText().equals("")){
			checkInteger = checkInteger && checkIntValues(npartCellText);
		}
		if (!xbText.getText().equals("")){
			xbFormat = xbFormat && checkXbFormat(xbText);
		}
	}
	
	/**
	 * Check the input fields for MESH
	 */
	public void doCheckingMesh() {
		checkIJK = true;
		checkMeshXBformat = true;
		
		checkMeshXBformat = checkMeshXBformat && checkMeshXB(xbMeshText);
		checkIJK = checkIJK && checkIJKformat(ijkText);
	}
	
	/**
	 * Check the format of XB <br>
	 * - No white spaces <br>
	 * - 6 values <br>
	 * - Positive float
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkXbFormat(TextField tempField){ //check the XB format
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should not be any whitespaces.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		String concatXB = "";
		
		if (xbValues.length != 6){
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should be 6 real values.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				float floatVal = Float.valueOf(xbValues[i]);
				if (floatVal < 0) { //check if the float is negative
					Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
					initAlert.setTitle("Invalid XB value");
					initAlert.setContentText("The values should not have negative numbers. Please check again.");
					initAlert.setHeaderText(null);
					initAlert.show();
					return false;
				}
				if (i==5){
					concatXB = concatXB + Float.toString(floatVal);
				}
				else{
					concatXB = concatXB + Float.toString(floatVal) + ","; //convert to string
				}
			}
			catch(Exception e){//check if each value is real
			
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Incorrect XB format");
				initAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
		}
		tempField.setText(concatXB);
		return true;
	}
	
	/**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkIntValues(TextField valueTF){
		try{
			String value = valueTF.getText();
			int valueInt = Integer.parseInt(value);
			if (valueInt < 0){ //check if it is negative
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Invalid init value");
				initAlert.setContentText("The values should not have negative numbers. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
			else{
				valueTF.setText(value);
				return true;
			}
		}
		catch(Exception e){ //check if it is integer
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Invalid init value");
			initAlert.setContentText("The particles and particles per cell values should be an integer. Please check again.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
	}
	
	/**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkFloatValues (TextField valueTF){
		try{
			String value = valueTF.getText();
			float valueFloat = Float.valueOf(value);
			if (valueFloat < 0){ //check if it is integer
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Invalid init value");
				initAlert.setContentText("The values should not have negative numbers. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
			else{
				valueTF.setText(Float.toString(valueFloat));
				return true;
			}
		}
		catch(Exception e){ //check if it is a float
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Invalid init value");
			initAlert.setContentText("The mass per time, mass per volume and XB values should be numerical. Please check again.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
	}
	
	/**
	 * Check XB format and if it is filled 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkMeshXB(TextField valueTF){
		if (valueTF.getText().equals("")){ //check if it is empty
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Empty MESH XB values");
			meshAlert.setContentText("MESH XB is a required value.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
		return checkXbFormat(valueTF); //check if there are 6 values comma-separated
		
	}
	
	/**
	 * Check the IJK format and if it is filled 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkIJKformat(TextField valueTF){ 
		if (valueTF.getText().equals("")){ //check if ijk is empty
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Empty MESH IJK values");
			meshAlert.setContentText("MESH IJK is a required value.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
		if (valueTF.getText().contains(" ")){ //check if there are any white spaces
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect IJK format");
			initAlert.setContentText("There should not be any whitespaces.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		
		String[] ijkValues = valueTF.getText().split(",");
		String concatIJK = "";
		if (ijkValues.length != 3){ //check if ijk is the correct length
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Incorrect IJK format");
			meshAlert.setContentText("There should be 3 integer values, comma-separated.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				int ijkInt = Integer.parseInt(ijkValues[i]);
				if (ijkInt <= 0){ //check if ijk is negative or zero
					Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
					meshAlert.setTitle("Incorrect IJK format");
					meshAlert.setContentText("The IJK values should be more than zero.");
					meshAlert.setHeaderText(null);
					meshAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the ijk string
					concatIJK = concatIJK + String.valueOf(ijkInt) + ",";
				}
				else{
					concatIJK = concatIJK + String.valueOf(ijkInt);
				}
			}
			valueTF.setText(concatIJK);
			return true;
		}
		catch(Exception e){ //check if ijk is an integer
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Incorrect IJK format");
			meshAlert.setContentText("There should be 3 integer values.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
	}
	
	/**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
	public void storeValues() throws SQLException{ //store values into the database
		storeValuesInit();
		storeValuesMesh();
	}
	
	public void storeValuesInit() throws SQLException{ //store INIT values into the database
		String mainInitIdString = Integer.toString(mainInitId);
		String sqlInit = "INSERT INTO init VALUES('" + mainInitIdString + "', '" + idText.getText() +
				"', '" + partIdText.getText() + "', '" + specIdText.getText() + "', '" +
				npartText.getText() + "', '" + npartCellText.getText() + "', '" +
				massTimeText.getText() + "', '" + massVolText.getText() + "', '" + 
				massFracText.getText() + "', '" + xbText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlInit);
	}
	
	public void storeValuesMesh() throws SQLException{ //store MESH values into the database
		String mainMeshIdString = Integer.toString(mainMeshId);
		String sqlMesh = "INSERT INTO mesh VALUES('" + mainMeshIdString + "', '" + ijkText.getText() +
				"', '" + xbMeshText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMesh);
	}

	/**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
	public void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoInit();
		showInfoMesh();
	}
	
	public void showInfoInit() throws SQLException { //to show the info when the page is loaded
		String sqlInit = "SELECT * FROM init;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlInit);
		while (rs.next()){
			idText.setText(rs.getString(2));
			partIdText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			npartText.setText(rs.getString(5));
			npartCellText.setText(rs.getString(6));
			massTimeText.setText(rs.getString(7));
			massVolText.setText(rs.getString(8));
			massFracText.setText(rs.getString(9));
			xbText.setText(rs.getString(10));
		}
	}
	
	public void showInfoMesh() throws SQLException { //to show the info when the page is loaded
		String sqlMesh = "SELECT * FROM mesh;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		
		ResultSet rs2 = statement.executeQuery(sqlMesh);
		while (rs2.next()){
			ijkText.setText(rs2.getString(2));
			xbMeshText.setText(rs2.getString(3));
		}
	}
}
