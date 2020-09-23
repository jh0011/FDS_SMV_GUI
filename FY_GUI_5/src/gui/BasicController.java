package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class BasicController implements Initializable{
	
	@FXML Button basicNextBtn;
	@FXML Button basicBackBtn;
	@FXML Button cancelBtn;
	@FXML TextField chidText = new TextField();
	@FXML TextArea titleText;
	
	protected String chid;
	protected String title;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void goToTime(ActionEvent event) throws IOException{ //NEXT SCENE
		if (checkChid(chidText.getText())){
			/*Values.chid = chidText.getText();
			Values.title = titleText.getText();*/
			Values.allStrings[0][0] = chidText.getText();
			Values.allStrings[1][0] = titleText.getText();
			
			/*Parent timeLayout = FXMLLoader.load(getClass().getResource("Time.fxml")); //Get the next layout
			Scene timeScene = new Scene(timeLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			mainWindow.setScene(timeScene);
			mainWindow.show();
			
			*/
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Time.fxml"));
			Parent root = loader.load();
			
			TimeController timeCont = loader.getController(); //Get the next page's controller
			timeCont.showInfo(); //Set the values of the page //Values.T_END, Values.T_BEGIN, Values.DT
			Scene timeScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(timeScene);
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
	private void goToIntro(ActionEvent event) throws IOException{
		Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
		Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
		
		
		mainWindow.setScene(introScene);
		mainWindow.show();
		
	}
	
	
	private boolean checkChid(String chid){
		for (int i=0; i<chid.length(); i++){
			if (chid.charAt(i) == ' '){
				Alert chidAlert = new Alert(Alert.AlertType.INFORMATION);
				chidAlert.setTitle("Invalid CHID");
				chidAlert.setContentText("Whitespaces are not allowed in the CHID field");
				chidAlert.setHeaderText(null);
				chidAlert.show();
				return false;
			}
		}
		return true;
	}
	

	
	protected void showInfo(){ //String chid, String title
		chidText.setText(Values.allStrings[0][0]);
		titleText.setText(Values.allStrings[1][0]);
	}
	


}
