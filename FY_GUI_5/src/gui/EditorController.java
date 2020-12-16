package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class EditorController implements Initializable{
	
	@FXML TextArea editorText;
	static String tempEditorString = "";
	static String path = "";
	static String CHID = "";
	static FileWriter fw;
	static BufferedWriter bw = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showFile();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to show Editor");
		}
	}
	
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

    @FXML
    public void goToTrnx(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	//store the updated text area
    	tempEditorString = editorText.getText();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Trnx.fxml"));
		Parent root = loader.load();
		
		TrnxController trnxCont = loader.getController(); //Get the next page's controller
		trnxCont.showInfo(); //Set the values of the page 
		Scene trnxScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(trnxScene);
		mainWindow.show();
    }
    
    public void goToFinal(ActionEvent event) throws IOException { //NEXT SCENE
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Final.fxml"));
		Parent root = loader.load();
		
		FinalController finalCont = loader.getController(); //Get the next page's controller
		Scene finalScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(finalScene);
		mainWindow.show();
    }
    
    @FXML
    public void saveFile(ActionEvent event) throws IOException { //save to the file system
    	//store the updated text area
    	tempEditorString = editorText.getText();
    	
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setTitle("Save FDS input file");
    	File selectedDirectory = directoryChooser.showDialog(null);
    	//System.out.println(selectedDirectory.getAbsolutePath());
    	File outputFile = new File(selectedDirectory + "\\" + CHID + ".fds");
    	boolean isFileCreated = false;
    	if (!outputFile.exists()){
    		isFileCreated = outputFile.createNewFile();
    		if (isFileCreated) { //confirmation message for CREATION of the file
        		Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
    			trnxAlert.setTitle("Successful file creation");
    			trnxAlert.setContentText("File has been created in directory: " + selectedDirectory);
    			trnxAlert.setHeaderText(null);
    			ImageView icon = new ImageView("Fire2.jpg");
    			icon.setFitHeight(48);
    	        icon.setFitWidth(48);
    	        trnxAlert.getDialogPane().setGraphic(icon);
    			trnxAlert.show();
    			
    			fw = new FileWriter(outputFile);
    			bw = new BufferedWriter(fw);
    			bw.write(editorText.getText());
    			bw.close();
        	}
		}
    	else { //confirmation to overwrite the existing file
			Alert trnxAlert = new Alert(Alert.AlertType.CONFIRMATION);
			trnxAlert.setTitle("File already exists");
			trnxAlert.setContentText("File with the same name already exists in directory: " + selectedDirectory + 
					"\nClick on OK to overwrite the existing file. Click on Cancel to choose another directory or rename the CHID parameter.");
			trnxAlert.setHeaderText(null);
			ImageView icon = new ImageView("Fire2.jpg");
			icon.setFitHeight(48);
	        icon.setFitWidth(48);
	        trnxAlert.getDialogPane().setGraphic(icon);
	        Optional<ButtonType> userOption = trnxAlert.showAndWait();
	        if (userOption.get() == ButtonType.OK){
	        	fw = new FileWriter(outputFile);
	    		bw = new BufferedWriter(fw);
	    		bw.write(editorText.getText());
	    		bw.close();
			}
		}
    	
    	//proceed to the final page
    	goToFinal(event);
    }
    
    
    
    public void showFile() throws SQLException {
    	if (!tempEditorString.equals("")) {
    		editorText.setText(tempEditorString);
    	}
    	else {
    		if (countNumLines("head") > 1) {
    			printValuesHead();
    		}
    		if (countNumLines("mesh") > 1) {
    			printValuesMesh();
    		}
    		if (countNumLines("time") > 1) {
    			printValuesTime();
    		}
    		if (countNumLines("init") > 1) {
    			printValuesInit();
    		}
    		if (countNumLines("surf") > 1) {
    			printValuesSurf();
    		}
    		if (countNumLines("reac") > 1) {
    			printValuesReac();
    		}
    		if (countNumLines("vent") > 1) {
    			printValuesVent();
    		}
    		if (countNumLines("part") > 1) {
    			printValuesPart();
    		}
    		if (countNumLines("slcf") > 1) {
    			printValuesSlcf();
    		}
    		if (countNumLines("matl") > 1) {
    			printValuesMatl();
    		}
    		if (countNumLines("obst") > 1) {
    			printValuesObst();
    		}
    		if (countNumLines("spec") > 1) {
    			printValuesSpec();
    		}
    		if (countNumLines("devc") > 1) {
    			printValuesDevc();
    		}
    		if (countNumLines("ctrl") > 1) {
    			printValuesCtrl();
    		}
    		if (countNumLines("prop") > 1) {
    			printValuesProp();
    		}
    		if (countNumLines("radi") > 1) {
    			printValuesRadi();
    		}
    		if (countNumLines("radf") > 1) {
    			printValuesRadf();
    		}
    		if (countNumLines("mult") > 1) {
    			printValuesMult();
    		}
    		if (countNumLines("pres") > 1) {
    			printValuesPres();
    		}
    		if (countNumLines("move") > 1) {
    			printValuesMove();
    		}
    		editorText.appendText("&TAIL /");
    	}
    	tempEditorString = editorText.getText();
    }
    
    public int countNumLines(String tableName) throws SQLException {
    	String sql = "SELECT COUNT(*) FROM " + tableName + ";";
    	ResultSet rs = getStatement().executeQuery(sql);
    	int count = 0;
    	while (rs.next()) {
    		count = rs.getInt(1);
    	}
    	return count;
    }
    
    public void printValuesHead() throws SQLException { //head
    	String sqlHead = "SELECT * FROM head";
    	String TITLE = "";
		ResultSet rs = getStatement().executeQuery(sqlHead);
		while (rs.next()) {
			CHID = rs.getString(1);
			TITLE = rs.getString(2);
		}
		editorText.appendText("&HEAD" + "\n");
		appendToEditorString("CHID", CHID);
		appendToEditorString("TITLE", TITLE);
		editorText.appendText("/" + "\n");
    }
    
    public void printValuesMesh() throws SQLException { //mesh
    	//get the number of MESH lines
    	String sqlMesh = "SELECT mainID FROM mesh GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMesh);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each MESH line
	    	String IJK = "";
	    	String XB = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlMesh = "SELECT * FROM mesh WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlMesh);
	        	while (rs.next()) {
	        		IJK = rs.getString(2);
	        		XB = rs.getString(3);
	        	}
	        	editorText.appendText("&MESH" + "\n");
	        	appendToEditorNumber("IJK", IJK);
	        	appendToEditorNumber("XB", XB);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesTime() throws SQLException { //time
    	String sqlTime = "SELECT * FROM time";
    	String endTime = "";
    	String startTime = "";
    	String dt = "";
		ResultSet rs = getStatement().executeQuery(sqlTime);
		while (rs.next()) {
			endTime = rs.getString(1);
			startTime = rs.getString(2);
			dt = rs.getString(3);
		}
		editorText.appendText("&TIME" + "\n");
		appendToEditorNumber("T_END", endTime);
		appendToEditorNumber("T_BEGIN", startTime);
		appendToEditorNumber("DT", dt);
		editorText.appendText("/" + "\n");
    }
    
    public void printValuesInit() throws SQLException {
    	//get the number of INIT lines
    	String sqlInit = "SELECT mainID FROM init GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlInit);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each INIT line
	    	String ID = "";
	    	String PART_ID = "";
	    	String SPEC_ID = "";
	    	String N_PARTICLES = "";
	    	String N_PARTICLES_PER_CELL = "";
	    	String MASS_PER_TIME = "";
	    	String MASS_PER_VOLUME = "";
	    	String MASS_FRACTION = "";
	    	String XB = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlInit = "SELECT * FROM init WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlInit);
	        	while (rs.next()) {
	        		ID = rs.getString(2);
	        		PART_ID = rs.getString(3);
	        		SPEC_ID = rs.getString(4);
	        		N_PARTICLES = rs.getString(5);
	        		N_PARTICLES_PER_CELL = rs.getString(6);
	        		MASS_PER_TIME = rs.getString(7);
	        		MASS_PER_VOLUME = rs.getString(8);
	        		MASS_FRACTION = rs.getString(9);
	        		XB = rs.getString(10);
	        	}
	        	editorText.appendText("&INIT" + "\n");
	        	appendToEditorString("ID", ID);
	        	appendToEditorString("PART_ID", PART_ID);
	        	appendToEditorString("SPEC_ID", SPEC_ID);
	        	appendToEditorNumber("N_PARTICLES", N_PARTICLES);
	        	appendToEditorNumber("N_PARTICLES_PER_CELL", N_PARTICLES_PER_CELL);
	        	appendToEditorNumber("MASS_PER_TIME", MASS_PER_TIME);
	        	appendToEditorNumber("MASS_PER_VOLUME", MASS_PER_VOLUME);
	        	appendToEditorNumber("MASS_FRACTION", MASS_FRACTION);
	        	appendToEditorNumber("XB", XB);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesSurf() throws SQLException { //surf
    	//get the number of SURF lines
    	String sqlSurf = "SELECT mainID FROM surf GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSurf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each SURF line
	    	String ID = "";
	    	String PART_ID = "";
	    	String MATL_ID = "";
	    	String VEL = "";
	    	String TMP_FRONT = "";
	    	String BACKING = "";
	    	String DEFAULT = "";
	    	String GEOMETRY = "";
	    	String COLOR = "";
	    	String HRRPUA = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSurf = "SELECT * FROM surf WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSurf);
	        	while (rs.next()) {
	        		ID = rs.getString(2);
	        		PART_ID = rs.getString(3);
	        		MATL_ID = rs.getString(4);
	        		VEL = rs.getString(5);
	        		TMP_FRONT = rs.getString(6);
	        		BACKING = rs.getString(7);
	        		DEFAULT = rs.getString(8);
	        		GEOMETRY = rs.getString(9);
	        		COLOR = rs.getString(10);
	        		HRRPUA = rs.getString(11);
	        	}
	        	editorText.appendText("&SURF" + "\n");
	        	appendToEditorString("ID", ID);
	        	appendToEditorString("PART_ID", PART_ID);
	        	appendToEditorString("MATL_ID", MATL_ID);
	        	appendToEditorNumber("VEL", VEL);
	        	appendToEditorNumber("TMP_FRONT", TMP_FRONT);
	        	appendToEditorString("BACKING", BACKING);
	        	appendToEditorBoolean("DEFAULT", DEFAULT);
	        	appendToEditorString("GEOMETRY", GEOMETRY);
	        	appendToEditorString("COLOR", COLOR);
	        	appendToEditorNumber("HRRPUA", HRRPUA);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesReac() throws SQLException { //reac
    	//get the number of REAC lines
    	String sqlReac = "SELECT mainID FROM reac GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlReac);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each REAC line
	    	String AUTO_IGNITION_TEMPERATURE = "";
	    	String SOOT_YIELD = "";
	    	String FUEL = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlReac = "SELECT * FROM reac WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlReac);
	        	while (rs.next()) {
	        		AUTO_IGNITION_TEMPERATURE = rs.getString(2);
	        		SOOT_YIELD = rs.getString(3);
	        		FUEL = rs.getString(4);
	        	}
	        	editorText.appendText("&REAC" + "\n");
	        	appendToEditorNumber("AUTO_IGNITION_TEMPERATURE", AUTO_IGNITION_TEMPERATURE);
	        	appendToEditorNumber("SOOT_YIELD", SOOT_YIELD);
	        	appendToEditorString("FUEL", FUEL);
	        	editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesVent() throws SQLException{ //vent
    	//get the number of VENT lines
    	String sqlVent = "SELECT mainID FROM vent GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlVent);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each VENT line
	    	String XB = "";
	    	String SURF_ID = "";
	    	String MB = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlVent = "SELECT * FROM vent WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlVent);
	        	while (rs.next()) {
	        		XB = rs.getString(2);
	        		SURF_ID = rs.getString(3);
	        		MB = rs.getString(4);
	        	}
	        	editorText.appendText("&VENT" + "\n");
	        	appendToEditorNumber("XB", XB);
	        	appendToEditorString("SURF_ID", SURF_ID);
	        	appendToEditorString("MB", MB);
	        	editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesPart() throws SQLException{ //part
    	//get the number of PART lines
    	String sqlPart = "SELECT mainID FROM part GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlPart);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each PART line
	    	String SURF_ID = "";
	    	String SPEC_ID = "";
	    	String PROP_ID = "";
	    	String QUANTITIES = "";
	    	String STATIC = "";
	    	String MASSLESS = "";
	    	String SAMPLING_FACTOR = "";
	    	String DIAMETER = "";
	    	String ID = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlPart = "SELECT * FROM part WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlPart);
	        	while (rs.next()) {
	        		SURF_ID = rs.getString(2);
	        		SPEC_ID = rs.getString(3);
	        		PROP_ID = rs.getString(4);
	        		QUANTITIES = rs.getString(5);
	        		STATIC = rs.getString(6);
	        		MASSLESS = rs.getString(7);
	        		SAMPLING_FACTOR = rs.getString(8);
	        		DIAMETER = rs.getString(9);
	        		ID = rs.getString(10);
	        	}
	        	editorText.appendText("&PART" + "\n");
	        	appendToEditorString("SURF_ID", SURF_ID);
	        	appendToEditorString("SPEC_ID", SPEC_ID);
	        	appendToEditorString("PROP_ID", PROP_ID);
	        	appendToEditorString("QUANTITIES", QUANTITIES);
	        	appendToEditorBoolean("STATIC", STATIC);
	        	appendToEditorBoolean("MASSLESS", MASSLESS);
	        	appendToEditorNumber("SAMPLING_FACTOR", SAMPLING_FACTOR);
	        	appendToEditorNumber("DIAMETER", DIAMETER);
	        	appendToEditorString("ID", ID);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesSlcf() throws SQLException { //slcf
    	//get the number of SLCF lines
    	String sqlSlcf = "SELECT mainID FROM slcf GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSlcf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each SLCF line
	    	String QUANTITY = "";
	    	String SPEC_ID = "";
	    	String PBY = "";
	    	String PBZ = "";
	    	String PBX = "";
	    	String VECTOR = "";
	    	String CELL_CENTERED = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSlcf = "SELECT * FROM slcf WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSlcf);
	        	while (rs.next()) {
	        		QUANTITY = rs.getString(2);
	        		SPEC_ID = rs.getString(3);
	        		PBY = rs.getString(4);
	        		PBZ = rs.getString(5);
	        		PBX = rs.getString(6);
	        		VECTOR = rs.getString(7);
	        		CELL_CENTERED = rs.getString(8);
	        	}
	        	editorText.appendText("&SLCF" + "\n");
	        	appendToEditorString("QUANTITY", QUANTITY);
	        	appendToEditorString("SPEC_ID", SPEC_ID);
	        	appendToEditorNumber("PBY", PBY);
	        	appendToEditorNumber("PBZ", PBZ);
	        	appendToEditorNumber("PBX", PBX);
	        	appendToEditorBoolean("VECTOR", VECTOR);
	        	appendToEditorBoolean("CELL_CENTERED", CELL_CENTERED);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesMatl() throws SQLException {
    	//get the number of MATL lines
    	String sqlMatl = "SELECT mainID FROM matl GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMatl);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each MATL line
	    	String SPECIFIC_HEAT = "";
	    	String HEAT_OF_REACTION = "";
	    	String SPEC_ID = "";
	    	String ID = "";
	    	String REFERENCE_TEMPERATURE = "";
	    	String N_REACTIONS = "";
	    	String DENSITY = "";
	    	String CONDUCTIVITY = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlMatl = "SELECT * FROM matl WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlMatl);
	        	while (rs.next()) {
	        		SPECIFIC_HEAT = rs.getString(2);
	        		HEAT_OF_REACTION = rs.getString(3);
	        		SPEC_ID = rs.getString(4);
	        		ID = rs.getString(5);
	        		REFERENCE_TEMPERATURE = rs.getString(6);
	        		N_REACTIONS = rs.getString(7);
	        		DENSITY = rs.getString(8);
	        		CONDUCTIVITY = rs.getString(9);
	        	}
	        	editorText.appendText("&MATL" + "\n");
	        	appendToEditorNumber("SPECIFIC_HEAT", SPECIFIC_HEAT);
	        	appendToEditorNumber("HEAT_OF_REACTION", HEAT_OF_REACTION);
	        	appendToEditorString("SPEC_ID", SPEC_ID);
	        	appendToEditorString("ID", ID);
	        	appendToEditorNumber("REFERENCE_TEMPERATURE", REFERENCE_TEMPERATURE);
	        	appendToEditorNumber("N_REACTIONS", N_REACTIONS);
	        	appendToEditorNumber("DENSITY", DENSITY);
	        	appendToEditorNumber("CONDUCTIVITY", CONDUCTIVITY);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesObst() throws SQLException { //obst
    	//get the number of OBST lines
    	String sqlObst = "SELECT mainID FROM obst GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlObst);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each OBST line
	    	String BULK_DENSITY = "";
	    	String COLOR = "";
	    	String SURF_ID = "";
	    	String XB = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlObst = "SELECT * FROM obst WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlObst);
	        	while (rs.next()) {
	        		BULK_DENSITY = rs.getString(2);
	        		COLOR = rs.getString(3);
	        		SURF_ID = rs.getString(4);
	        		XB = rs.getString(5);
	        	}
	        	editorText.appendText("&OBST" + "\n");
	        	appendToEditorNumber("BULK_DENSITY", BULK_DENSITY);
	        	appendToEditorString("COLOR", COLOR);
	        	appendToEditorString("SURF_ID", SURF_ID);
	        	appendToEditorNumber("XB", XB);
	        	editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesSpec() throws SQLException { //spec
    	//get the number of SPEC lines
    	String sqlSpec = "SELECT mainID FROM spec GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSpec);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each SPEC line
	    	String ID = "";
	    	String BACKGROUND = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSpec = "SELECT * FROM spec WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSpec);
	        	while (rs.next()) {
	        		ID = rs.getString(2);
	        		BACKGROUND = rs.getString(3);
	        	}
	        	editorText.appendText("&SPEC" + "\n");
	        	appendToEditorString("ID", ID);
	        	appendToEditorBoolean("BACKGROUND", BACKGROUND);
	        	editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesDevc() throws SQLException { //devc
    	//get the number of DEVC lines
    	String sqlDevc = "SELECT mainID FROM devc GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlDevc);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each DEVC line
	    	String ID = "";
	    	String PROP_ID = "";
	    	String SPEC_ID = "";
	    	String XYZ = "";
	    	String QUANTITY = "";
	    	String IOR = "";
	    	String XB = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlDevc = "SELECT * FROM devc WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlDevc);
	        	while (rs.next()) {
	        		ID = rs.getString(2);
	        		PROP_ID = rs.getString(3);
	        		SPEC_ID = rs.getString(4);
	        		XYZ = rs.getString(5);
	        		QUANTITY = rs.getString(6);
	        		IOR = rs.getString(7);
	        		XB = rs.getString(8);
	        	}
	        	editorText.appendText("&DEVC" + "\n");
	        	appendToEditorString("ID", ID);
	        	appendToEditorString("PROP_ID", PROP_ID);
	        	appendToEditorString("SPEC_ID", SPEC_ID);
	        	appendToEditorNumber("XYZ", XYZ);
	        	appendToEditorString("QUANTITY", QUANTITY);
	        	appendToEditorNumber("IOR", IOR);
	        	appendToEditorNumber("XB", XB);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesCtrl() throws SQLException { //ctrl
    	//get the number of CTRL lines
    	String sqlCtrl = "SELECT mainID FROM ctrl GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlCtrl);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each CTRL line
	    	String INPUT_ID = "";
	    	String RAMP_ID = "";
	    	String ID = "";
	    	String LATCH = "";
	    	String FUNCTION_TYPE = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlCtrl = "SELECT * FROM ctrl WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlCtrl);
	        	while (rs.next()) {
	        		INPUT_ID = rs.getString(2);
	        		RAMP_ID = rs.getString(3);
	        		ID = rs.getString(4);
	        		LATCH = rs.getString(5);
	        		FUNCTION_TYPE = rs.getString(6);
	        	}
	        	editorText.appendText("&CTRL" + "\n");
	        	appendToEditorString("INPUT_ID", INPUT_ID);
	        	appendToEditorString("RAMP_ID", RAMP_ID);
	        	appendToEditorString("ID", ID);
	        	appendToEditorBoolean("LATCH", LATCH);
	        	appendToEditorString("FUNCTION_TYPE", FUNCTION_TYPE);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesProp() throws SQLException { //prop
    	//get the number of PROP lines
    	String sqlSurf = "SELECT mainID FROM prop GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSurf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each PROP line
	    	String ID = "";
	    	String PART_ID = "";
	    	String QUANTITY = "";
	    	String SMOKEVIEW_ID = "";
	    	String OFFSET = "";
	    	String PDPA_INTEGRATE = "";
	    	String PDPA_NORMALIZE = "";
	    	String OPERATING_PRESSURE = "";
	    	String PARTICLES_PER_SECOND = "";
	    	String PARTICLE_VELOCITY = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSurf = "SELECT * FROM prop WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSurf);
	        	while (rs.next()) {
	        		ID = rs.getString(2);
	        		PART_ID = rs.getString(3);
	        		QUANTITY = rs.getString(4);
	        		SMOKEVIEW_ID = rs.getString(5);
	        		OFFSET = rs.getString(6);
	        		PDPA_INTEGRATE = rs.getString(7);
	        		PDPA_NORMALIZE = rs.getString(8);
	        		OPERATING_PRESSURE = rs.getString(9);
	        		PARTICLES_PER_SECOND = rs.getString(10);
	        		PARTICLE_VELOCITY = rs.getString(11);
	        	}
	        	editorText.appendText("&PROP" + "\n");
	        	appendToEditorString("ID", ID);
	        	appendToEditorString("PART_ID", PART_ID);
	        	appendToEditorString("QUANTITY", QUANTITY);
	        	appendToEditorString("SMOKEVIEW_ID", SMOKEVIEW_ID);
	        	appendToEditorNumber("OFFSET", OFFSET);
	        	appendToEditorBoolean("PDPA_INTEGRATE", PDPA_INTEGRATE);
	        	appendToEditorBoolean("PDPA_NORMALIZE", PDPA_NORMALIZE);
	        	appendToEditorNumber("OPERATING_PRESSURE", OPERATING_PRESSURE);
	        	appendToEditorNumber("PARTICLES_PER_SECOND", PARTICLES_PER_SECOND);
	        	appendToEditorNumber("PARTICLE_VELOCITY", PARTICLE_VELOCITY);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesRadi() throws SQLException { //radi
    	String sqlRadi = "SELECT * FROM radi";
    	String RADIATION = "";
		ResultSet rs = getStatement().executeQuery(sqlRadi);
		while (rs.next()) {
			RADIATION = rs.getString(1);
		}
		editorText.appendText("&RADI" + "\n");
		appendToEditorBoolean("RADIATION", RADIATION);
		editorText.appendText("/" + "\n");
    }
    
    public void printValuesRadf() throws SQLException { //radf
    	String sqlRadf = "SELECT * FROM radf";
    	String I_STEP = "";
    	String J_STEP = "";
    	String K_STEP = "";
    	String XB = "";
		ResultSet rs = getStatement().executeQuery(sqlRadf);
		while (rs.next()) {
			I_STEP = rs.getString(1);
			J_STEP = rs.getString(2);
			K_STEP = rs.getString(3);
			XB = rs.getString(4);
		}
		editorText.appendText("&RADF" + "\n");
		appendToEditorNumber("I_STEP", I_STEP);
		appendToEditorNumber("J_STEP", J_STEP);
		appendToEditorNumber("K_STEP", K_STEP);
		appendToEditorNumber("XB", XB);
		editorText.appendText("/" + "\n");
    }
    
    public void printValuesMult() throws SQLException { //mult
    	//get the number of MULT lines
    	String sqlMult = "SELECT mainID FROM mult GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMult);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each MULT line
	    	String ID = "";
	    	String I_UPPER = "";
	    	String J_UPPER = "";
	    	String K_UPPER = "";
	    	String DX = "";
	    	String DY = "";
	    	String DZ = "";
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlMult = "SELECT * FROM mult WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlMult);
	        	while (rs.next()) {
	        		ID = rs.getString(2);
	        		I_UPPER = rs.getString(3);
	        		J_UPPER = rs.getString(4);
	        		K_UPPER = rs.getString(5);
	        		DX = rs.getString(6);
	        		DY = rs.getString(7);
	        		DZ = rs.getString(8);
	        	}
	        	editorText.appendText("&MULT" + "\n");
	        	appendToEditorString("ID", ID);
	        	appendToEditorNumber("I_UPPER", I_UPPER);
	        	appendToEditorNumber("J_UPPER", J_UPPER);
	        	appendToEditorNumber("K_UPPER", K_UPPER);
	        	appendToEditorNumber("DX", DX);
	        	appendToEditorNumber("DY", DY);
	        	appendToEditorNumber("DZ", DZ);
	    		editorText.appendText("/" + "\n");
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    public void printValuesPres() throws SQLException { //pres
    	String sqlPres = "SELECT * FROM pres";
    	String FISHPAK_BC = "";
    	String SOLVER = "";
		ResultSet rs = getStatement().executeQuery(sqlPres);
		while (rs.next()) {
			FISHPAK_BC = rs.getString(1);
			SOLVER = rs.getString(2);
		}
		editorText.appendText("&PRES" + "\n");
		appendToEditorNumber("FISHPAK_BC", FISHPAK_BC);
		appendToEditorString("SOLVER", SOLVER);
		editorText.appendText("/" + "\n");
    }
    
    public void printValuesMove() throws SQLException { //move
    	String sqlMove = "SELECT * FROM move";
    	String ID = "";
    	String X0 = "";
    	String Y0 = "";
    	String Z0 = "";
    	String ROTATION_ANGLE = "";
    	String AXIS = "";
		ResultSet rs = getStatement().executeQuery(sqlMove);
		while (rs.next()) {
			ID = rs.getString(1);
			X0 = rs.getString(2);
			Y0 = rs.getString(3);
			Z0 = rs.getString(4);
			ROTATION_ANGLE = rs.getString(5);
			AXIS = rs.getString(6);
		}
		editorText.appendText("&MOVE" + "\n");
		appendToEditorString("ID", ID);
		appendToEditorNumber("X0", X0);
		appendToEditorNumber("Y0", Y0);
		appendToEditorNumber("Z0", Z0);
		appendToEditorNumber("ROTATION_ANGLE", ROTATION_ANGLE);
		appendToEditorNumber("AXIS", AXIS);
		editorText.appendText("/" + "\n");
    }
    
    public void appendToEditorString(String paramName, String value) { //add single quotes for string
    	if (!value.equals("")) {
    		editorText.appendText("\t" + paramName + "='" + value + "'" + "\n");
    	}
    }
    
    public void appendToEditorNumber(String paramName, String value) { //no single quotes for numerical values
    	if (!value.equals("")) {
    		editorText.appendText("\t" + paramName + "=" + value + "\n");
    	}
    }
    
    public void appendToEditorBoolean(String paramName, String value) { //.. for logical values
    	if (!value.equals("")) {
    		editorText.appendText("\t" + paramName + "=." + value + "." + "\n");
    	}
    }
    
    public Statement getStatement() throws SQLException {
    	ConnectionClass connectionClass = new ConnectionClass();
    	Connection connection = connectionClass.getConnection();
    	Statement statement = connection.createStatement();
    	return statement;
    }
    

}
