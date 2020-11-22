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
import javafx.stage.Stage;

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
	
	@FXML 
	private void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
	}
	
	@FXML
	private void goToPart(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
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
		else {
			System.out.println("Unable to go back to PART page");
		}
	}
	
	@FXML
	private void goToDevc(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
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
		else {
			System.out.println("Unable to go to DEVC page");
		}
		
		
	}
	
	@FXML
	private void newPropLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW PROP LINE
		doCheckingProp();
		
		if (checkFloat && checkInteger) {
			//store the values
			storeValuesProp();
			
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
			System.out.println("Unable to add new PROP line");
		}
	}
	
	@FXML
	private void newSpecLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW SPEC LINE
		//store the values
		storeValuesSpec();
		
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
	
	@FXML
    void backgroundSelect(ActionEvent event) {
		backgroundSelection = backgroundCombo.getSelectionModel().getSelectedItem().toString();
		backgroundCombo.setValue(backgroundSelection);
    }
	
	@FXML
    void integrateSelect(ActionEvent event) {
		integrateSelection = integrateCombo.getSelectionModel().getSelectedItem().toString();
		integrateCombo.setValue(integrateSelection);
    }
	
	@FXML
    void normaliseSelect(ActionEvent event) {
		normaliseSelection = normaliseCombo.getSelectionModel().getSelectedItem().toString();
		normaliseCombo.setValue(normaliseSelection);
    }
	
	private void doChecking() {
		doCheckingProp();
	}
	
	private void doCheckingProp() {
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
	
	private boolean checkFloatValues(TextField tempField){
		try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert propAlert = new Alert(Alert.AlertType.INFORMATION);
				propAlert.setTitle("Invalid value");
				propAlert.setContentText("Offset, pressure and particle velocity should be positive values. Please check again.");
				propAlert.setHeaderText(null);
				propAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert propAlert = new Alert(Alert.AlertType.INFORMATION);
			propAlert.setTitle("Invalid value");
			propAlert.setContentText("Offset, pressure and particle velocity should be numerical values. Please check again.");
			propAlert.setHeaderText(null);
			propAlert.show();
			return false;
		}
	}
	
	private boolean checkIntegerValues(TextField tempField) {
		try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert propAlert = new Alert(Alert.AlertType.INFORMATION);
				propAlert.setTitle("Invalid integer value");
				propAlert.setContentText("Particles per second should be a positive integer. Please check again.");
				propAlert.setHeaderText(null);
				propAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert propAlert = new Alert(Alert.AlertType.INFORMATION);
			propAlert.setTitle("Invalid integer value");
			propAlert.setContentText("Particles per second should be an integer. Please check again.");
			propAlert.setHeaderText(null);
			propAlert.show();
			return false;
		}
	}
	
	private void storeValues() throws SQLException { //store values into the database
		storeValuesProp();
		storeValuesSpec();
	}
	
	private void storeValuesProp() throws SQLException { //store PROP values into the database
		String mainPropIdString = Integer.toString(mainPropId);
		String sqlProp = "INSERT INTO prop VALUES('" + mainPropIdString + "', '" + idText.getText() + "', '" + partIdText.getText() + "', '" +
				qtyText.getText() + "', '" + smvIdText.getText() + "', '" + offsetText.getText() + "', '" + integrateSelection + "', '" +
				normaliseSelection + "', '" + pressureText.getText() + "', '" + partSecText.getText() + "', '" + partVelText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlProp);
	}
	
	private void storeValuesSpec() throws SQLException { //store SPEC values into the database
		String mainSpecIdString = Integer.toString(mainSpecId);
		String sqlSpec = "INSERT INTO spec VALUES ('" + mainSpecIdString + "', '" + specIdText.getText() + "', '" + backgroundSelection + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlSpec);
	}
	
	protected void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoProp();
		showInfoSpec();
	}
	
	protected void showInfoProp() throws SQLException { //to show the info when the page is loaded
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
	
	protected void showInfoSpec() throws SQLException { //to show the info when the page is loaded
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
