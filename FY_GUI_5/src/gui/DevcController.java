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
 * Controller class for Devc.fxml
 * 
 */
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
    @FXML ComboBox centeredCombo; //boolean
    
	@FXML Button addDevcBtn;
	@FXML Button addSlcfBtn;
	
	boolean checkXb;
	boolean checkXyz;
	boolean checkFloat;
	
	static String iorSelection = "";
	static String vectorSelection = "";
	static String centeredSelection = "";
	
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
		
		ObservableList<String> centeredList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		centeredCombo.setItems(centeredList);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException If database access error
	 * @throws IOException If cannot display the page
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
	 * Go to the previous page (PROP) + input validation
	 * @param event Back button is clicked
	 * @throws IOException If cannot display the page
	 * @throws SQLException If database access error
	 */
	@FXML
	public void goToProp(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		try {
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
		} catch(Exception e) {
			Values.showError();
		}
		
	}
	
	/**
	 * Go to the next page (SURF) + input validation
	 * @param event Next button is clicked
	 * @throws IOException If cannot display the page
	 * @throws SQLException If database access error
	 */
	@FXML
	public void goToSurf(ActionEvent event) throws SQLException, IOException { //NEXT SCENE
		doChecking();
		
		try {
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
		} catch(Exception e) {
			Values.showError();
		}
    }
	
	/**
	 * Set the value chosen for IOR
	 * @param event A value is chosen for IOR
	 */
	@FXML
	public void iorSelect(ActionEvent event) {
		iorSelection = iorCombo.getSelectionModel().getSelectedItem().toString();
		iorCombo.setValue(iorSelection);
    }
	
	/**
	 * Add a new line for DEVC namelist
	 * @param event The add button is clicked
	 * @throws IOException If cannot display the page
	 * @throws SQLException If database access error
	 */
	@FXML
	public void newDevcLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW DEVC LINE
		doCheckingDevc();
		if (checkXyz && checkXb) {
			//store the values
			storeValuesDevc();
			
			//confirmation message for success
			Values.printConfirmationMessage("DEVC", true);
			
			mainDevcId++;
			String mainDevcIdString = Integer.toString(mainDevcId);
			String sqlDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('" + mainDevcIdString + "', '', '', '', '', '', '', '');";
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlDevc);
			
			showInfoDevc();
		}
		else {
			//confirmation message for failure
			Values.printConfirmationMessage("DEVC", false);
			//System.out.println("Unable to add new DEVC line");
		}
		
	}
	
	/**
	 * Add a new line for SLCF namelist
	 * @param event The add button is clicked
	 * @throws SQLException If database access error
	 */
	@FXML
	public void newSlcfLine(ActionEvent event) throws SQLException { //ADD NEW SLCF LINE
		doCheckingSlcf();
		
		if (checkFloat) {
			//store the values
			storeValuesSlcf();
			
			//confirmation message for success
			Values.printConfirmationMessage("SLCF", true);
			
			mainSlcfId++;
			String mainSlcfIdString = Integer.toString(mainSlcfId);
			String sqlSlcf = "INSERT INTO slcf (mainID, QUANTITY, SPEC_ID, PBY, PBZ, PBX, VECTOR, CELL_CENTERED) VALUES ('" + mainSlcfIdString + 
					"', '', '', '', '', '', '', '');";
			
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlSlcf);
			
			showInfoSlcf();
		}
		else {
			//confirmation message for failure
			Values.printConfirmationMessage("SLCF", false);
			//System.out.println("Unable to add new SLCF line");
		}
		
    }
	
	/**
	 * Description of DEVC namelist
	 * @param event Open the description label
	 */
	@FXML
    public void openDevcDesc(MouseEvent event) {
		String content = "The DEVC namelist can be used to define a device. \n\n"
				+ "ID: To identify the DEVC line.\n\n"
				+ "Prop_ID: To specify a Prop ID.\n\n"
				+ "Spec_ID: To specify a species ID.\n\n"
				+ "XYZ: FDS uses these coordinates to determine in which gas or wall cell the device is located. 3 real values which are comma-separated.\n\n"
				+ "IOR: The parameter IOR (Index of Orientation) is required for any device that is placed on the surface of a solid. "
				+ "The values +/-1 or +/-2 or +/-3 indicate the direction that the device �points.� For example, IOR=-1 means that the "
				+ "device is mounted on a wall that faces in the negative x direction.\n\n"
				+ "XB: The origin point of a mesh is defined by the first, third and fifth values of the real number "
				+ "sextuplet, XB, and the opposite corner is defined by the second, fourth and sixth values. There should be 6 comma-separated values.";
		String namelist = "DEVC";
		Values.openDesc(namelist, content);
    }
	
	/**
	 * Description of SLCF namelist
	 * @param event Open the description label
	 */
	@FXML
    public void openSlcfDesc(MouseEvent event) {
		String content = "The SLCF (slice file) namelist allows you to record various gas phase quantities at more than a single point.\n\n"
				+ "PBX: Controls planes perpendicular to the X-axis. For example, x=5.3\n\n"
				+ "PBY: Controls planes perpendicular to the Y-axis. For example, y=5.3\n\n"
				+ "PBZ: Controls planes perpendicular to the Z-axis. For example, z=5.3\n\n"
				+ "Cell_centered: Normally, FDS averages slice file data at cell corners. For example, gas temperatures are computed at "
				+ "cell centers, but they are linearly interpolated to cell corners and output to a file that is read by Smokeview. "
				+ "To prevent this from happening, set Cell_centered to TRUE. \n\n"
				+ "Spec_ID: To identify a species ID.\n\n"
				+ "Vector: If Cell_centered is set to TRUE and is combined with Vector=TRUE then the staggered velocity components will be displayed.";
		String namelist = "SLCF";
		Values.openDesc(namelist, content);
    }

	/**
	 * Set the value chosen for vector
	 * @param event A value is chosen for vector
	 */
    @FXML
    public void vectorSelect(ActionEvent event) {
    	vectorSelection = vectorCombo.getSelectionModel().getSelectedItem().toString();
    	vectorCombo.setValue(vectorSelection);
    }
    
    /**
	 * Set the value chosen for centered
	 * @param event A value is chosen for centered
	 */
    @FXML
    public void centeredSelect(ActionEvent event) {
    	centeredSelection = centeredCombo.getSelectionModel().getSelectedItem().toString();
    	centeredCombo.setValue(centeredSelection);
    }
    
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
		doCheckingDevc();
		doCheckingSlcf();
	}
    
    /**
     * Checking for the DEVC name list
     */
    public void doCheckingDevc() {
    	checkXb = true;
		checkXyz = true;
	
		if(!xbText.getText().equals("")) {
			checkXb = checkXb && checkXbFormat(xbText);
		}
		if(!xyzText.getText().equals("")) {
			checkXyz = checkXyz && checkXyzFormat(xyzText);
		}
    }
    
    /**
     * Checking for the SLCF name list
     */
    public void doCheckingSlcf() {
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
     * Check the XYZ format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkXyzFormat(TextField tempField) {
    	return Values.checkXyzFormat(tempField);
	}
	
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatValues(TextField tempField) {
    	String param = "PBX, PBY and PBZ";
    	return Values.checkPosFloatValues(param, tempField);
	}
	
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException If database access error
	 */
    public void storeValues() throws SQLException { //store values into the database
		storeValuesDevc();
		storeValuesSlcf();
	}
	
    public void storeValuesDevc() throws SQLException { //store DEVC values into the database
		String mainDevcIdString = Integer.toString(mainDevcId);
		String sqlDevc = "INSERT INTO devc VALUES('" + mainDevcIdString + "', '" + devcIdText.getText() + "', '" + propIdText.getText() + "', '" +
				specIdText.getText() + "', '" + xyzText.getText() + "', '" + quantityText.getText() + "', '" + iorSelection + "', '" + xbText.getText() + "');";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlDevc);
	}
	
    /**
     * Store the SLCF line and its values into the database
     * @throws SQLException If database access error
     */
    public void storeValuesSlcf() throws SQLException { //store SLCF values into the database
		String mainSlcfIdString = Integer.toString(mainSlcfId);
		String sqlSlcf = "INSERT INTO slcf VALUES('" + mainSlcfIdString + "', '" + slcfQtyText.getText() + "', '" + slcfSpecIdText.getText() + "', '" +
				pbyText.getText() + "', '" + pbzText.getText() + "', '" + pbxText.getText() + "', '" + vectorSelection + "', '" + centeredSelection + "');";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlSlcf);
	}
	
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException If database access error
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoDevc();
		showInfoSlcf();
	}
	
    public void showInfoDevc() throws SQLException { //to show the info when the page is loaded
		String sqlDevc = "SELECT * FROM devc;";
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
	}
	
    public void showInfoSlcf() throws SQLException { //to show the info when the page is loaded
		String sqlSlcf = "SELECT * FROM slcf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs2 = statement.executeQuery(sqlSlcf);
		while (rs2.next()) {
			slcfQtyText.setText(rs2.getString(2));
			slcfSpecIdText.setText(rs2.getString(3));
			pbyText.setText(rs2.getString(4));
			pbzText.setText(rs2.getString(5));
			pbxText.setText(rs2.getString(6));
			vectorSelection = rs2.getString(7);
			vectorCombo.setValue(vectorSelection);
			centeredSelection = rs2.getString(8);
			centeredCombo.setValue(centeredSelection);
		}
	}
}
