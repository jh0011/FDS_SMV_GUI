# Overview
This GUI is a Java Desktop Application to simplify the process of creating the .fds input file for the FDS_SMV simulator. Currently, the FDS_SMV simulator does not have a GUI to take in user input. The .fds file has to be manually written by the user. Thus, the GUI would make the input process more convenient. Due to the extensive number of parameters in each namelist, only the more commonly used parameters are added into the GUI. The remaining parameters would need to be manually inputted by the user after the .fds file has been created.

# Software
- Eclipse (2020-09)
- SceneBuilder
- MySQL database 

The .fxml file is used to design the page. Each .fxml is linked to a Controller Java file, which implements the functionalities. The .fxml file is designed using SceneBuilder. The user inputs are stored in the database.

Download Eclipse IDE: https://www.eclipse.org/downloads/ 

Download SceneBuilder: https://gluonhq.com/products/scene-builder/  

Download MySQL Workbench: https://dev.mysql.com/downloads/workbench/ 

# Before running
## MySQL Connection

The connection to the database on localhost would also need to be established. These are the setup steps.

1. Create a MySQL connection in MySQL workbench. The default username is "root" and default password is "root". 

2. Create a new schema in MySQL workbench and name it as "fds_db".

3. Open the fds_db.sql script into MySQL workbench.

4. Click on the fds_db schema and execute the SQL script.

#### Default values used for MySQL database
Username: root

Password: root

Database name: fds_db

**If any of these values are not used, be sure to update the variables in ConnectionClass.java (within the Connectivity package).**

## Importing the project into Eclipse

## Running the generated .fds input file

## VM Arguments
--module-path "C:\Users\dell\Documents\javaFX\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml

# Features
- Each Namelist mentioned in the FDS user guide would have a .fxml and a Controller class to handle the user input for the multiple parameters.
- User input validity checking.
- User values would be maintained across the different Scenes, unless the user clicks on "Cancel".
- The output would be a .fds file created in the directory specified.
- Multiple lines for each namelist can be added, if required.
