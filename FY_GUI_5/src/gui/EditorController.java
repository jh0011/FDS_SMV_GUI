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
	static String path = "";
	static String CHID = "";
	static FileWriter fw;
	static BufferedWriter bw = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
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
    public void saveFile(ActionEvent event) throws IOException {
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
    	printValuesHead();
    	editorText.appendText("\n" + "&TAIL /");
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
		appendToEditor("CHID", CHID);
		appendToEditor("TITLE", TITLE);
		editorText.appendText("/");
    }
    
    public void appendToEditor(String paramName, String value) {
    	editorText.appendText("\t" + paramName + "='" + value + "'" + "\n");
    }
    
    public Statement getStatement() throws SQLException {
    	ConnectionClass connectionClass = new ConnectionClass();
    	Connection connection = connectionClass.getConnection();
    	Statement statement = connection.createStatement();
    	return statement;
    }

}
