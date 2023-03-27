package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import nl.tudelft.oopp.demo.controllers.LauncherController;
import nl.tudelft.oopp.demo.data.LecturePace;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.User;

public class ServerCommunication {

    private static final HttpClient client = HttpClient.newBuilder().build();

    private static Gson gson =  new Gson();

    /**.
     * Creates a new room
     * @return room id
     */
    public static String getRoom() {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/rooms/create")).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        Optional<String> hedUserId = response.headers().firstValue("userId");

        System.out.println("[#] Server thinks this is user is id: " + hedUserId);

        /*
         get the user cookies from the server response, and store them for
         future requests
        */
        UserHandler.setCurrentUserFromResponse(response);
        return response.body();
    }

    /**.
     * Creates a new room
     * @return room id
     */
    public static String getRoomOpenTime(LocalDateTime openTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm", Locale.ROOT);
        String formattedDateTime = openTime.format(formatter);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/rooms/create/" + formattedDateTime)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        Optional<String> hedUserId = response.headers().firstValue("userId");

        System.out.println("[#] Server thinks this is user is id: " + hedUserId);

        /*
         get the user cookies from the server response, and store them for
         future requests
        */
        UserHandler.setCurrentUserFromResponse(response);
        return response.body();
    }
    /**
     * Retrieves the room info + authorization.
     * @param code the room code entered in the launcher text field
     * @param username the username entered in the launcher text field
     * @return the response body as a string
     */

    public static String joinRoom(String code, String username) {

        URI url = URI.create("http://localhost:8080/api/rooms/join/" + code);

        // get the current user (the one who makes this particular request)
        User currentUser = UserHandler.getCurrentUser();

        if (currentUser.exists()) {
            currentUser.setUsername(username);
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            url = URI.create(url.toString() + "?username=" + username);
        }

        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = null;

        try {
            System.out.println("[*] GET to url: " + url);
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }

        System.out.println(response.body());

        if ((response.body().contains(
                "Room is currently closed, joining is not allowed."))) {
            System.out.println("Status: " + response.statusCode());
            return "The room is not open yet!";
        }
        if ((response.statusCode() != 200) || (response.body().equals(""))) {
            System.out.println("Status: " + response.statusCode());
            return "Invalid room code!";
        }


        /*
         get the user cookies from the server response, and store them for
         future requests
        */
        UserHandler.setCurrentUserFromResponse(response);

        return response.body();
    }

    /**
     * Retrieves the list of questions from the current room.
     * @param roomId the room ID
     * @return the list of questions
     */
    public static List<Question> findQuestions(String roomId) {

        User currentUser = UserHandler.getCurrentUser();
        String username = currentUser.getUsername();

        URI url = URI.create("http://localhost:8080/api/questions/view/" + roomId);

        if (currentUser.exists()) {
            currentUser.setUsername(username);
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            url = URI.create(url.toString() + "?username=" + username);
        }

        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // empty list instead of error message
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Room closed");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("Error! This room is closed right now");
            alertInfo.showAndWait();
            return List.of();
        }

        /*
         get the user cookies from the server response, and store them for
         future requests
        */
        UserHandler.setCurrentUserFromResponse(response);

        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**
     * Retrieves the list of questions from the current room.
     * @param roomId the room ID
     * @return the list of questions
     */
    public static List<Question> sortQuestions(String roomId,
                                               String type,
                                               Boolean includeAnswered) {
        User currentUser = UserHandler.getCurrentUser();
        String username = currentUser.getUsername();

        URI url = URI.create("http://localhost:8080/api/questions/sort/" + roomId + "/" + type + "/" + includeAnswered);

        if (currentUser.exists()) {
            currentUser.setUsername(username);
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            url = URI.create(url.toString() + "?username=" + username);
        }
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // empty list instead of error message
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        System.out.println("last checks");
        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**
     * Posts question from client side.
     * @param content content of question
     * @param username username
     */
    public static void postQuestion(String content, String username) {

        URI url = URI.create("http://localhost:8080/api/questions/create");

        // get the current user (the one who makes this particular request)
        User currentUser = UserHandler.getCurrentUser();

        if (currentUser.exists()) {
            //            currentUser.setUsername(username);
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        }



        String body = "{\r\n\t\"question\": {\r\n\t\t\"content\": \"" + content + "\"\r\n\t}\r\n}";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type","application/json; utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(body)).uri(url).build();
        HttpResponse<String> response = null;

        try {
            System.out.println("[*] POST to url: " + url);
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if ((response.statusCode() != 200)) {
            System.out.println("Status: " + response.statusCode());
            return;
        }

        /*
         get the user cookies from the server response, and store them for
         future requests
        */
        UserHandler.setCurrentUserFromResponse(response);

    }

    /**
     * Deletes a selected question based on ID.
     * @param questionID the ID of the question to be deleted
     */
    public static void deleteOwnQuestion(long questionID) {

        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/api/questions/delete/" + questionID)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to delete question!");
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }

