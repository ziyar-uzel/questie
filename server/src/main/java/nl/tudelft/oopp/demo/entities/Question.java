package nl.tudelft.oopp.demo.entities;

import java.time.Duration;
import java.time.LocalTime;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import jdk.jfr.Timestamp;

@Entity
@Table(name = "Questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "score")
    private int score;

    public Set<User> getUpvotedBy() {
        return this.upvotedBy;
    }

    public void setUpvotedBy(Set<User> upvotedBy) {
        this.upvotedBy = upvotedBy;
    }

    @ManyToMany(mappedBy = "upvotedQuestions")
    private Set<User> upvotedBy = new HashSet<User>();

    @Column(name = "username")
    private String username;

    @Column(name = "userId")
    private long userId;

    @Column(name = "roomId")
    private String roomId;

    @Column(name = "answered")
    private boolean answered;

    @Column(name = "time")
    @Timestamp
    private LocalTime askTime;

    public Question() {

    }

    /**.
     * Creates a new instance of Question
     * @param content the content of the question
     * @param username the owner of the question
     */
    public Question(String content, String username) {
        this.content = content;
        this.username = username;
        this.askTime = LocalTime.now();
        this.answered = false;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String n) {
        username = n;
    }

    /**.
     * Gets the Question id
     * @return question id
     */
    public long getId() {
        return id;
    }


    /**.
     * Gets the content of the question
     * @return question content
     */
    public String getContent() {
        return content;
    }

    /**.
     * Gets the score of the question
     * @return score of question
     */
    public int getScore() {
        return upvotedBy.size();
    }

    /**.
     * Equals method for question class
     * @param o the question
     * @return true if questions are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        Question question = (Question) o;
        return getId() == question.getId() && getScore() == question.getScore()
                && getContent().equals(question.getContent()) && username.equals(question.username);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public LocalTime getAskTime() {
        return askTime;
    }

    public void setAskTime(LocalTime askTime) {
        this.askTime = askTime;
    }

    /**
     * Sorts a list of Questions based on the given sorting method.
     * Sorts by score by default (if no valid method name is provided).
     * @param questionList  the question list
     * @param sortingMethod the sorting method.
     *                      'score' sorts by score, 'age' sorts by age,
     *                      'relevance' sorts by relevance.
     * @return the sorted list
     */
    public static List<Question> sort(List<Question> questionList, String sortingMethod) {

        Comparator<Question> byScore = Comparator.comparing(Question::getScore).reversed();

        Comparator<Question> byAge;
        byAge = Comparator.comparing(
            x -> Duration.between(x.getAskTime(),LocalTime.now()).toSeconds()
        );

        Comparator<Question> byRelevance;
        byRelevance = Comparator.comparing(
            x -> (Duration.between(x.getAskTime(),LocalTime.now()).toSeconds() / 60.0)
                    - (long) x.getScore()
        );

        switch (sortingMethod.toLowerCase()) {
            case "age":
                questionList.sort(byAge);
                break;
            case "score":
                questionList.sort(byScore);
                break;
            case "relevance":
                questionList.sort(byRelevance);
                break;
            default:
                questionList.sort(byScore);
        }

        return questionList;
    }

    /**
     * Filters out the answered questions from an input list of questions.
     * @param questionList the question list to remove questions from.
     * @return the question list without the questions that have been answered.
     */
    public static List<Question> removeAnsweredQuestions(List<Question> questionList) {

        List<Question> result = questionList.stream()
                .filter(x -> !x.isAnswered())
                .collect(Collectors.toList());

        return result;

    }
}
