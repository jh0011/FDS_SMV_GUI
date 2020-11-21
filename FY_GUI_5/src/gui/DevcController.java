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

public class DevcController implements Initializable{
	//devc
	@FXML TextField devcIdText; //string
	@FXML TextField propIdText; //string
	@FXML TextField specIdText; //string
	@FXML TextField xyzText; //float (3)
	@FXML TextField quantityText; //string
	@FXML ComboBox iorCombo;
	@FXML TextField xbText; //float (6)
	
	//slcf
	@FXML TextField slcfQtyText; //string
    @FXML TextField slcfSpecIdText; //string
    @FXML TextField pbyText; //float
    @FXML TextField pbzText; //float
    @FXML TextField pbxText; //float
    @FXML ComboBox vectorCombo; //boolean
    
	@FXML Button addDevcBtn;
	@FXML Button addSlcfBtn;
	
	boolean checkXb;
	boolean checkXyz;
	boolean checkFloat;
	
	static String iorSelection = "";
	static String vectorSelection = "";
	
	static int mainDevcId = 1;
	static int mainSlcfId = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip devcTooltip = new Tooltip("Click to add another DEVC field.");
		addDevcBtn.setTooltip(devcTooltip);
		Tooltip slcfTooltip = new Tooltip("Click to add another SLCF field.");
		addSlcfBtn.setTooltip(slcfTooltip);
		
		ObservableList<String> iorList = FXCollections.observableArrayList("", "1", "-1", "2", "-2", "3", "-3");
		iorCombo.setItems(iorList);
		
		ObservableList<String> vectorList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		vectorCombo.setItems(vectorList);
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
	private void goToProp(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		if (checkXyz && checkXb && checkFloat) {
			//store the values
			storeValues();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Prop.fxml"));
			Parent root = loader.load();
			
			PropController propCont = loader.getController(); //Get the next page's controller
			propCont.showInfo(); //Set the values of the page 
			Scene propScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(propScene);
			mainWindow.show();
		}
		
	}
	
