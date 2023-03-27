package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The type User.
 */
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "authToken")

    private String authToken;

    @Column(name = "username")

    private String username;

    @Column(name = "roomId")
    private String roomId;

    @Column(name = "moderator")
    private boolean moderator;

    @Column(name = "banned")
    private boolean banned;

    /**
     * Instantiates a new User.
     */

    @ManyToMany
    @JoinTable(
            name = "upvote",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    protected Set<Question> upvotedQuestions;

    /**
     * Instantiates a new User.
     */
    public User() {
        upvotedQuestions = new HashSet<Question>();
    }

    /**
     * Instantiates a new User with extra params.
     * @param username the username
     */
    public User(String username) {
        this.username = username;
        upvotedQuestions = new HashSet<Question>();
    }

    /**
     * Instantiates a new User with extra params.
     * @param username the user's username
     * @param room     is the room the user wants to join
     */
    public User(String username, String room) {
        this.username = username;
        this.joinRoom(room);
        upvotedQuestions = new HashSet<Question>();
    }

    /**
     * Instantiates a new User with username, roomCode and moderator.
     * @param moderator - bool true/false if true the User will be a moderator
     */
    public User(String username, String room, boolean moderator) {
        this.username = username;
        this.joinRoom(room);
        upvotedQuestions = new HashSet<Question>();

        if (moderator) {
            this.setModerator(true);
        }
    }

    /**
     * Gets username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's auth token.
     * @return user's current auth token
     */
    @JsonIgnore
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Sets auth token.
     * @param token the token
     */
    public void setAuthToken(String token) {
        authToken = token;
    }

    /**
     * Gets the room id.
     * @return the room id
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * Sets the room id.
     * @param roomId the room id
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * Joins the user to a room.
     * @param id the id of the room
     */
    public void joinRoom(String id) {
        this.setRoomId(id);
    }

    /**
     * Generates, and sets a new auth token to the user
     * Note: you must call `userRepository.saveAndFlush(user)`
     * in order to commit the newly generated auth token to the
     * database
     *
     * @return the unique auth token generated
     */
    public String generateNewAuthToken() {
        String auth = UUID.randomUUID().toString();
        setAuthToken(auth);
        return auth;
    }

    /**.
     * Equals method for User class
     * @param o the user class
     * @return true if users are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(getAuthToken(),
                user.getAuthToken()) && Objects.equals(getUsername(),
                user.getUsername()) && Objects.equals(getRoomId(),
                user.getRoomId());
    }


    /**
     * Checks whether the user has upvoted a specific question.
     *
     * @param q the question that the user has or has not upvoted.
     */
    public boolean hasUpvotedQuestion(Question q) {
        return upvotedQuestions.contains(q);
    }

    /**
     * Upvotes the given question.
     *
     * @param q the question to be upvoted.
     */
    public void upvoteQuestion(Question q) {
        upvotedQuestions.add(q);
    }

    /**
     * Removes the user's upvote from question.
     *
     * @param q the question the upvote is to be removed from.
     */
    public void removeUpvoteFromQuestion(Question q) {
        upvotedQuestions.remove(q);
    }

    /**
     * Getter for the moderator boolean.
     *
     * @return the moderator boolean.
     */
    public boolean isModerator() {
        return moderator;
    }

    /**
     * Promotes the user to moderator.
     */
    public void promoteToModerator() {
        setModerator(true);
    }

    /**
     * Setter for the moderator boolean.
     *
     * @param moderator the desired state of the field.
     */
    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    public void ban() {
        setBanned(true);
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
