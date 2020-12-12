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
import java.util.ResourceBundle;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
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
    
    @FXML
    public void saveFile(ActionEvent event) throws IOException { //save to the file system
    	//store the updated text area
    	tempEditorString = editorText.getText();
    	
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setTitle("Save FDS input file");
    	File selectedDirectory = directoryChooser.showDialog(null);
    	System.out.println(selectedDirectory.getAbsolutePath());
    	File outputFile = new File(selectedDirectory + "\\" + CHID + ".fds");
    	if (!outputFile.exists()){
			outputFile.createNewFile();
		}
    	fw = new FileWriter(outputFile);
		bw = new BufferedWriter(fw);
		bw.write(editorText.getText());
		bw.close();
    }
    
    public void showFile() throws SQLException {
    	if (!tempEditorString.equals("")) {
    		editorText.setText(tempEditorString);
    	}
    	else {
    		printValuesHead();
    		printValuesMesh();
    		printValuesTime();
    		printValuesInit();
    		printValuesSurf();
    		editorText.appendText("&TAIL /");
    	}
    	tempEditorString = editorText.getText();
    }
    
    public boolean compareText(TextArea text1, TextArea text2) {
    	if (text1.getText().equals(text2.getText())) {
    		return true;
    	}
    	else {
    		return false;
    	}
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
    	String sqlMesh = "SELECT mainID FROM MESH GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMesh);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	//print each MESH line
    	String IJK = "";
    	String XB = "";
    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
    		sqlMesh = "SELECT * FROM MESH WHERE mainID='" + i + "';";
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
    	String sqlInit = "SELECT mainID FROM INIT GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlInit);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
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
    		sqlInit = "SELECT * FROM INIT WHERE mainID='" + i + "';";
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
    }
    
    public void printValuesSurf() throws SQLException { //surf
    	//get the number of SURF lines
    	String sqlSurf = "SELECT mainID FROM SURF GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSurf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	//print each INIT line
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
    		sqlSurf = "SELECT * FROM SURF WHERE mainID='" + i + "';";
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
