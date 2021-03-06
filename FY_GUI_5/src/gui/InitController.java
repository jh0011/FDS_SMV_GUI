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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class for Init.fxml
 *  
 */
public class InitController implements Initializable{
	
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
	 * @throws SQLException If database access error
	 * @throws IOException If page cannot be displayed
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
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
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
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
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
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
	 */
	@FXML
	public void newInitLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW INIT LINE
		doCheckingInit();
		
		if (xbFormat && checkFloat && checkInteger){
			//store values
			storeValuesInit();
			
			//confirmation message for success
			Values.printConfirmationMessage("INIT", true);
			
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
			//confirmation message for failure
			Values.printConfirmationMessage("INIT", false);
			//System.out.println("Unable to add a new INIT line");
		}
		
	}
	
	/**
	 * Add a new line for MESH namelist
	 * @param event The add button is clicked
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
	 */
	@FXML
	public void addMeshLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW MESH LINE
		doCheckingMesh();
		
		if (checkIJK && checkMeshXBformat){
			//store values
			storeValuesMesh();
			
			//confirmation message for success
			Values.printConfirmationMessage("MESH", true);
			
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
			//confirmation message for failure
			Values.printConfirmationMessage("MESH", false);
			//System.out.println("Unable to add a new MESH line");
		}
	}
	
	/**
	 * Description of INIT namelist
	 * @param event Open the description label
	 */
	@FXML
    public void openInitDesc(MouseEvent event) {
		String content = "The INIT namelist can be used to change the ambient conditions within rectangular regions of the domain. \n\n"
				+ "ID: To identify the INIT line.\n\n"
				+ "Part_ID: The type of particle to be inserted.\n\n"
				+ "Spec ID: Mass Fraction and Spec ID occur in pairs.\n\n"
				+ "N Particles: To indicate the number of particles to insert within a specified region of the domain.\n\n"
				+ "N Particles per cell: To indicate the number of particles within each grid cell of a specified region.\n\n"
				+ "Mass Fraction: Mass Fraction and Spec ID occur in pairs.\n\n"
				+ "Mass per time: For particles with mass, it can be used to specify the rate the particles should be introduced, in the units of kg/s.\n\n"
				+ "Mass per volume: To indicate the mass per volume of particles in the units of kg/m3.\n\n"
				+ "XB: The origin point of a mesh is defined by the first, third and fifth values of the real number "
    			+ "sextuplet, XB, and the opposite corner is defined by the second, fourth and sixth values. There should be 6 comma-separated values.";
		String namelist = "INIT";
		Values.openDesc(namelist, content);
    }

	/**
	 * Description of MESH namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openMeshDesc(MouseEvent event) {
    	String content = "All FDS calculations must be performed within a domain that is made up of rectilinear volumes called "
    			+ "meshes. Each mesh is divided into rectangular cells, the number of which depends on the desired resolution "
    			+ "of the flow dynamics. MESH is the namelist group that defines the computational domain. \n\n"
    			+ "IJK: The mesh is subdivided into uniform cells via the parameter IJK. The 3 real values should be comma-separated.\n\n"
    			+ "XB: The origin point of a mesh is defined by the first, third and fifth values of the real number "
    			+ "sextuplet, XB, and the opposite corner is defined by the second, fourth and sixth values. There should be 6 comma-separated values.";
    	String namelist = "MESH";
		Values.openDesc(namelist, content);
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
		return Values.checkXbFormat(tempField);
	}
	
	/**
	 * Check if the integer is a positive value 
	 * @param valueTF TextField for user input
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
			initAlert.setContentText("N particles and N particles per cell values should be an integer. Please check again.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
	}
	
	/**
	 * Check if the float is a positive value 
	 * @param valueTF TextField for user input
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
			initAlert.setContentText("Mass per time, mass per volume and XB values should be numerical. Please check again.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
	}
	
	/**
	 * Check XB format and if it is filled 
	 * @param valueTF TextField for user input
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
	 * @param valueTF TextField for user input
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
		return Values.checkIJKformat(valueTF);
	}
	
	/**
	 * Store the values into the database after input validation
	 * @throws SQLException If database access error
	 */
	public void storeValues() throws SQLException{ //store values into the database
		storeValuesInit();
		storeValuesMesh();
	}
	
	/**
	 * Store the INIT line and its values into the database
	 * @throws SQLException If database access error
	 */
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
	
	/**
	 * Store the INIT line and its values into the database
	 * @throws SQLException If database access error
	 */
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
	 * @throws SQLException If database access error
	 */
	public void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoInit();
		showInfoMesh();
	}
	
	/**
	 * Display the values for the INIT name list
	 * @throws SQLException If database access error
	 */
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
	
	/**
	 * Display the values for the MESH name list
	 * @throws SQLException If database access error
	 */
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
