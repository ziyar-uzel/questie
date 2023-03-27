package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Room;



public class LauncherController {

    @FXML
    private Button create;

    @FXML
    private Button join;

    @FXML
    private TextField code;

    @FXML
    private TextField username;

    @FXML
    private ImageView imageName;

    public static String roomCode;

    @FXML
    private DatePicker openDate;

    @FXML
    private TextField openTime;


    /**
     * Handles clicking the create button.
     * Gets the room id
     */
    public void createRoom() {
        Room newRoom = null;
        String response;
        try {
            String openTimeString = openTime.getText();
            LocalTime openLocalTime = LocalTime.parse(openTimeString);
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong format");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Error! The time is in a wrong format, enter as hh:mm (e.g. 09:15)");
            alert.showAndWait();
            return;
        }
        try {
            String openTimeString = openTime.getText();
            LocalDate openLocalDate = openDate.getValue();
            LocalTime openLocalTime = LocalTime.parse(openTimeString);
            LocalDateTime openDateTime = openLocalDate.atTime(openLocalTime);
            response = ServerCommunication.getRoomOpenTime(openDateTime);
            newRoom = new Room(response.substring(7,15));
        } catch (NullPointerException e) {
            System.out.println("Construct with default opening time");
            response = ServerCommunication.getRoom();
            newRoom = new Room(response.substring(7,15));
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/presentingView.fxml");
            loader.setLocation(xmlUrl);
            Parent root1 = loader.load();

            //Presenting controller instance is created is declared to
            // call the init method that updates the lecture pace feedback field
            PresentingController presentingController = loader.getController();
            presentingController.init();

            Stage stage = new Stage();
            stage.setTitle(newRoom.getId() + " - " + response.substring(32,37));
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
            System.out.println("Can't load new window");
        }

    }

    /**
     * Handles clicking the join button.
     * Gets the room info + authorization
     */
    public void joinRoom() {
        roomCode = code.getText();

        String result = ServerCommunication.joinRoom(roomCode, username.getText());
        System.out.println(result);
        if (result.equals("The room is not open yet!")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not open yet");
            alert.setHeaderText(null);
            alert.setContentText("Error! This room is not open yet.");
            alert.showAndWait();
            return;
        }
        if (result.equals("Invalid room code!")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("username and room");
            alert.setHeaderText(null);
            alert.setContentText("Error! Invalid credentials"); // get Room ID
            alert.showAndWait();
            return;
        }

        if (roomCode.contains("@")) {
            System.out.println("Joining as Mod");
            try {
                FXMLLoader loader = new FXMLLoader();
                URL xmlUrl = getClass().getResource("/presentingView.fxml");
                loader.setLocation(xmlUrl);
                Parent root1 = loader.load();

                PresentingController presentingController = loader.getController();
                presentingController.init();

                Stage stage = new Stage();
                stage.setTitle(roomCode.substring(0,8) + " - " + roomCode.substring(9));
                stage.setScene(new Scene(root1));
                stage.show();

            } catch (Exception e) {
                System.out.println("Can't load new window");
            }
        } else {
            System.out.println("Joining as Student");
            try {
                FXMLLoader loader = new FXMLLoader();
                URL xmlUrl = getClass().getResource("/studentView.fxml");
                loader.setLocation(xmlUrl);
                Parent root1 = loader.load();

                StudentController studentController = loader.getController();
                studentController.init();

                Stage stage = new Stage();
                stage.setTitle(username.getText() + " : " + code.getText());
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                System.out.println("Can't load new window");
            }
        }


    }

}
