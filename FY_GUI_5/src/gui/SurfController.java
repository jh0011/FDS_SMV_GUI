package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SurfController implements Initializable{
	//surf
    @FXML TextField idText; //string
    @FXML TextField partIdText; //string
    @FXML TextField matlIdText; //string
    @FXML TextField velText; //float (+ / -)
    @FXML TextField tmpFrontText; //float (+)
    @FXML ComboBox backingCombo;
    @FXML ComboBox defaultCombo;
    @FXML ComboBox geometryCombo;
    @FXML TextField colourText; //string
    @FXML TextField hrrpuaText; //float (+)
    
    @FXML Button addPropBtn;
    
    boolean checkFloatPos;
    boolean checkFloat;
    
    static String backingSelection = "";
    static String defaultSelection = "";
    static String geometrySelection = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
    private void cancelOption(ActionEvent event) throws SQLException, IOException { //CANCEL
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
    private void goToDevc(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Devc.fxml"));
		Parent root = loader.load();
		
		DevcController devcCont = loader.getController(); //Get the next page's controller
		devcCont.showInfo(); //Set the values of the page 
		Scene devcScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(devcScene);
		mainWindow.show();
    }

}
