package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;




public class StudentController {

    @FXML
    private ListView questions;

    @FXML
    private Button tooSlow;

    @FXML
    private Button tooFast;

    @FXML
    private ChoiceBox sortBy;

    @FXML
    private Button post;

    @FXML
    private Button leaveRoom;

    @FXML
    private TextArea questionToBePosted;

    @FXML
    private Button refresh;

    @FXML
    private Button delete;

    @FXML
    private Button upvote;

    private int tooFastClicks = 0;

    private int tooSlowClicks = 0;

    /**
     * Handles clicking the post button.
     * Posts the question that is written down in the textfield
     */

    public void postQuestion() {
        String content = questionToBePosted.getText();
        Stage stage = (Stage) post.getScene().getWindow();
        String username = stage.getTitle().substring(0, stage.getTitle().length() - 11);;
        ServerCommunication.postQuestion(content, username);
    }

    /**
     * Handles clicking the Leave Room button.
     * Closes the window
     */

    public void leaveRoom() {
        Stage stage = (Stage) leaveRoom.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles clicking the refresh button.
     * Fetches the questions from the server
     */
    public void getQuestions() {
        try {
            Stage stage = (Stage) leaveRoom.getScene().getWindow();
            String roomId = stage.getTitle().substring(stage.getTitle().length() - 8);

            List<Question> result = ServerCommunication.findQuestions(roomId);
            System.out.println(questions);
            System.out.println(result);

            questions.getItems().clear();
            questions.getItems().addAll(result);
        } catch (NullPointerException exception) {
            System.out.println("There is no new question");
        }

    }

    /**
     * Handles clicking the delete button.
     * Deletes the selected question in the ListView
     */
    public void deleteQuestion() {

        Stage stage = (Stage) post.getScene().getWindow();
        String username = stage.getTitle().substring(0, stage.getTitle().length() - 11);
        try {
            Question ownQuestion = (Question) questions.getSelectionModel().getSelectedItem();
            long questionID = ownQuestion.getId();
            String studentID = ownQuestion.getUsername();

            if (studentID.equals(username)) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Deleting question");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this question?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ServerCommunication.deleteOwnQuestion(questionID);
                }

            } else {
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Deleting question");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("Error! This isn't your question!");
                alertInfo.showAndWait();
            }

        } catch (Exception e) {
            noQuestionSelection();
        }

    }

    /**
     * Handles clicking the upvote button.
     * Upvotes the selected question.
     */
    public void upvoteQuestion() throws Exception {

        try {
            Question ownQuestion = (Question) questions.getSelectionModel().getSelectedItem();
            long questionID = ownQuestion.getId();
            ServerCommunication.voteQuestion(questionID);
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
     * Handles clicking the too fast! button.
     *
     */

    public void slowDown() {


        try {
            tooFastClicks++;
            if (tooFastClicks % 2 == 0) {
                tooFast.setStyle("");
            } else {
                if (tooSlowClicks % 2 != 0) {
                    tooSlowClicks++;
                    tooSlow.setStyle("");
                }
                tooFast.setStyle("-fx-background-color: #808080");
            }
            ServerCommunication.upvoteLecturePace();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Handles clicking the too slow! button.
     *
     */
    public void speedUp() {

        try {
            tooSlowClicks++;

            if (tooSlowClicks % 2 == 0) {
                tooSlow.setStyle("");
            } else {
                if (tooFastClicks % 2 != 0) {
                    tooFastClicks++;
                    tooFast.setStyle("");
                }
                tooSlow.setStyle("-fx-background-color: #808080");
            }
            ServerCommunication.downVoteLecturePace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * closes the room with a pop-up.
     */
    public void alertAndClose(String cause) throws IOException {

        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        String title;
        String headerText;
        switch (cause.toLowerCase()) {
            case("closure"):
                title = "Room Closure";
                headerText = "This room is closed";
                break;

            case("banned"):
                title = "Banned";
                headerText = "You have been banned from this room.";
                break;

            default:
                title = "Room Closure";
                headerText = "This room is closed.";
        }
        Stage stage = (Stage) post.getScene().getWindow();
        alertInfo.setTitle(title);
        alertInfo.setHeaderText(headerText);
        stage.close();
        alertInfo.showAndWait();
    }


    /**
     * this method, in every 6.42 seconds, checks whether the
     * room is closed or not, if it is not closed it will call the getQuestions method
     * ,acting as an auto-refresh, if the room is closed then it will call the alertandClose
     * method and close the student view.
     */

    public void init() {
        Timer studentTimer = new Timer();
        studentTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (checkRoomClosed()) {
                            studentTimer.cancel();
                            alertAndClose("closure");
                        } else if (checkUserBanned()) {
                            studentTimer.cancel();
                            alertAndClose("banned");
                        } else {
                            getQuestions();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 6420);

    }

    private boolean checkRoomClosed() {
        Stage stage = (Stage) leaveRoom.getScene().getWindow();
        String roomId = stage.getTitle().substring(stage.getTitle().length() - 8);
        boolean roomIsClosed = ServerCommunication.checkRoomClosed(roomId);
        if (roomIsClosed) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUserBanned() {
        Stage stage = (Stage) leaveRoom.getScene().getWindow();
        boolean userIsBanned = ServerCommunication.checkUserBanned();
        return userIsBanned;
    }
}
