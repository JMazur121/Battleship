# Battleship

## Description
Simple implementation of well-known Battleship game in JavaFX. Project is based on client-server architecture. It allows to create separate games for 2 players. Program uses TCP protocol for communication.
Project done for academic purpose for Warsaw University of Technology.
## Build
### Tools
* Java version 1.8
* Gradle tool

### Building
1. Download this repository.
2. Open the main directory containing 'gradle.build' file.
3. Use 'gradle build' command to build the project.
4. Go to the build directory 'build/libs'.
5. JAR file 'Battleship-1.0' should be located here.

## Running the application

Open 'Battleship-1.0' by typing `java -jar Battleship-1.0.jar` or simply click twice on it.
You should get the following result : 
![Alt text](https://github.com/ToLiveisToCode/Battleship/blob/master/src/main/resources/main.png "Main window")

### Server
1. Click on the first button named "Serwer". You shuld get the following result: 
####
![Alt text](https://github.com/ToLiveisToCode/Battleship/blob/master/src/main/resources/server.png "Server window")
###
2. In the TextField type your port number. Port number can be any free tcp port (Try to use high numbers).
3. Press 'Start' button. Connection will be established and the 'Start' button will be disabled.
4. Number of active players is shown in the second TextField.
5. To stop the server, press 'Stop' button. Server and all clients will exchange information about shutdown. The number of active players should drop to 0.
6. Press the last button to close the application.

### Client
1. In the main window press the second button named "Klient". You should get the following result:
####
![Alt text](https://github.com/ToLiveisToCode/Battleship/blob/master/src/main/resources/client1.png "Client window")
###
2. Type server IP adress in the first TextField. Use "0.0.0.0" format.
3. Type server port number in the second TextField. ( You shuould type the same number you used during server opening).
4. Press the button to connect with the server.
* If the server is unreachable a suitable message is shown in the dialog.
4. When the connection is established, you shuld get the following result:
####
![Alt text](https://github.com/ToLiveisToCode/Battleship/blob/master/src/main/resources/client2.png "Client window")
### UI Description
1. Player board. You can place your ships here. 
2. Enemy board. You can choose an enemy cell to shoot here.
3. Nick field. Type your name here and press the button named "Zaloguj".
4. Game field. A name of your current game will be shown here.
5. A list of games you can join.
6. Join button. Choose the game name from the list and press the button to join the game.
7. Create button. Press this button and type the game name in the dialog to create your own game.
8. Delete button. Press this button to delete the game you own. The second player will be informed about your decision.
9. Abandon button. Press this button to abandon your current game. 
* If you are the owner of the game, your guest will become a host after your leaving.
* If there is no guest, the game will be deleted.
10. TextArea. Statements form server will be shown here.
11. ChatArea. Messages from your oppent will be shown here.
12. ChatField. Type your message and press the button to send it to your opponent.
13. Invite button. Use this button to propose a new game.
14. Ready buttons. Once you placed all your ships, press the second button to inform your opponent. After that, ship placement will be disabled.
15. Size buttons. Use this buttons to choose the size of a ship to place. Once you placed all the ships in a particular size, the proper button will be disabled.
16. Remove button. Use this button to remove a ship from your board. This is a toggle button so don't forget to press it again when you want to place a ship.
17. GiveUp button. Use this button to inform your opponent about your resignation.

### Tips for ship placing and shooting
1. To place your ship, click on a specified cell. Use LeftMouseButton to place your ship vertically and RightMouseButton to place it horizontally.
2. To shoot, click on a specified cell in the enemy board.

### Closing
#### To close the application, press 'X' button in the top right-hand corner. A suitable information will be sent to the server and to your opponent.