    /**
     * Upvotes a selected question.
     * @param questionID the ID of the question to be upvoted
     */
    public static void voteQuestion(long questionID) throws Exception {

        URI url = URI.create("http://localhost:8080/api/questions/upvote/" + questionID);

        // get the current user (the one who makes this particular request)
        User currentUser = UserHandler.getCurrentUser();

        if (currentUser.exists()) {
            //            currentUser.setUsername(username);
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            throw new Exception("user doesn't exist!");
        }

        String body = "";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type","application/json; utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(body)).uri(url).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to upvote question!");
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }

    /**
     * Downloads all questions in a room.
     */
    public static void downLoadRoom(String roomID) {
        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            url = new URL("http://localhost:8080/api/rooms/download/" + roomID); //File Location goes here
            con = url.openConnection(); // open the url connection.
            dis = new DataInputStream(con.getInputStream());
            fileData = new byte[con.getContentLength()];
            for (int q = 0; q < fileData.length; q++) {
                fileData[q] = dis.readByte();
            }
            dis.close(); // close the data input stream
            String localDir = System.getProperty("user.dir");
            System.out.println("The file is saved in: " + localDir);
            Alert selectAlert = new Alert(Alert.AlertType.INFORMATION);
            selectAlert.setTitle("Save");
            selectAlert.setHeaderText(null);
            selectAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            selectAlert.setContentText(
                    "The file is saved in: " + localDir + "\n And is called: " + roomID + ".txt");
            selectAlert.show();
            fos = new FileOutputStream(new File(
                    localDir + "\\" + roomID + ".txt")); //FILE Save Location goes here
            fos.write(fileData);  // write out the file we want to save.
            fos.close(); // close the output stream writer
        } catch (Exception e) {
            System.out.println("something went wrong");
        }
    }
    /** Increments the number in the too fast column of the lecture pace.
    * If it is called again it will decrement the number.
    * Hence in every odd calls it will increment and in every even calls it will decrement.
    */

    public static void upvoteLecturePace()  {

        try {


            User currentUser = UserHandler.getCurrentUser();
            String userId = currentUser.getId();

            URI url = URI.create("http://localhost:8080/api/feedback/upvote/" + LauncherController.roomCode + "/" + userId);

            // get the current user (the one who makes this particular request)

            httpRequestSenderForLpUpvote(currentUser, url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * Increments the number in the too slow column of the lecture pace.
     * If it is called again it will decrement the number.
     * Hence in every odd calls it will increment and in every even calls it will decrement.
     */
    public static void downVoteLecturePace() {

        try {

            User currentUser = UserHandler.getCurrentUser();
            String userId = currentUser.getId();

            URI url = URI.create("http://localhost:8080/api/feedback/downvote/" + LauncherController.roomCode + "/" + userId);

            // get the current user (the one who makes this particular request)

            httpRequestSenderForLpUpvote(currentUser, url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void httpRequestSenderForLpUpvote(User currentUser, URI url) throws Exception {
        if (currentUser.exists()) {
            url = currentUser.sign(url);
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            throw new Exception("user doesn't exist!");
        }
        String body = "";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type","application/json; utf-8")
                .PUT(HttpRequest.BodyPublishers.ofString(body)).uri(url).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to vote for lecture pace feedback!");
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * gives the numbers of upvotes(too fast) and downvotes(too slow) within lecture pace entity.
     * @param roomId the id of the room that lecture pace entity is in.
     * @return array of the number of upvotes and downvotes
     */
    public static int[] getLecturePaceFeedback(String roomId) {


        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/feedback/receive/" + roomId)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return new int[0]; // empty list instead of error message
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        LecturePace lecturePace = gson
                .fromJson(response.body(), new TypeToken<LecturePace>(){}.getType());

        int[] lecturePaceFeedBack = new int[2];
        lecturePaceFeedBack[0] = lecturePace.getPaceUpvote();
        lecturePaceFeedBack[1] = lecturePace.getPaceDownvote();
        return lecturePaceFeedBack;
    }

    /**
     * Bans a user.
     *
     * @param userId the user id
     * @throws Exception the exception
     */
    public static void banUser(long userId) throws Exception {

        URI url = URI.create("http://localhost:8080/api/users/ban/" + userId);
        String body = "";
        User currentUser = UserHandler.getCurrentUser();

        if (currentUser.exists()) {
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            throw new Exception("user doesn't exist!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type","application/json; utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(body)).uri(url).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to ban user!");
        }

        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            System.out.println("User has not been banned!");
        } else {
            System.out.println("User with ID" + userId + "is banned!");
        }

    }

    /**
     * Helper method for calculating percentages.
     */
    public String calculatePercentage(int number, int total) {
        int result = number / total * (100);
        return result + "%";
    }
        
    /**
     * Marks a question as answered.
     * @param questionID the ID of the question to be marked
     * @throws Exception if user does not exist
     */
    public static void markQuestion(long questionID) throws Exception {

        URI url = URI.create("http://localhost:8080/api/questions/mark/" + questionID);
        String body = "";
        User currentUser = UserHandler.getCurrentUser();

        if (currentUser.exists()) {
            url = currentUser.sign(url); // `sign` the url with user credentials & username
            System.out.printf("[*] current user: [%s] %s%n",
                    currentUser.getId(),
                    currentUser.getUsername());
        } else {
            throw new Exception("user doesn't exist!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type","application/json; utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(body)).uri(url).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to mark question!");
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }
    /**
     * closes the room.
     * @param roomId the ID of the room to be closed
     */

    public static void closeRoomById(String roomId) {

        String body = "";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type","application/json; utf-8")
                .PUT(HttpRequest.BodyPublishers.ofString(body)).uri(URI.create("http://localhost:8080/api/rooms/close/" + roomId)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to close room!");
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }

    /**
     * check whether the room is closed or not.
     * @param roomId the ID of the room to be checked
     * @return true if the room is closed or false if it is not closed
     */
    public static boolean checkRoomClosed(String roomId) {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/rooms/check/closed/" + roomId)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<Boolean>(){}.getType());
    }

    /**
     * Check if a user is banned.
     *
     * @return whether the user is banned.
     */
    public static boolean checkUserBanned() {

        User currentUser = UserHandler.getCurrentUser();
        URI signedUri = currentUser.sign(URI.create("http://localhost:8080/api/users/amIBanned"));
        HttpRequest request = HttpRequest.newBuilder().GET().uri(signedUri).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return gson.fromJson(response.body(), new TypeToken<Boolean>(){}.getType());
    }







    /**
     * Sets a room open time to the current time.
     * @param roomId id of the room that needs to be opened
     */
    public static void enableJoin(String roomId) {
        URI url = URI.create("http://localhost:8080/api/rooms/status/" + roomId + "/open");

        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = null;

        try {
            System.out.println("[*] GET to url: " + url);
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Communication with server failed");
        }
    }

    /**
     * Sets a room open time to the max time.
     * @param roomId id of the room that needs to be closed
     */
    public static void disableJoin(String roomId) {
        URI url = URI.create("http://localhost:8080/api/rooms/status/" + roomId + "/close");

        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = null;

        try {
            System.out.println("[*] GET to url: " + url);
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Communication with server failed");
        }
    }

}

