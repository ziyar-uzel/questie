package nl.tudelft.oopp.demo.data;

import java.util.List;
import java.util.Objects;

public class LecturePace {


    private long id;


    private int paceUpvote;


    private int paceDownvote;


    private Room room;

    private List<User> upVotedUsers;


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


}
