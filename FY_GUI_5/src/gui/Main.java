package gui;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import connectivity.ConnectionClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Values.initValues();
			Parent root;
			root = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Should be the actual line
			//root = FXMLLoader.load(getClass().getResource("Devc.fxml")); //For testing a certain page
			Scene introScene = new Scene(root, 870, 710);
			primaryStage.setScene(introScene);
			primaryStage.setTitle("FDS-SMV GUI");
			primaryStage.show();
			initDB();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDB(){
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			//delete the table
			String sqlHead = "DELETE FROM head;";
			String sqlTime = "DELETE FROM time;";
			String sqlCatf = "DELETE FROM catf;";
			String sqlInit = "DELETE FROM init;";
			String sqlMesh = "DELETE FROM mesh;";
			String sqlPart = "DELETE FROM part;";
			String sqlBndf = "DELETE FROM bndf;";
			String sqlProp = "DELETE FROM prop;";
			String sqlSpec = "DELETE FROM spec;";
			String sqlDevc = "DELETE FROM devc;";
			String sqlSlcf = "DELETE FROM slcf;";
			
			//insert an empty row
			String initHead = "INSERT INTO head(CHID, TITLE) VALUES ('', '');";
			String initTime = "INSERT INTO time (EndTime, StartTime, DT) VALUES ('', '', '');";
			String initCatf = "INSERT INTO catf(OTHER_FILES) VALUES ('');";
			String initInit = "INSERT INTO init(mainID, idText, partIdText, specIdText, npartText, "
					+ "npartCellText, massTimeText, massVolText, massFracText, xbText) "
					+ "VALUES ('1', '', '', '', '', '', '', '', '', '')";
			String initMesh = "INSERT INTO mesh (mainID, ijkText, xbText) VALUES ('', '', '');";
			String initPart = "INSERT INTO part (mainID, SURF_ID, SPEC_ID, PROP_ID, QUANTITIES, STATIC" + 
					", MASSLESS, SAMPLING_FACTOR, DIAMETER, ID) VALUES ('1', '', '', '', '', '', '', '', '', '');";
			String initBndf = "INSERT INTO bndf (mainID, QUANTITY) VALUES ('1', '');";
			String initProp = "INSERT INTO prop (mainID, ID, PART_ID, QUANTITY, SMOKEVIEW_ID, OFFSET, PDPA_INTEGRATE, PDPA_NORMALIZE"
					+ ", OPERATING_PRESSURE, PARTICLES_PER_SECOND, PARTICLE_VELOCITY) VALUES ('1', '', '', '', '', '', '', '', '', '', '');";
			String initSpec = "INSERT INTO spec (mainID, ID, BACKGROUND) VALUES ('1', '', '');";
//			//String initDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('1', '', '', '', '', '', 'Select one (optional)', '');";
			String initDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('1', '', '', '', '', '', '', '');";
			String initSlcf = "INSERT INTO slcf (mainID, QUANTITY, SPEC_ID, PBY, PBZ, PBX, VECTOR) VALUES ('1', '', '', '', '', '', '');";
			
			Statement statement;
			statement = connection.createStatement();
			statement.executeUpdate(sqlHead);
			statement.executeUpdate(sqlTime);
			statement.executeUpdate(sqlCatf);
			statement.executeUpdate(sqlInit);
			statement.executeUpdate(sqlMesh);
			statement.executeUpdate(sqlPart);
			statement.executeUpdate(sqlBndf);
			statement.executeUpdate(sqlProp);
			statement.executeUpdate(sqlSpec);
			statement.executeUpdate(sqlDevc);
			statement.executeUpdate(sqlSlcf);
			
			statement.executeUpdate(initHead);
			statement.executeUpdate(initTime);
			statement.executeUpdate(initCatf);
			statement.executeUpdate(initInit);
			statement.executeUpdate(initMesh);
			statement.executeUpdate(initPart);
			statement.executeUpdate(initBndf);
			statement.executeUpdate(initProp);
			statement.executeUpdate(initSpec);
			statement.executeUpdate(initDevc);
			statement.executeUpdate(initSlcf);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("DATABASE NOT SET CORRECTLY");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
