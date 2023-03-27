package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

import java.util.TimerTask;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;






public class PresentingController {

    @FXML
    protected ListView questions;

    @FXML
    private Button leave;

    @FXML
    private Button toBrowsing;

    @FXML
    private Button refresh;

    @FXML
    private Text roomName;

    @FXML
    private TextField roomId;

    @FXML
    private Button getRoomId;

    @FXML
    private TextField modCode;

    @FXML
    private Button getMod;

    @FXML
    private Button delete;

    @FXML
    private Button openRoom;

    @FXML
    private Button closeRoom;

    @FXML
    private Button mark;

    @FXML
    private Label tooSlowCount;

    @FXML
    private Label tooFastCount;

    @FXML
    private Button download;

    public boolean closed = false;

    /**
     * Handles clicking the get ID button.
     * Displays the roomID in the textField
     */
    public void getRoomId() {
        Stage stage = (Stage) leave.getScene().getWindow();
        String roomCode = stage.getTitle().substring(0,8);
        roomId.setText(roomCode);
        roomId.setEditable(false);
    }

    /**
     * Handles clicking the get Mod button.
     * Displays the modPassword in the textField
     */
    public void getModCode() {
        Stage stage = (Stage) leave.getScene().getWindow();
        String modPassword = "@" + stage.getTitle().substring(11,16);
        modCode.setText(modPassword);
        modCode.setEditable(false);
    }

    /**
     * Handles clicking the leave button.
     * Closes the window
     */
    public void leave() {
        Stage stage = (Stage) leave.getScene().getWindow();
        closed = true;
        stage.close();
    }

    /**
     * Handles clicking the Browsing view button.
     * Moves to browsing view
     */
    public void toBrowsing() {

        try {
            Parent browsingViewParent;
            browsingViewParent = FXMLLoader.load(getClass().getResource("/browsingView.fxml"));

            Stage stage = (Stage) leave.getScene().getWindow();
            stage.setScene(new Scene(browsingViewParent));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new page");
        }

    }

    /**
     * Handles clicking the refresh button.
     * Lists the top 5 upvoted questions
     */
    public void getTopQuestions() {

        try {
            Stage stage = (Stage) leave.getScene().getWindow();
            String roomId = stage.getTitle().substring(0, 8);

            List<Question> result = ServerCommunication.sortQuestions(roomId, "Score", false);

            if (result.size() > 5) {
                result = result.subList(0, 5);
            }

            System.out.println(questions);
            System.out.println(result);

            // Customize the cells to allow wrapping
            questions.setCellFactory(param -> new ListCell<Question>() {
                @Override
                protected void updateItem(Question item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        setMinWidth(538.0);
                        setMaxWidth(538.0);
                        setPrefWidth(538.0);

                        setWrapText(true);

                        setText(item.toString());

                    }
                }
            });

            questions.getItems().clear();
            questions.getItems().addAll(result);

        } catch (NullPointerException ex) {
            System.out.println("There is no new question");
        }

    }

    /**
     * Handles clicking the delete button.
     * Deletes the selected question
     */
    public void deleteQuestion() {

        try {
            Stage stage = (Stage) leave.getScene().getWindow();
            Question question = (Question) questions.getSelectionModel().getSelectedItem();
            long questionID = question.getId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting question");
            alert.setHeaderText("Deleting a question!");
            alert.setContentText("Are you sure you want to delete this student's question?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                ServerCommunication.deleteOwnQuestion(questionID);
            }
        } catch (Exception e) {
            noQuestionSelection();
        }

    }

    /**
     * Method called when user tries to click the delete/upvote button without
     * a selected question.
     * Alerts the user that no question has been selected.
     */
    public void noQuestionSelection() {
        Alert selectAlert = new Alert(Alert.AlertType.INFORMATION);
        selectAlert.setTitle("No question selected.");
        selectAlert.setHeaderText(null);
        selectAlert.setContentText("You must select a question!");
        selectAlert.showAndWait();
    }

    /**
     * Handles clicking the 'mark a answered' button.
     * Marks a selected question as answered
     */
    public void markAsAnswered() {
        try {
            Stage stage = (Stage) leave.getScene().getWindow();
            Question question = (Question) questions.getSelectionModel().getSelectedItem();
            long questionID = question.getId();
            ServerCommunication.markQuestion(questionID);
        } catch (Exception e) {
            noQuestionSelection();
        }
    }

    /**
     * Handles opening the room.
     * Puts opening time to min time
     */
    public void enableJoin() {
        Stage stage = (Stage) leave.getScene().getWindow();
        String roomId = stage.getTitle().substring(0, 8);
        ServerCommunication.enableJoin(roomId);
    }

    /**
     * Handles closing a room.
     * Puts opening time to max time
     */
    public void disableJoin() {
        Stage stage = (Stage) leave.getScene().getWindow();
        String roomId = stage.getTitle().substring(0, 8);
        ServerCommunication.disableJoin(roomId);
    }
    /**
     * Displays the lecture pace feedback numbers.
     */

    public void showLecturePaceFeedback() {

        try {
            Stage stage = (Stage) leave.getScene().getWindow();
            int[] lecturePaceFeedback = ServerCommunication
                    .getLecturePaceFeedback(stage.getTitle().substring(0,8));

            tooFastCount.setText(String.valueOf(lecturePaceFeedback[0]));
            tooSlowCount.setText(String.valueOf(lecturePaceFeedback[1]));
        } catch (NullPointerException exception) {
            System.out.println("Lecture feedback array is empty for now");
        }
    }

    /**
     * Handles clicking the download button.
     * Downloads all questions in a room
     */
    public void downloadQuestions() {
        Stage stage = (Stage) leave.getScene().getWindow();
        String roomCode = stage.getTitle().substring(0,8);
        ServerCommunication.downLoadRoom(roomCode);
    }

    /** Updates lecture pace feedback and questions in every 6.42 seconds.
    */
    public void init() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (closed == false) {
                            showLecturePaceFeedback();
                            getTopQuestions();
                        } else {
                            timer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 6420);


    }


    /**
     * Handles clicking the close button.
     * Closes the room that the user currently in.
     */
    public void closeRoom() {

        try {
            Stage stage = (Stage) leave.getScene().getWindow();

            String roomId = stage.getTitle().substring(0,8);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Closing the Room");
            alert.setHeaderText("You are about to close this room.");
            alert.setContentText("Are you sure you want to close this room?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                closed = true;
                ServerCommunication.closeRoomById(roomId);
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
