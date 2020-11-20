package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class PropController implements Initializable{
	
	@FXML TextField idText; //string
	@FXML TextField partIdText; //string
	@FXML TextField qtyText; //string
	@FXML TextField smvIdText; //string
	@FXML TextField offsetText; //float
	@FXML TextField integrateText; //boolean
	@FXML TextField normaliseText; //boolean
	@FXML TextField pressureText; //float
	@FXML TextField partSecText; //integer
	@FXML TextField partVelText; //float
	
	@FXML TextField specIdText; //string
	@FXML TextField backgroundText; //boolean
	
	@FXML Button addPropBtn;
	@FXML Button addSpecBtn;
	
	boolean checkBoolean;
	boolean checkFloat;
	boolean checkInteger;
	
	static int mainPropId = 1;
	static int mainSpecId = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Tooltip propTooltip = new Tooltip("Click to add another PROP field.");
		addPropBtn.setTooltip(propTooltip);
		Tooltip specTooltip = new Tooltip("Click to add another SPEC field.");
		addSpecBtn.setTooltip(specTooltip);
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
		
		if (checkBoolean && checkFloat && checkInteger) {
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
	}
	
	@FXML
	private void goToDevc(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		if (checkBoolean && checkFloat && checkInteger) {
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
	private void newPropLine(ActionEvent event) throws IOException, SQLException{
		mainPropId++;
		String mainPropIdString = Integer.toString(mainPropId);
		String sqlProp = "INSERT INTO prop (mainID, ID, PART_ID, QUANTITY, SMOKEVIEW_ID, OFFSET, PDPA_INTEGRATE, PDPA_NORMALIZE"
				+ ", OPERATING_PRESSURE, PARTICLES_PER_SECOND, PARTICLE_VELOCITY) VALUES ('" + mainPropIdString + "', '', '', '', '', '', '', '', '', '', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlProp);
		
		showInfo();
	}
	
	@FXML
	private void newSpecLine(ActionEvent event) throws IOException, SQLException{
		mainSpecId++;
		String mainSpecIdString = Integer.toString(mainSpecId);
		String sqlSpec = "INSERT INTO spec (mainID, ID, BACKGROUND) VALUES ('" + mainSpecIdString + "', '', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlSpec);
		
		showInfo();
	}
	
	private void doChecking() {
		checkBoolean = true;
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
		if (!integrateText.getText().equals("")) {
			checkBoolean = checkBoolean && checkBooleanValues(integrateText);
		}
		if (!normaliseText.getText().equals("")) {
			checkBoolean = checkBoolean && checkBooleanValues(normaliseText);
		}
		if (!backgroundText.getText().equals("")) {
			checkBoolean = checkBoolean && checkBooleanValues(backgroundText);
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
	
	private boolean checkBooleanValues(TextField tempField){
		if (tempField.getText().equalsIgnoreCase("true")){
			tempField.setText("TRUE");
			return true;
		}
		else if (tempField.getText().equalsIgnoreCase("false")){
			tempField.setText("FALSE");
			return true;
		}
		else{
			Alert propAlert = new Alert(Alert.AlertType.INFORMATION);
			propAlert.setTitle("Invalid logical value");
			propAlert.setContentText("Pdpa_integrate, Pdpa_normalize and Background should be logical values. Please check again.");
			propAlert.setHeaderText(null);
			propAlert.show();
			return false;
		}
	}
	
	private void storeValues() throws SQLException { //store values into the database
		String mainPropIdString = Integer.toString(mainPropId);
		String mainSpecIdString = Integer.toString(mainSpecId);
		String sqlProp = "INSERT INTO prop VALUES('" + mainPropIdString + "', '" + idText.getText() + "', '" + partIdText.getText() + "', '" +
				qtyText.getText() + "', '" + smvIdText.getText() + "', '" + offsetText.getText() + "', '" + integrateText.getText() + "', '" +
				normaliseText.getText() + "', '" + pressureText.getText() + "', '" + partSecText.getText() + "', '" + partVelText.getText() + "');";
		String sqlSpec = "INSERT INTO spec VALUES ('" + mainSpecIdString + "', '" + specIdText.getText() + "', '" + backgroundText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlProp);
		statement.executeUpdate(sqlSpec);
	}
	
	protected void showInfo() throws SQLException { //to show the info when the page is loaded
		String sqlProp = "SELECT * FROM prop;";
		String sqlSpec = "SELECT * FROM spec;";
		
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
			integrateText.setText(rs.getString(7));
			normaliseText.setText(rs.getString(8));
			pressureText.setText(rs.getString(9));
			partSecText.setText(rs.getString(10));
			partVelText.setText(rs.getString(11));
		}
		
		ResultSet rs2 = statement.executeQuery(sqlSpec);
		while(rs2.next()) {
			specIdText.setText(rs2.getString(2));
			backgroundText.setText(rs2.getString(3));
		}
	}

}
