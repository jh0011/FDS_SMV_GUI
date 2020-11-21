## Overview
This GUI is a Java Desktop Application to simplify the process of creating the .fds input file for the FDS_SMV simulator. Currently, the FDS_SMV simulator does not have a GUI to take in user input. The .fds file has to be manually written by the user. Thus, the GUI would make the input process more convenient. Due to the extensive number of parameters in each namelist, only the more commonly used parameters are added into the GUI. The remaining parameters would need to be manually inputted by the user after the .fds file has been created.

## Software
- Eclipse (2020-09)
- SceneBuilder
- MySQL database 

The .fxml file is used to design the page. Each .fxml is linked to a Controller Java file, which implements the functionalities. The .fxml file is designed using SceneBuilder. The user inputs are stored in the database.

Download Eclipse IDE: https://www.eclipse.org/downloads/ 

Download SceneBuilder: https://gluonhq.com/products/scene-builder/  

Download MySQL Workbench: https://dev.mysql.com/downloads/workbench/ 

## Before running
Before running the program, we need to change the PATH static final int in Values.java. This is the directory in which the .fds file would be created in. The path should be written in this format: "C:\\\Users\\\dell\\\Desktop\\\\".
The connection to the database on localhost would also need to be established.

## Features
- Each Namelist mentioned in the FDS user guide would have a .fxml and a Controller class to handle the user input for the multiple parameters.
- User input validity checking.
- User values would be maintained across the different Scenes, unless the user clicks on "Cancel".
- The output would be a .fds file created in the directory specified.
- Multiple lines for each namelist can be added, if required.