	@FXML
    private void goToSurf(ActionEvent event) throws SQLException, IOException { //NEXT SCENE
		doChecking();
		if (checkXyz && checkXb && checkFloat) {
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
		else {
			System.out.println("Unable to proceed to SURF page");
		}
    }
	
	@FXML
    private void iorSelect(ActionEvent event) {
		iorSelection = iorCombo.getSelectionModel().getSelectedItem().toString();
		iorCombo.setValue(iorSelection);
    }
	
	@FXML
	private void newDevcLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW DEVC LINE
		doCheckingDevc();
		if (checkXyz && checkXb) {
			//store the values
			storeValues();
			
			mainDevcId++;
			String mainDevcIdString = Integer.toString(mainDevcId);
			String sqlDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('" + mainDevcIdString + "', '', '', '', '', '', '', '');";
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlDevc);
			
			showInfo();
		}
		else {
			System.out.println("Unable to add new DEVC line");
		}
	}
	
	@FXML
    private void newSlcfLine(ActionEvent event) throws SQLException { //ADD NEW SLCF LINE
		doCheckingSlcf();
		
		if (checkFloat) {
			//store the values
			storeValues();
			
			mainSlcfId++;
			String mainSlcfIdString = Integer.toString(mainSlcfId);
			String sqlSlcf = "INSERT INTO slcf (mainID, QUANTITY, SPEC_ID, PBY, PBZ, PBX, VECTOR) VALUES ('" + mainSlcfIdString + 
					"', '', '', '', '', '', '');";
			
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlSlcf);
			
			showInfo();
		}
		else {
			System.out.println("Unable to add new SLCF line");
		}
    }

    @FXML
    private void vectorSelect(ActionEvent event) {
    	vectorSelection = vectorCombo.getSelectionModel().getSelectedItem().toString();
    	vectorCombo.setValue(vectorSelection);
    }
    
    private void doChecking() {
		doCheckingDevc();
		doCheckingSlcf();
	}
    
    private void doCheckingDevc() {
    	checkXb = true;
		checkXyz = true;
	
		if(!xbText.getText().equals("")) {
			checkXb = checkXb && checkXbFormat(xbText);
		}
		if(!xyzText.getText().equals("")) {
			checkXyz = checkXyz && checkXyzFormat(xyzText);
		}
    }
    
    private void doCheckingSlcf() {
		checkFloat = true;
		
    	if(!pbxText.getText().equals("")) {
			checkFloat = checkFloat && checkFloatValues(pbxText);
		}
		if(!pbyText.getText().equals("")) {
			checkFloat = checkFloat && checkFloatValues(pbyText);
		}
		if(!pbzText.getText().equals("")) {
			checkFloat = checkFloat && checkFloatValues(pbzText);
		}
    }
	
	private boolean checkXbFormat(TextField tempField) {
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XB format");
			devcAlert.setContentText("There should not be any whitespaces.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		float[] xbFloatValues = new float[6];
		String concatXB = "";
		
		if (xbValues.length != 6){ //check if it is the correct length
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XB format");
			devcAlert.setContentText("There should be 6 real values.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				Float.valueOf(xbValues[i]);
			}
			catch(Exception e){//check if each value is real
			
				Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
				devcAlert.setTitle("Incorrect XB format");
				devcAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				devcAlert.setHeaderText(null);
				devcAlert.show();
				return false;
			}
			
			xbFloatValues[i] = Float.valueOf(xbValues[i]); //convert to float
			if (i==5){
				concatXB = concatXB + Float.toString(xbFloatValues[i]);
			}
			else{
				concatXB = concatXB + Float.toString(xbFloatValues[i]) + ","; //convert to string
			}
		}
		tempField.setText(concatXB);
		return true;
	}
	
	private boolean checkXyzFormat(TextField tempField) {
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XYZ format");
			devcAlert.setContentText("There should not be any whitespaces.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		String[] xyzValues = tempField.getText().split(",");
		String concatXYZ = "";
		if (xyzValues.length != 3){ //check if ijk is the correct length
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XYZ format");
			devcAlert.setContentText("There should be 3 positive real values, comma-separated.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float xyzFloat = Float.valueOf(xyzValues[i]);
				if (xyzFloat < 0){ //check if xyz is negative or zero
					Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
					devcAlert.setTitle("Incorrect XYZ format");
					devcAlert.setContentText("The XYZ values should be positive real values.");
					devcAlert.setHeaderText(null);
					devcAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the xyz string
					concatXYZ = concatXYZ + Float.toString(xyzFloat) + ",";
				}
				else{
					concatXYZ = concatXYZ + Float.toString(xyzFloat);
				}
			}
			tempField.setText(concatXYZ);
			return true;
		}
		catch(Exception e){ //check if xyz is a number
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect xyz format");
			devcAlert.setContentText("There should be 3 positive real values.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean checkFloatValues(TextField tempField) {
		try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < 0){ //if it is not a positive float
				Alert slcfAlert = new Alert(Alert.AlertType.INFORMATION);
				slcfAlert.setTitle("Invalid value");
				slcfAlert.setContentText("PBX, PBY and PBZ should not be negative values. Please check again.");
				slcfAlert.setHeaderText(null);
				slcfAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert slcfAlert = new Alert(Alert.AlertType.INFORMATION);
			slcfAlert.setTitle("Invalid value");
			slcfAlert.setContentText("PBX, PBY and PBZ should be numerical values. Please check again.");
			slcfAlert.setHeaderText(null);
			slcfAlert.show();
			return false;
		}
	}
	
	
	private void storeValues() throws SQLException { //store values into the database
		String mainDevcIdString = Integer.toString(mainDevcId);
		String mainSlcfIdString = Integer.toString(mainSlcfId);
		String sqlDevc = "INSERT INTO devc VALUES('" + mainDevcIdString + "', '" + devcIdText.getText() + "', '" + propIdText.getText() + "', '" +
				specIdText.getText() + "', '" + xyzText.getText() + "', '" + quantityText.getText() + "', '" + iorSelection + "', '" + xbText.getText() + "');";
		String sqlSlcf = "INSERT INTO slcf VALUES('" + mainSlcfIdString + "', '" + slcfQtyText.getText() + "', '" + slcfSpecIdText.getText() + "', '" +
				pbyText.getText() + "', '" + pbzText.getText() + "', '" + pbxText.getText() + "', '" + vectorSelection + "');";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlDevc);
		statement.executeUpdate(sqlSlcf);
	}
	
	protected void showInfo() throws SQLException { //to show the info when the page is loaded
		String sqlDevc = "SELECT * FROM devc;";
		String sqlSlcf = "SELECT * FROM slcf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlDevc);
		while (rs.next()) {
			devcIdText.setText(rs.getString(2));
			propIdText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			xyzText.setText(rs.getString(5));
			quantityText.setText(rs.getString(6));
			iorSelection = rs.getString(7);
			iorCombo.setValue(iorSelection);
			xbText.setText(rs.getString(8));
		}
		ResultSet rs2 = statement.executeQuery(sqlSlcf);
		while (rs2.next()) {
			slcfQtyText.setText(rs2.getString(2));
			slcfSpecIdText.setText(rs2.getString(3));
			pbyText.setText(rs2.getString(4));
			pbzText.setText(rs2.getString(5));
			pbxText.setText(rs2.getString(6));
			vectorSelection = rs2.getString(7);
			vectorCombo.setValue(vectorSelection);
		}
	}

}
