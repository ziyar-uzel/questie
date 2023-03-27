package nl.tudelft.oopp.demo.services;

import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.api.Api;
import nl.tudelft.oopp.demo.datatransferobjects.QuestionRequest;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;



@Service
public class QuestionService extends Api {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Post {
     *     "question": {
     *         "content": "hello world"
     *     }
     * } to create new question.
     * @param httpRequest - the httpRequest to grab the user creds from
     * @param response - the response to that request to add the cookies
     * @return
     */
    public Question placeQuestion(HttpServletRequest httpRequest,
                                  HttpServletResponse response,
                                  @RequestBody QuestionRequest request) throws Exception {

        User currentUser = getUserFromRequest(httpRequest, response);

        if (currentUser == null) {
            System.out.println("[!] Can't authenticate user.");
            return null;
        }

        /*
           A question can technically be posted to a closed room.
           This is an additional check to ensure the room is open before allowing question posting.
        */

        String roomId = currentUser.getRoomId();

        boolean roomExists = roomRepository.existsById(roomId);
        Room room;

        if (!roomExists) {
            System.out.println("[!] room code [" + roomId + "]  is invalid");
            throw new Exception("Room does not exist.");
        }

        // fetches the Room object referred to by the id.
        // this action is required to check whether the room is open or not.
        room = roomRepository.getOne(roomId);

        if (room.getOpen().isAfter(LocalDateTime.now())) {
            System.out.println("[!] Can't post question; room is currently not open");
            return null;
        }

        Question sample = request.getQuestion();
        sample.setRoomId(currentUser.getRoomId());
        sample.setUsername(currentUser.getUsername());
        sample.setUserId(currentUser.getId());
        sample.setAskTime(LocalTime.now());
        System.out.println("question place " + sample.getRoomId());
        return questionRepository.saveAndFlush(sample);
    }

    /**.
     * Gives the questions of room with the respective given room id
     * @param roomId The id of the wanted room
     * @return List of questions of the input room
     */
    public List<Question> findAllQuestions(@PathVariable String roomId,
                                           HttpServletRequest httpRequest) throws Exception {
        // authenticate and set currentUser or throw exception
        setCurrentUserOrThrow(httpRequest);
        boolean isMod = currentUser.isModerator();

        List<Question> list = questionRepository.findAll();
        List<Question> result = new ArrayList<Question>();
        boolean roomExists = roomRepository.existsById(roomId);
        Room room = roomRepository.getOne(roomId);

        if (!roomExists) {
            System.out.println("[!] room code [" + roomId + "]  is invalid");
            throw new Exception("Room does not exist.");
        }
        if (room.getOpen().isAfter(LocalDateTime.now()) && !isMod) {
            throw new Exception("Room is closed");
        }
        for (Question question: list) {
            if (question.getRoomId().equals(roomId)) {
                result.add(question);
            }
        }

        return result;
    }
    /**.
     * Deletes the question with the given question id from the question repository
     * @param id The id of the question that will be deleted
     */

    public boolean deleteQuestion(long id) {

        if (questionRepository.existsById(id)) {
            Question q = questionRepository.findById(id).get();
            for (User user : q.getUpvotedBy()) {
                user.removeUpvoteFromQuestion(q);
            }
            questionRepository.delete(q);
            return true;
        }
        return false;

    }

    /**
     * Upvotes the question with the id after authenticating the user from the
     * request.
     * @param httpRequest - the httpRequest to grab the user creds from
     * @param response - the response to that request to add the cookies
     * @return
     */
    public Question upvote(long id, HttpServletRequest httpRequest,
                                  HttpServletResponse response) throws Exception {

        // authenticate and set currentUser or throw exception
        setCurrentUserOrThrow(httpRequest);

        Question question = questionRepository.findById(id).get();
        currentUser.upvoteQuestion(question);
        userRepository.saveAndFlush(currentUser);
        return questionRepository.saveAndFlush(question);
    }

    /**
     * Removes and upvote from a question after authenticating the user from
     * the requst.
     * @param httpRequest - the httpRequest to grab the user creds from
     * @param response - the response to that request to add the cookies
     * @return the question with the id
     */
    public Question removeUpvote(long id, HttpServletRequest httpRequest,
                           HttpServletResponse response) throws Exception {

        // authenticate and set currentUser or throw exception
        setCurrentUserOrThrow(httpRequest);

        Question question = questionRepository.getOne(id);
        currentUser.removeUpvoteFromQuestion(question);
        userRepository.saveAndFlush(currentUser);
        return questionRepository.saveAndFlush(question);
    }

    /**
     * Marks the question with the given id as answered.
     * @param id - the question's Id
     * @param httpRequest - the httpRequest to grab the user creds from
     */
    public Question markAsAnswered(long id, HttpServletRequest httpRequest,
                                   HttpServletResponse response) throws Exception {
        if (questionRepository.findById(id).isPresent()) {
            // authenticate and set currentUser or throw exception
            User user = setCurrentUserOrThrow(httpRequest);

            /* TODO Add admin privilege check
            if (!user.isModerator()){
                throw new Exception("Only admins can mark questions.");
            }

             */

            Question question = questionRepository.findById(id).get();
            question.setAnswered(!question.isAnswered());
            return questionRepository.saveAndFlush(question);
        }
        throw new Exception("Question with the requested ID does not exist");


    }
}
