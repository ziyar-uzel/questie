package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;



public class BrowsingController {

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
    private Button delete;

    @FXML
    private Button mark;

    @FXML
    private Button banUser;

    @FXML
    private Label sortLabel;

    @FXML
    private MenuButton menuButton;

    @FXML
    private MenuItem m1;

    @FXML
    private MenuItem m2;

    @FXML
    private MenuItem m3;

    @FXML
    private CheckBox checkbox;

    private String lastSort;

    /**
     * Handles the initial configuration of the scene.
     */
    public void initialize() {
        m1.setOnAction(e -> sortQuestions("Score"));
        m2.setOnAction(e -> sortQuestions("Age"));
        m3.setOnAction(e -> sortQuestions("Relevance"));
        refresh.setOnAction(e -> sortQuestions(lastSort));
    }

    /**
     * Handles clicking the leave button.
     * Closes the window
     */
    public void leave() {
        Stage stage = (Stage) leave.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles clicking the Presenting view button.
     * Moves to presenting view
     */
    public void toPresenting() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentingView.fxml"));
            Parent presentingViewParent;
            presentingViewParent = loader.load();

            PresentingController presentingController = loader.getController();
            presentingController.init();


            Stage stage = (Stage) leave.getScene().getWindow();
            stage.setScene(new Scene(presentingViewParent));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new page");
        }
    }

    /**
     * Displays the question in the requested sorting type.
     * If no type is given, questions will be sorted by score.
     * @param type The requested sorting method.
     */
    public void sortQuestions(String type) {

        if (lastSort == null) {
            lastSort = "Score";
            sortQuestions("Score");
            return;
        }

        lastSort = type;

        Stage stage = (Stage) leave.getScene().getWindow();
        String roomId = stage.getTitle().substring(0,8);

        Boolean includeAnswered = checkbox.isSelected();

        System.out.println(type);
        menuButton.setText("Sort by: " + type);
        List<Question> result = ServerCommunication.sortQuestions(roomId, type, includeAnswered);

        System.out.println(questions);
        System.out.println(result);

        questions.getItems().clear();
        questions.getItems().addAll(result);
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
     * Ban user who is the author of the question.
     */
    public void banUserFromQuestion() {
        try {
            Stage stage = (Stage) leave.getScene().getWindow();
            Question question = (Question) questions.getSelectionModel().getSelectedItem();
            String userId = question.getUserId();
            ServerCommunication.banUser(Long.parseLong(userId));
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
}
