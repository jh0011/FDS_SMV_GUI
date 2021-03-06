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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class for Hvac.fxml
 * 
 */
public class HvacController implements Initializable{
	
	//hvac
	@FXML TextField idText; //string
    @FXML TextField roughnessText; //float (+)
    @FXML TextField devcIdText; //string
    @FXML TextField lengthText; //float (+)
    @FXML TextField fanIdText; //string
    @FXML TextField areaText; //float (+)
    @FXML ComboBox typeCombo;
    
    //hole
    @FXML TextField meshIdText; //string
    @FXML TextField multIdText; //string
    @FXML TextField holeDevcText; //string
    @FXML TextField ctrlIdText; //string
    @FXML TextField xbText; //xb float (6) (+)
    
    //isof
    @FXML TextField qtyText; //string
    @FXML TextField val1Text; //float (+ / -)
    @FXML TextField val2Text; //float (+ / -)
    @FXML TextField val3Text; //float (+ / -)
    
    boolean checkFloatPosHvac;
    boolean checkXb;
    boolean checkFloat;
    
    static String typeSelection = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> typeList = FXCollections.observableArrayList("", "DUCT", "NODE", "FAN", "FILTER", "AIRCOIL", "LEAK");
		typeCombo.setItems(typeList);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException If database access error
	 * @throws IOException If page cannot be displayed
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
	 * Go to the previous page (PRES) + input validation
	 * @param event Back button is clicked
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
	 */
    @FXML
    public void goToPres(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if (checkFloatPosHvac && checkXb && checkFloat) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Pres.fxml"));
	    		Parent root = loader.load();
	    		
	    		PresController presCont = loader.getController(); //Get the next page's controller
	    		presCont.showInfo(); //Set the values of the page 
	    		Scene presScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(presScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Go to the next page (MOVE) + input validation
	 * @param event Next button is clicked
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
	 */
    @FXML
    public void goToMove(ActionEvent event) throws SQLException, IOException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if (checkFloatPosHvac && checkXb && checkFloat) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Move.fxml"));
	    		Parent root = loader.load();
	    		
	    		MoveController moveCont = loader.getController(); //Get the next page's controller
	    		moveCont.showInfo(); //Set the values of the page 
	    		Scene moveScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(moveScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Description of HVAC namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openHvacDesc(MouseEvent event) {
    	String content = "The HVAC (heating, ventilation and air-conditioner) namelist invokes the HVAC solver. An HVAC "
    			+ "network is defined by providing inputs for the ducts; duct nodes; and any fans, dampers, filters, or heating "
    			+ "and coiling coils present in the system. \n\n"
    			+ "Devc_ID: It is the ID of a DEVC for a damper, fan, or aircoil in the duct.\n\n"
    			+ "Fan_ID: It is the ID of a fan located in the duct.\n\n"
    			+ "Type_ID: It is a character string that indicates the type of component that the namelist group is defining. "
    			+ "TYPE_ID can be DUCT, NODE, FAN, FILTER, AIRCOIL, or LEAK.\n\n"
    			+ "Roughness: It is the absolute roughness in m of the duct that is used to compute the friction factor for the duct. "
    			+ "If ROUGHNESS is not set, the HVAC solver will not compute the friction factor and the wall friction will "
    			+ "be zero.\n\n"
    			+ "Length: It is the length of the duct in units of m. Note that LENGTH is not computed automatically as the difference "
    			+ "between the XYZ of the duct�s endpoints.\n\n"
    			+ "Area: It is the cross sectional area of the duct in units of m2.";
    	String namelist = "HVAC";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Description of HOLE namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openHoleDesc(MouseEvent event) {
    	String content = "The HOLE namelist defines parameters that carve a hole out of an existing obstruction or set of obstructions. \n\n"
    			+ "Mesh_ID: To identify the mesh ID.\n\n"
    			+ "Devc_ID: To allow a hole to be controlled with either the CTRL or DEVC namelist groups, you will need to add the "
    			+ "CTRL_ID or DEVC_ID parameter respectively.\n\n"
    			+ "XB: Any solid mesh cells within the volume specfied are removed. XB consists of 6 values, comma-separated.\n\n"
    			+ "Mult_ID: To identify the multiplier ID.\n\n"
    			+ "Ctrl_ID: To allow a hole to be controlled with either the CTRL or DEVC namelist groups, you will need to add the "
    			+ "CTRL_ID or DEVC_ID parameter respectively.";
    	String namelist = "HOLE";
		Values.openDesc(namelist, content);
    }

    /**
	 * Description of ISOF namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openIsofDesc(MouseEvent event) {
    	String content = "The ISOF (isosurface file) namelist creates three-dimensional animated contours of gas phase "
    			+ "scalar quantities. For example, a 300 degrees Celsius temperature isosurface is a 3-D surface on which the gas temperature "
    			+ "is 300 degrees Celsius.";
    	String namelist = "ISOF";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Set the value chosen for type
	 * @param event A value is chosen for type
	 */
    @FXML
    public void typeSelect(ActionEvent event) {
    	typeSelection = typeCombo.getSelectionModel().getSelectedItem().toString();
    	typeCombo.setValue(typeSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingHvac();
    	doCheckingHole();
    	doCheckingIsof();
    }
    
    /**
     * Checking for the HVAC name list
     */
    public void doCheckingHvac() {
    	checkFloatPosHvac = true;
    	if (!roughnessText.getText().equals("")) {
    		checkFloatPosHvac = checkFloatPosHvac && checkFloatPosValues(roughnessText);
    	}
    	if (!lengthText.getText().equals("")) {
    		checkFloatPosHvac = checkFloatPosHvac && checkFloatPosValues(lengthText);
    	}
    	if (!areaText.getText().equals("")) {
    		checkFloatPosHvac = checkFloatPosHvac && checkFloatPosValues(areaText);
    	}
    }
    
    /**
     * Checking for the HOLE name list
     */
    public void doCheckingHole() {
    	checkXb = true;
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    
    /**
     * Checking for the ISOF name list
     */
    public void doCheckingIsof() {
    	checkFloat = true;
    	if(!val1Text.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(val1Text);
    	}
    	if(!val2Text.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(val2Text);
    	}
    	if(!val3Text.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(val3Text);
    	}
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatPosValues(TextField tempField) { //check if the float is positive
    	String param = "Roughness, length and area";
    	return Values.checkPosFloatValues(param, tempField);
    	
    }
    
    /**
     * Check the XB format: <br>
     * - No white spaces <br>
     * - 6 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkXbFormat(TextField tempField) { //check the XB format
    	return Values.checkXbFormat(tempField);
    }
    
    /**
	 * Check if the value is a float 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatValues(TextField tempField) { //check if value is a float
    	String param = "Value(1), Value(2) and Value(3)";
    	return Values.checkFloatValues(param, tempField);
    }
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException If database access error
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesHvac();
    	storeValuesHole();
    	storeValuesIsof();
    }
    
    /**
     * Store HVAC line and its values into the database
     * @throws SQLException If database access error
     */
    public void storeValuesHvac() throws SQLException { //store HVAC values into the database
    	String sqlHvac = "INSERT INTO hvac VALUES ('" + idText.getText() + "', '" + roughnessText.getText() + "', '" + devcIdText.getText() + "', '" +
    			lengthText.getText() + "', '" + fanIdText.getText() + "', '" + areaText.getText() + "', '" + typeSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlHvac);
    }
    
    /**
     * Store HOLE line and its values into the database
     * @throws SQLException If database access error
     */
    public void storeValuesHole() throws SQLException { //store HOLE values into the database
    	String sqlHole = "INSERT INTO hole VALUES ('" + meshIdText.getText() + "', '" + multIdText.getText() + "', '" + holeDevcText.getText() + "', '" + ctrlIdText.getText() + 
    			"', '" + xbText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlHole);
    }
    
    /**
     * Store ISOF line and its values into the database
     * @throws SQLException If database access error
     */
    public void storeValuesIsof() throws SQLException { //store ISOF values into the database
    	String sqlIsof = "INSERT INTO isof VALUES ('" + qtyText.getText() + "', '" + val1Text.getText() + "', '" + val2Text.getText() + "', '" + val3Text.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlIsof);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException If database access error
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoHvac();
    	showInfoHole();
    	showInfoIsof();
    }
    
    /**
     * Display the values for the HVAC name list
     * @throws SQLException If database access error
     */
    public void showInfoHvac() throws SQLException { //to show the info when the page is loaded
    	String sqlHvac = "SELECT * FROM hvac";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlHvac);
		while (rs.next()) {
			idText.setText(rs.getString(1));
			roughnessText.setText(rs.getString(2));
			devcIdText.setText(rs.getString(3));
			lengthText.setText(rs.getString(4));
			fanIdText.setText(rs.getString(5));
			areaText.setText(rs.getString(6));
			typeSelection = rs.getString(7);
			typeCombo.setValue(typeSelection);
		}
    }
    
    /**
     * Display the values for the HOLE name list
     * @throws SQLException If database access error
     */
    public void showInfoHole() throws SQLException { //to show the info when the page is loaded
    	String sqlHole = "SELECT * FROM hole";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlHole);
		while (rs.next()) {
			meshIdText.setText(rs.getString(1));
			multIdText.setText(rs.getString(2));
			holeDevcText.setText(rs.getString(3));
			ctrlIdText.setText(rs.getString(4));
			xbText.setText(rs.getString(5));
		}
    }
    
    /**
     * Display the values for the ISOF name list
     * @throws SQLException If database access error
     */
    public void showInfoIsof() throws SQLException { //to show the info when the page is loaded
    	String sqlIsof = "SELECT * FROM isof";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlIsof);
		while (rs.next()) {
			qtyText.setText(rs.getString(1));
			val1Text.setText(rs.getString(2));
			val2Text.setText(rs.getString(3));
			val3Text.setText(rs.getString(4));
		}
    }
}
