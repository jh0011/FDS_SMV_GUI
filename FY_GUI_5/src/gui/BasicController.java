package gui;

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
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Controller class for Basic.fxml
 */
public class BasicController implements Initializable{
	/**
	 * Controller class for Basic.fxml
	 * 
	 */
	//head
	@FXML Button basicNextBtn;
	@FXML Button basicBackBtn;
	@FXML Button cancelBtn;
	@FXML TextField chidText = new TextField();
	@FXML TextArea titleText;
	
	@FXML Label headTitle;
	@FXML AnchorPane headAnchor;
	
	protected String chid;
	protected String title;
	
	static boolean isShowingHead = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException If database access error
	 * @throws IOException If page cannot be displayed
	 */
	@FXML
	public void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
	}
	
	/**
	 * Go to the previous page (INTRO) + input validation
	 * @param event Back button is clicked
	 * @throws IOException If page cannot be displayed
	 */
	@FXML
	public void goToIntro(ActionEvent event) throws IOException{ //PREVIOUS SCENE
		try {
			if (checkChid(chidText.getText())){
				String sql = "INSERT INTO head (CHID, TITLE) VALUES ('" + chidText.getText() + "', '" + titleText.getText() + "');";
				
				ConnectionClass connectionClass = new ConnectionClass();
				Connection connection = connectionClass.getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
				
				Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
				Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
				
				
				mainWindow.setScene(introScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Go to the next page (TIME) + input validation
	 * @param event Next button is clicked
	 * @throws IOException If page cannot be displayed
	 * @throws SQLException If database access error
	 */
	@FXML
	public void goToTime(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		try {
			if (checkChid(chidText.getText())){
				String sql = "INSERT INTO head (CHID, TITLE) VALUES ('" + chidText.getText() + "', '" + titleText.getText() + "');";
				
				ConnectionClass connectionClass = new ConnectionClass();
				Connection connection = connectionClass.getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Time.fxml"));
				Parent root = loader.load();
				
				TimeController timeCont = loader.getController(); //Get the next page's controller
				timeCont.showInfo(); //Set the values of the page //Values.T_END, Values.T_BEGIN, Values.DT
				Scene timeScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(timeScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Description of HEAD namelist
	 * @param event Open the description label
	 */
	@FXML
	public void openHeadDesc(MouseEvent event) {
		String content = "The HEAD namelist is a required value and is used to give the job a name. \n\n"
				+ "CHID: It is the character ID that will be used to name the .fds file. The extension \".fds\" and whitespaces should not "
				+ "be written. \n\nTITLE: Describes the simulation.";
		String namelist = "HEAD";
		Values.openDesc(namelist, content);
    }
	
	
	/**
	 * CHID input validation: <br>
	 * - If CHID is empty <br>
	 * - Whitespaces <br>
	 * - File extension <br>
	 * - Invalid symbols
	 * @param chid The user's CHID input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkChid(String chid){
		if (chid.equals("")){ //check whether CHID is filled
			Alert chidAlert = new Alert(Alert.AlertType.INFORMATION);
			chidAlert.setTitle("Empty CHID");
			chidAlert.setContentText("CHID is required.");
			chidAlert.setHeaderText(null);
			chidAlert.show();
			return false;
		}
		for (int i=0; i<chid.length(); i++){ //check for white spaces
			if (chid.charAt(i) == ' '){
				Alert chidAlert = new Alert(Alert.AlertType.INFORMATION);
				chidAlert.setTitle("Invalid CHID");
				chidAlert.setContentText("Whitespaces are not allowed in the CHID field");
				chidAlert.setHeaderText(null);
				chidAlert.show();
				return false;
			}
		}
		if (chid.contains(".fds")){ //check for .fds extension
			Alert extAlert = new Alert(Alert.AlertType.INFORMATION);
			extAlert.setTitle("Invalid CHID");
			extAlert.setContentText("The extension .fds does not need to be specified.");
			extAlert.setHeaderText(null);
			extAlert.show();
			return false;
		}
		if (chid.contains("|") || chid.contains("/") || chid.contains("\\") || chid.contains(":") ||
				chid.contains("?") || chid.contains("*") || chid.contains("<") || chid.contains(">") || chid.contains("\"")){ //check for invalid symbols
			Alert extAlert = new Alert(Alert.AlertType.INFORMATION);
			extAlert.setTitle("Invalid CHID");
			extAlert.setContentText("Invalid symbols in CHID");
			extAlert.setHeaderText(null);
			extAlert.show();
			return false;
		}
		//Values.allStrings[0][0] = chidText.getText();
		return true;
	}
	
	/**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException If database access error
	 */
	public void showInfo() throws SQLException{ //String chid, String title
		String sql = "SELECT * FROM head";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()){
			chidText.setText(rs.getString(1));
			titleText.setText(rs.getString(2));
		}
	}
}
