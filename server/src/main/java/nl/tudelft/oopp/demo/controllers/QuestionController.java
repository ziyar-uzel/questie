package nl.tudelft.oopp.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.api.Api;
import nl.tudelft.oopp.demo.datatransferobjects.QuestionRequest;
import nl.tudelft.oopp.demo.entities.Question;

import nl.tudelft.oopp.demo.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "api/questions")
public class QuestionController extends Api {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Post {
     *     "question": {
     *         "content": "hello world"
     *     }
     * } to create new question.
     * @param httpRequest - the httpRequest to grab the user creds from
     * @param response - the response to that request to add the cookies
     * @return questionService method
     */
    @PostMapping("/create")
    public Question placeQuestion(HttpServletRequest httpRequest,
                                   HttpServletResponse response,
                                   @RequestBody QuestionRequest request) throws Exception {

        return questionService.placeQuestion(httpRequest, response, request);

    }

    /**.
     * Gives the questions of room with the respective given room id
     * @param roomId The id of the wanted room
     * @return questionService method
     */
    @GetMapping(path = "/view/{roomId}")
    public List<Question> findAllQuestions(@PathVariable String roomId,
                                           HttpServletRequest httpRequest) throws Exception {

        return questionService.findAllQuestions(roomId, httpRequest);
    }

    /**
     * Returns a sorted list of questions of the room with the given room id.
     * @param roomId The id of the room
     * @param type Sorting method to be used.
     * @param includeAnswered Whether answered questions are to be included.
     *                        an optional variable:
     *                        if left blank, answered/marked questions will be added to the list.
     * @return questionList a sorted list of questions
     */
    @GetMapping(path = "sort/{roomId}/{type}/{includeAnswered}")
    public List<Question> sortQuestions(@PathVariable String roomId,
                                        @PathVariable String type,
                                        @PathVariable(required = false) Boolean includeAnswered,
                                        HttpServletRequest httpRequest) throws Exception {

        List<Question> result = questionService.findAllQuestions(roomId, httpRequest);
        Question.sort(result, type);

        if (includeAnswered != null) {

            if (!includeAnswered) {
                result = Question.removeAnsweredQuestions(result);
            }

        }

        return result;
    }


    /**.
     * Deletes the question with the given question id
     * @param questionId The id of the question that will be deleted
     */

    @DeleteMapping(path = "/delete/{questionId}")
    public void deleteQuestionById(@PathVariable long questionId) {
        questionService.deleteQuestion(questionId);
    }

    /**.
     * Authenticates the user and upvotes the question with the id
     * as the current user.
     * @param questionId The id of the question
     * @return questionService method
     */
    @PostMapping(path = "/upvote/{questionId}")
    public Question upvoteQuestion(@PathVariable long questionId,
                                 HttpServletRequest httpRequest,
                                 HttpServletResponse response) throws Exception {

        return questionService.upvote(questionId, httpRequest, response);
    }

    /**.
     * Authenticates the user and removes their upvote from the question with the id.
     * @param questionId The id of the question
     * @return questionService method
     */
    @DeleteMapping(path = "/upvote/{questionId}")
    public Question removeUpvote(@PathVariable long questionId,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse response) throws Exception {

        return questionService.removeUpvote(questionId, httpRequest, response);
    }

    @PostMapping(path = "/mark/{questionId}")
    public Question markQuestionAsAnswered(@PathVariable long questionId,
                                           HttpServletRequest httpRequest,
                                           HttpServletResponse response) throws Exception {
        return questionService.markAsAnswered(questionId, httpRequest, response);
    }

}
