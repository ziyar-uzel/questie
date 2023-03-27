package nl.tudelft.oopp.demo.services;

import java.util.Optional;
import nl.tudelft.oopp.demo.entities.LecturePace;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.LecturePaceRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class LecturePaceService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LecturePaceRepository lecturePaceRepository;

    @Autowired
    private UserRepository userRepository;


    /**.
     * if the user never upvoted before, this method increment the upvote
     * attribute of lecture pace entity within the specified room, if user
     * upvoted before the upvote will be decremented by one meaning that it is resetted.
     * @param roomId The id of the room that lecture pace entity in
     * @param userId of the user that votes
     * @return List of questions of the input room
     */
    public LecturePace upvotePace(String roomId,
                                    long userId) {

        Room currentRoom = roomRepository.findById(roomId).get();
        LecturePace currentLecturePace = lecturePaceRepository.findByRoom(currentRoom).get();
        User currentUser = userRepository.getOneByIdString(Long.toString(userId));

        if (currentLecturePace.getUpVotedUsers().contains(currentUser)) {
            removeFromUpVote(currentLecturePace, currentUser);
            lecturePaceRepository.save(currentLecturePace);
            return currentLecturePace;
        } else if (currentLecturePace.getDownVotedUsers().contains(currentUser)) {
            removeFromDownVote(currentLecturePace, currentUser);

            addToUpvote(currentLecturePace, currentUser);
            lecturePaceRepository.save(currentLecturePace);
            return currentLecturePace;
        } else {
            addToUpvote(currentLecturePace, currentUser);
            lecturePaceRepository.save(currentLecturePace);
            return currentLecturePace;
        }

    }

    /**.
     * if the user never downvoted before, this method increment the downvote
     * attribute of lecture pace entity within the specified room, if user
     * downvoted before the downvote will be decremented by one meaning that it is resetted.
     * @param roomId The id of the room that lecture pace entity in
     * @param userId of the user that votes
     * @return updated version of Lecture pace instance
     */
    public LecturePace downVotePace(String roomId,
                                  long userId) {

        Room currentRoom = roomRepository.findById(roomId).get();
        LecturePace currentLecturePace = lecturePaceRepository.findByRoom(currentRoom).get();
        User currentUser = userRepository.getOneByIdString(Long.toString(userId));

        if (currentLecturePace.getDownVotedUsers().contains(currentUser)) {
            removeFromDownVote(currentLecturePace, currentUser);
            lecturePaceRepository.save(currentLecturePace);
            return currentLecturePace;
        } else if (currentLecturePace.getUpVotedUsers().contains(currentUser)) {
            removeFromUpVote(currentLecturePace, currentUser);

            addToDownvote(currentLecturePace, currentUser);
            lecturePaceRepository.save(currentLecturePace);
            return currentLecturePace;
        } else {
            addToDownvote(currentLecturePace, currentUser);
            lecturePaceRepository.save(currentLecturePace);
            return currentLecturePace;
        }

    }

    /**.
     * gives the desired LecturePace feedback
     * @param roomId The id of the room that lecture pace entity in
     * @return LecturePace instance
     */

    public LecturePace findFeedback(String roomId) {

        Room currentRoom = roomRepository.findById(roomId).get();
        return lecturePaceRepository.findByRoom(currentRoom).get();
    }



    private void removeFromDownVote(LecturePace currentLecturePace, User currentUser) {
        int a = currentLecturePace.getPaceDownvote() - 1;
        currentLecturePace.setPaceDownvote(a);
        currentLecturePace.getDownVotedUsers().remove(currentUser);
    }

    private void addToUpvote(LecturePace currentLecturePace, User currentUser) {
        int a = currentLecturePace.getPaceUpvote() + 1;
        currentLecturePace.setPaceUpvote(a);
        currentLecturePace.getUpVotedUsers().add(currentUser);
    }

    private void addToDownvote(LecturePace currentLecturePace, User currentUser) {
        int a = currentLecturePace.getPaceDownvote() + 1;
        currentLecturePace.setPaceDownvote(a);
        currentLecturePace.getDownVotedUsers().add(currentUser);
    }

    private void removeFromUpVote(LecturePace currentLecturePace, User currentUser) {
        int a = currentLecturePace.getPaceUpvote() - 1;
        currentLecturePace.setPaceUpvote(a);
        currentLecturePace.getUpVotedUsers().remove(currentUser);
    }




}
