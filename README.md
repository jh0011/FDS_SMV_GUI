## Overview
This is a GUI to simplify the process of creating the .fds input file for the FDS_SMV simulator. Currently, the FDS_SMV simulator does not have a GUI to take in user input. The .fds file has to be manually written by the user. Thus, the GUI would make the input process more convenient. 

## Software
- Eclipse (Mars)
- SceneBuilder

The .fxml file is used to design the page. Each .fxml is linked to a Controller Java file, which implements the functionalities. The .fxml file is designed using SceneBuilder.

Download SceneBuilder: https://gluonhq.com/products/scene-builder/

## Before running
Before running the program, we need to change the PATH static final int in Values.java. This is the directory in which the .fds file would be created in. The path should be written in this format: "C:\\\Users\\\dell\\\Desktop\\\\".

## Features
- Each Namelist mentioned in the FDS user guide would have a .fxml and a Controller class to handle the user input for the multiple parameters.
- User input validity checking.
- User values would be maintained across the different Scenes, unless the user clicks on "Cancel".
- The output would be a .fds file created in the directory specified.
