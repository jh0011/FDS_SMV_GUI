package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class TimeController implements Initializable{
	
	@FXML TextField endTimeText;
	@FXML Button timeBackBtn;
	@FXML Button timeNextBtn;
	@FXML Button cancelBtn;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void goToBasic(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Basic.fxml"));
		Parent root = loader.load();
		BasicController newBasic = loader.getController(); //Get the previous page's controller
		
		newBasic.showInfo(Values.chid, Values.title); //Set the values of the page
		Scene basicScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(basicScene);
		mainWindow.show();
		
	}
	
	
	
	protected void showInfo(String endTime){
		endTimeText.setText(endTime);
	}
	

}
