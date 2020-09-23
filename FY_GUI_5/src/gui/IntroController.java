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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IntroController implements Initializable{
	
	@FXML private Button introNextBtn;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void goToBasic(ActionEvent event) throws IOException{ //NEXT SCENE
		/*Parent introLayout = FXMLLoader.load(getClass().getResource("Basic.fxml")); //Get the next layout
		Scene basicScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
		
		
		mainWindow.setScene(basicScene);
		mainWindow.show();*/
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Basic.fxml"));
		Parent root = loader.load();
		BasicController basicCont = loader.getController(); //Get the next page's controller
		
		basicCont.showInfo(); //Set the values of the page //Values.chid, Values.title
		Scene basicScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(basicScene);
		mainWindow.show();
	}
	
	

	

}
