# minesweeper-gui
 A simple JavaFX GUI of the Minesweeper game

**JAR file can be found under `\docs\jar\`.**
--
### Instructions on how to execute JAR file
#### 1) You must have the following installed on your computer:
- [Java Runtime Environment](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html)
- [JavaFX](https://openjfx.io/)

#### 2) Running the file
- Install the necessary software above.
- Take note of the directory path where you install JavaFX, specifically the `lib` folder.
- In your command line, go to the directory where you download the JAR file.
- Type in this command: `java --module-path` _path-to-javafx-lib_ `--add-modules javafx.controls,javafx.graphics -jar MinesweeperGUI.jar` (view footnote)
- Press 'Enter' and play!

*_Example_: Let's say the path to your JavaFX lib file is: `C:\javafx\javafx-sdk-11.0.2\lib`*    
*The command you will put in will be: `java --module-path \javafx\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.graphics -jar MinesweeperGUI.jar`*

