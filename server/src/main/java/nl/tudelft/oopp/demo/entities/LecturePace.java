package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.Objects;

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




@Entity
@Table(name = "lecturePace")
public class LecturePace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "pace_upvote")
    private int paceUpvote;

    @Column(name = "pace_downvote")
    private int paceDownvote;

    @OneToOne(mappedBy = "lecturePace")
    @JsonBackReference
    private Room room;

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ulp_fk", referencedColumnName = "id")
    private List<User> upVotedUsers;

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "dlp_fk", referencedColumnName = "id")
    private List<User> downVotedUsers;

    public List<User> getDownVotedUsers() {
        return downVotedUsers;
    }

    public LecturePace() {
    }


    public Room getRoom() {
        return room;
    }

    /**
     * All arguments constructor.
     *
     * @param id             the id
     * @param paceUpvote     the pace upvote
     * @param paceDownvote   the pace downvote
     * @param room           the room
     * @param upVotedUsers   the upvoted users
     * @param downVotedUsers the downvoted users
     */
    public LecturePace(long id, int paceUpvote, int paceDownvote,
                       Room room, List<User> upVotedUsers, List<User> downVotedUsers) {
        this.id = id;
        this.paceUpvote = paceUpvote;
        this.paceDownvote = paceDownvote;
        this.room = room;
        this.upVotedUsers = upVotedUsers;
        this.downVotedUsers = downVotedUsers;
    }

    public long getId() {
        return id;
    }

    public void setPaceUpvote(int paceUpvote) {
        this.paceUpvote = paceUpvote;
    }

    public void setPaceDownvote(int paceDownvote) {
        this.paceDownvote = paceDownvote;
    }

    public int getPaceUpvote() {
        return paceUpvote;
    }

    public int getPaceDownvote() {
        return paceDownvote;
    }



    public List<User> getUpVotedUsers() {
        return upVotedUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof LecturePace)) {
            return false;
        }

        LecturePace that = (LecturePace) o;
        return getId() == that.getId()
                && getPaceUpvote() == that.getPaceUpvote()
                && getPaceDownvote() == that.getPaceDownvote()
                && getRoom().equals(that.getRoom())
                && Objects.equals(getUpVotedUsers(), that.getUpVotedUsers())
                && Objects.equals(getDownVotedUsers(), that.getDownVotedUsers());
    }

    public void setUpVotedUsers(List<User> upVotedUsers) {
        this.upVotedUsers = upVotedUsers;
    }

    public void setDownVotedUsers(List<User> downVotedUsers) {
        this.downVotedUsers = downVotedUsers;
    }


}
