package nl.tudelft.oopp.demo.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.api.Api;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.LecturePaceRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@Service
public class RoomService extends Api {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LecturePaceRepository lecturePaceRepository;



    /**.
     * Creates a new room and returns this newly created room
     * @return room
     */
    public Room create(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Room room = new Room();
        roomRepository.save(room);
        lecturePaceRepository.save(room.getLecturePace());

        User currentUser = getOrCreateUserFromRequest(request);

        currentUser.setModerator(true);
        currentUser.setRoomId(room.getId());
        currentUser.setUsername("default");
        updateUserAuthAndSave(currentUser, response);
        userRepository.save(currentUser);
        System.out.println("[#] User +" + currentUser.id + " created a new room!");
        return room;
    }

    /**.
     * Creates a room with an opening time
     * @param openTime the time the room needs to open
     * @return created room
     */
    public Room createOpenTime(HttpServletRequest request, HttpServletResponse response,
                               LocalDateTime openTime) throws Exception {



        Room room = new Room(openTime);
        roomRepository.save(room);
        lecturePaceRepository.save(room.getLecturePace());

        User currentUser = getOrCreateUserFromRequest(request);

        currentUser.setModerator(true);
        currentUser.setRoomId(room.getId());
        currentUser.setUsername("default");
        updateUserAuthAndSave(currentUser, response);
        userRepository.save(currentUser);
        System.out.println("[#] User +" + currentUser.id + " created a new room!");

        return room;
    }


    /**
     * Finds/Gets the current user and joins them in
     * the requested room. If a password is provided, it checks if the password
     * matches the one stored in the database, and promotes the user to a moderator
     * if the auth passes.
     * @apiNote /join/{id} eg. /join/xyz, /join/xyz@pwd
     * @return the current user
     */
    public User join(HttpServletResponse response,
                     HttpServletRequest request,
                     @PathVariable String id,
                     @RequestParam(value = "username", required = false) String username,
                     @RequestParam(value = "password", required = false) String roomPassword
    ) throws Exception {

        if (id.contains("@")) {
            System.out.println("ID has mod-password embedded!");
            String[] split = id.split("@");
            id = split[0]; // the room code is the first half
            roomPassword = split[1]; // room password is the other half
        }

        boolean roomExists = roomRepository.existsById(id);
        Room room;

        if (!roomExists) {
            throw new Exception("Room does not exist.");
        }

        room = roomRepository.findById(id).get();

        boolean authModerator = false; // by default this is false
        if (roomPassword != null) {
            authModerator = roomPassword.equals(room.getModPassword());
            if (!authModerator) {
                throw new Exception("Invalid room password");
            }
        }

        if (!authModerator && room.getOpen().isAfter(LocalDateTime.now())) {
            System.out.println("[!] Can't join room; room is currently not open");
            throw new Exception("Room is currently closed, joining is not allowed.");
        }

        User currentUser = getOrCreateUserFromRequest(request);
        System.out.println("[#] current user is: " + currentUser.id);

        currentUser.setModerator(authModerator);
        currentUser.setUsername(username);
        currentUser.joinRoom(id);
        userRepository.save(currentUser);

        updateUserAuthAndSave(currentUser, response);
        return currentUser;
    }

    /**
     * Sets the status of the room with the requested id.
     * @param open sets room opening time to given time
     * @apiNote rooms/status/{id}/{targetStatus} eg. rooms/status/xyz/close
     * @return the current room
     */
    public Room setStatus(LocalDateTime open, HttpServletResponse response,
                          HttpServletRequest request, String id
    ) throws Exception {

        // TODO Authenticate the user sending the close request so that
        //  only admins have permission to close the room.

        boolean roomExists = roomRepository.existsById(id);
        Room room;

        if (!roomExists) {
            System.out.println("[!] room code [" + id + "]  is invalid");
            throw new Exception("Room does not exist.");
        }

        room = roomRepository.findById(id).get();
        System.out.println("[#] room [" + room + "]  exists");

        //    if (open == room.getOpen()) {
        //        String message = open ? "Can't open room; room is already open"
        //                : "Can't close room; room is already closed";
        //        throw new Exception(message);
        //    } else if (open) {
        //        room.setOpen();
        //    } else {
        //        room.setClosed();
        //    }

        room.setOpen(open);

        roomRepository.saveAndFlush(room);

        return room;
    }


    @Autowired
    private ServletContext servletContext;
    /**.
     * Takes as an input the room id,
     * and creates and returns a file with all questions belonging to that room.
     * @param roomId the room id of the room that is downloaded
     * @throws IOException Throws io exception is the file cant be found.
     */

    public void download(
            HttpServletRequest request, HttpServletResponse response,
            @PathVariable String roomId) throws IOException {

        System.out.println("downloading...");
        List<Question> list = questionRepository.findAll();

        StringBuilder toWrite = new StringBuilder();
        for (Question question: list) {
            if (question.getRoomId().equals(roomId)) {
                toWrite.append(question.getScore()).append(" ");
                toWrite.append(question.getAskTime().toString());
                toWrite.append(" ");
                toWrite.append(question.getUsername());
                toWrite.append("\n").append(question.getContent()).append("\n\n");
            }
        }
        if (toWrite.toString().equals("")) {
            System.out.println("This room does not exist");
            return;
        }

        String fileToWrite = roomId + ".txt";
        File file = new File("server/DownloadedFiles/" + fileToWrite);
        try {
            Writer writer = new BufferedWriter(new java.io.FileWriter(file));
            writer.write(toWrite.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(2);
        }

        System.out.println("fileName: " + fileToWrite);

        //source of code: https://o7planning.org/11765/spring-boot-file-download#a16291459
        // Content-Type
        // application/pdf
        response.setContentType("application/octet-stream");

        // Content-Disposition
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=" + file.getName());

        // Content-Length
        response.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();
    }

    /**
     * Checks if a given room is open.
     * @param id of room
     * @return boolean
     * @throws Exception if room doesnt exist
     */
    public boolean isOpen(@PathVariable String id) throws Exception {
        boolean roomExists = roomRepository.existsById(id);
        Room room;

        if (!roomExists) {
            System.out.println("[!] room code [" + id + "]  is invalid");
            throw new Exception("Room does not exist.");
        }

        room = roomRepository.findById(id).get();
        return !LocalDateTime.now().isBefore(room.getOpen());
    }

    /**.
     * Changes the boolean(Closed) attribute of the room from false to true
     * @param id The id of the room that will be closed
     * @return boolean
     */

    public boolean closeRoom(String id) {


        if (roomRepository.findById(id) != null) {
            Optional<Room> currentRoom = roomRepository.findById(id);
            Room room = currentRoom.get();
            room.setClosed(true);
            roomRepository.save(room);
            return true;
        }
        return false;

    }

    /**.
     * @param id The id of the room that will be returned
     * @return Optional room
     */

    public Optional<Room> getRoomById(String id) {

        return roomRepository.findById(id);

    }
}
