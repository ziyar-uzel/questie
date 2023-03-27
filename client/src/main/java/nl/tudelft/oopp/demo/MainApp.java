package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.views.LauncherDisplay;

// When starting application and joining a room it given the following error:
// Caused by: java.net.ConnectException: Connection refused: no further information
// After closing pop up with "fails to connect with server" the studentview still opens

public class MainApp {
    public static void main(String[] args) {
        LauncherDisplay.main(new String[0]);
    }
}
