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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PresController implements Initializable{
	//pres
	@FXML TextField fishpakText;
    @FXML ComboBox solverCombo;
    
    static String solverSelection = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> solverList = FXCollections.observableArrayList("", "FFT", "UGLMAT", "GLMAT");
		solverCombo.setItems(solverList);
	}
	
	@FXML
    private void cancelOption(ActionEvent event) throws IOException, SQLException { //CANCEL
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
    private void goToMult(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	//store the values
    	storeValues();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Mult.fxml"));
		Parent root = loader.load();
		
		MultController multCont = loader.getController(); //Get the next page's controller
		multCont.showInfo(); //Set the values of the page 
		Scene multScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(multScene);
		mainWindow.show();
    }

    @FXML
    private void solverSelect(ActionEvent event) {
    	solverSelection = solverCombo.getSelectionModel().getSelectedItem().toString();
    	solverCombo.setValue(solverSelection);
    }
    
    private void doChecking() { 
    	//no checking for pres
    	
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesPres();
    }
    
    private void storeValuesPres() throws SQLException { //store PRES values into the database
    	String sqlPres = "INSERT INTO pres VALUES ('" + fishpakText.getText() + "', '" + solverSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlPres);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoPres();
    }
    
    protected void showInfoPres() throws SQLException { //to show the info when the page is loaded
    	String sqlPres = "SELECT * FROM pres;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlPres);
		while (rs.next()) {
			fishpakText.setText(rs.getString(1));
			solverSelection = rs.getString(2);
			solverCombo.setValue(solverSelection);
		}
    }

}
