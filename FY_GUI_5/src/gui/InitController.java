package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;

public class InitController implements Initializable{
	
	@FXML Button cancelBtn;
	@FXML Button catfBackBtn;
	@FXML Button printBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void goToTime(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Time.fxml"));
		Parent root = loader.load();
		
		TimeController timeCont = loader.getController(); //Get the next page's controller
		timeCont.showInfo(); //Set the values of the page 
		Scene timeScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(timeScene);
		mainWindow.show();
	}
	
	@FXML
	private void goToInit(ActionEvent event) throws IOException{ //NEXT SCENE
		//re-format the text
		boolean isCorrectFormat = true;

		//go to the next page
		if (isCorrectFormat){
			//Values.allStrings[13][0] = filesText.getText();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Init2.fxml"));
			Parent root = loader.load();
			
			Init2Controller initCont = loader.getController(); //Get the next page's controller
			//initCont.showInfo(); //Set the values of the page 
			Scene initScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(initScene);
			mainWindow.show();
		}
	}
	@FXML 
	private void cancelOption(ActionEvent event) throws IOException{ //CANCEL
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
	private void printFile(ActionEvent event) throws IOException{ //SAMPLE TESTING
		Values.printFile();
	}

	//To take values from Values and display them for the Catf page
	public void showInfo() {
		//filesText.setText(Values.allStrings[13][0]);
	}
	
	

}
