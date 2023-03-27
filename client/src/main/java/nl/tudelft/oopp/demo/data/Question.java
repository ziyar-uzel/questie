package nl.tudelft.oopp.demo.data;

public class Question {

    private long id;

    private String username;

    private String userId;

    private String content;

    private int score;

    private String roomId;

    private boolean answered;


    /**
     * Creates a new instance of a Question (client side).
     * @param username the username of the current user
     * @param content the content of the question
     */
    public Question(String content, String username, String userId) {
        this.username = username;
        this.content = content;
        this.userId = userId;
        this.score = 0;
        this.answered = false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public String getRoomId() {
        return roomId;
    }

    public long getId() {
        return id;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    @Override
    public String toString() {
        if (!answered) {
            return "(" + score + ") " + username + " : " + content;
        } else {
            return "ANSWERED! (" + score + ") " + username + " : " + content;
        }
    }
}
