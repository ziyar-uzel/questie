package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "Room")
public class Room {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mod_password")
    private String modPassword;

    @OneToMany(targetEntity = Question.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId", referencedColumnName = "id")
    private List<Question> questions;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lp_id", referencedColumnName = "id")
    @JsonManagedReference
    private LecturePace lecturePace;


    @Column(name = "open")
    @DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm")
    private LocalDateTime openTime;

    @Column(name = "closed")
    private boolean closed;

    public LecturePace getLecturePace() {
        return lecturePace;
    }

    /**
     * Creates a new Lecture room instance.
     * Initializes an empty ArrayList for questions
     */
    public Room() {
        // the room will be open on creation when using the default constructor
        this.openTime = LocalDateTime.now();
        this.id = Room.generateCode(8);
        this.modPassword = Room.generateCode(5);
        this.questions = new ArrayList<>();
        this.lecturePace = new LecturePace();
        this.closed = false;
    }

    /**
     * Creates a new Lecture room instance with an extra boolean parameter as input.
     * @param openTime gives the time the room needs to be opened.
     */
    public Room(LocalDateTime openTime) {
        this.openTime = openTime;
        this.id = Room.generateCode(8);
        this.modPassword = Room.generateCode(5);
        this.questions = new ArrayList<>();
        this.lecturePace = new LecturePace();
        this.closed = false;
    }

    /**
     * Sets the time a room needs to be opened.
     */
    public void setOpen(LocalDateTime openTime) {
        this.openTime = openTime;
    }

    /**
     * Closes the referred room.
     */
    public void setClosingDate() {
        this.openTime = LocalDateTime.now().withYear(4000);
    }

    /**.
     * Generates a random code
     * @param codeLength number of characters
     * @return a generated random code
     */
    private static String generateCode(int codeLength) {
        StringBuilder code = new StringBuilder();
        String randomChars = "abcdefghijklmnopqrstuvwrxyz1234567890";
        Random rand = new Random();

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = rand.nextInt(randomChars.length());
            code.append(randomChars.charAt(randomIndex));
        }

        return code.toString();
    }

    /**.
     * Get method for the id
     * @return the id of the room
     */
    public String getId() {
        return id;
    }


    /**.
     * Get method for the list of questions
     * @return the list of questions of the room
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Get method for the status of the room.
     * @return true if the room is open, false otherwise.
     */
    public LocalDateTime getOpen() {
        return openTime;
    }

    /**
     * Get method for the mod password.
     * @return the modPassword
     */
    public String getModPassword() {
        return modPassword;
    }

    /**
     * Set method for the mod password.
     * @param modPassword the current modPassword
     */
    public void setModPassword(String modPassword) {
        this.modPassword = modPassword;
    }

    /**.
     * Equals method. Compares room ids.
     * @param o The room
     * @return whether a room is equal to another
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Room)) {
            return false;
        }

        Room room = (Room) o;

        return getId().equals(room.getId());

    }

    /**
     * Gets the value of the room's status.
     * @return boolean for room status
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Sets the value of the room's status.
     * @param closed desired status
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

}
