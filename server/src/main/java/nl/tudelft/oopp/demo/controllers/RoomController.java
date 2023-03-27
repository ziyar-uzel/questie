package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.api.Api;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;

import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.services.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "api/rooms")
public class RoomController extends Api {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**.
     * Creates a new room and returns this newly created room
     * @return room
     */

    @GetMapping(path = "/create")
    public Room create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return roomService.create(request, response);
    }

    /**.
     * Creates a new room with a scheduled time
     * @param openTime states the time the room has to open
     * @return created room
     */
    @GetMapping(path = "/create/{openTime}")
    public Room createOpenTime(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable String openTime) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm", Locale.ROOT);
        LocalDateTime parsedDateTime = LocalDateTime.parse(openTime, formatter);
        return roomService.createOpenTime(request, response, parsedDateTime);
    }


    /**
     * Finds/Gets the current user and joins them in
     * the requested room.
     * @apiNote /join/{id} eg. /join/xyz
     * @return roomService method
     */
    @GetMapping(path = "/join/{id}")
    public User join(HttpServletResponse response,
                     HttpServletRequest request,
                     @PathVariable String id,
                     @RequestParam(value = "username", required = false) String username,
                     @RequestParam(value = "roomPassword", required = false) String roomPassword
    ) throws Exception {
        return roomService.join(response, request, id, username, roomPassword);
    }

    /**
     * Finds the room with the given id
     * and opens or closes it.
     * /open to open the room,
     * /close to close.
     * @apiNote rooms/status/{id}/{targetStatus} eg. rooms/status/xyz/close
     * @return the current room
     */
    @GetMapping(path = "/status/{id}/{targetStatus}")
    public Room setstate(HttpServletResponse response,
                         HttpServletRequest request,
                         @PathVariable String id,
                         @PathVariable String targetStatus) throws Exception {
        if (targetStatus.equals("open")) {
            System.out.println("opening room");
            return roomService.setStatus(LocalDateTime.now(), response, request, id);
        } else if (targetStatus.equals("close")) {
            System.out.println("closing room");
            LocalDateTime max = LocalDateTime.now().withYear(4000);
            return roomService.setStatus(max, response, request, id);
        }

        throw new Exception("Invalid status; provide target status as open or close");

    }

    @Autowired
    private QuestionRepository questionRepository;

    /**.
     * Takes as an input the room id,
     * and creates and returns a file with all questions belonging to that room.
     * @param roomId the room id of the room that is downloaded
     * @throws IOException Throws io exception is the file cant be found.
     */
    @GetMapping(path = "/download/{roomId}")
    public void download(
            HttpServletRequest request,HttpServletResponse response,
            @PathVariable String roomId) throws IOException {

        roomService.download(request, response, roomId);
    }

    /**.
     * Changes the boolean(Closed) attribute of the room from false to true
     * @path roomId The id of the room that will be closed
     */

    @PutMapping(path = "/close/{roomId}")
    public void deleteRoomById(@PathVariable String roomId) {

        roomService.closeRoom(roomId);

    }
    /**.
     * With the given id, checks whether that room is closed or not
     * @path roomId The id of the room that will be checked
     * @return true if it is closed, and false if it is not
     */

    @GetMapping(path = "/check/closed/{roomId}")
    public boolean checkRoomIsClosed(@PathVariable String roomId) {

        return roomService.getRoomById(roomId).get().isClosed();

    }
}
