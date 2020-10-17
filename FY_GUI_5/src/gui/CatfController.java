package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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

public class CatfController implements Initializable{
	
	@FXML Button cancelBtn;
	@FXML Button catfBackBtn;
	@FXML TextArea filesText;
	@FXML Button printBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void goToTime(ActionEvent event) throws IOException{ //PREVIOUS SCENE
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
		if (!filesText.getText().equals("")){
			isCorrectFormat = formatText(filesText.getText());
		}
		
		//go to the next page
		if (isCorrectFormat){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Init.fxml"));
			Parent root = loader.load();
			
			InitController initCont = loader.getController(); //Get the next page's controller
			//timeCont.showInfo(); //Set the values of the page 
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
	
	private boolean formatText(String text){
		String[] files = text.split("\\n");
		String concatFiles = "";
		for (int i=0; i<files.length; i++){
			//check for file extension
			if (!files[i].contains(".")){
				Alert filesAlert = new Alert(Alert.AlertType.INFORMATION);
				filesAlert.setTitle("Files title format");
				filesAlert.setContentText("The files require a file extension.");
				filesAlert.setHeaderText(null);
				filesAlert.show();
				return false;
			}
			if (i == 0){
				concatFiles = concatFiles + files[i] + "', ";
			}
			if (i <= files.length - 2){
				concatFiles = concatFiles + "'" + files[i] + "', ";
			}
			else{
				concatFiles = concatFiles + "'" + files[i];
			}
		}
		Values.allStrings[15][0] = concatFiles;
		return true;
	}

	//To take values from Values and display them for the Catf page
	public void showInfo() {
		filesText.setText(Values.allStrings[15][0]);
	}
	
	

}
