package nl.tudelft.oopp.demo;

import java.time.LocalDateTime;
import java.util.List;
import nl.tudelft.oopp.demo.entities.LecturePace;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.LecturePaceRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LecturePaceTest {
    @Autowired
    private LecturePaceRepository lecturePaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void emptyConstructorTest() {
        LecturePace test = new LecturePace();
        Assertions.assertNotNull(test);
    }

    @Test
    public void fullConstructorTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertNotNull(test);
        Assertions.assertEquals(test.getId(), 1);
        Assertions.assertEquals(test.getPaceUpvote(), 3);
        Assertions.assertEquals(test.getPaceDownvote(), 5);
        Assertions.assertEquals(test.getRoom(), room);
        Assertions.assertEquals(test.getUpVotedUsers(), userList1);
        Assertions.assertEquals(test.getDownVotedUsers(), userList2);
    }

    @Test
    public void getIdTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test.getId(), 1);
    }

    @Test
    public void getSetPaceUpvoteTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test.getPaceUpvote(), 3);

        test.setPaceUpvote(21);

        Assertions.assertEquals(test.getPaceUpvote(), 21);
    }

    @Test
    public void getSetPaceDownvoteTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test.getPaceDownvote(), 5);

        test.setPaceDownvote(23);

        Assertions.assertEquals(test.getPaceDownvote(), 23);
    }

    @Test
    public void getRoomTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test.getRoom(), room);
    }

    @Test
    public void getUpVotedUsersTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test.getUpVotedUsers(), userList1);
    }

    @Test
    public void getDownVotedUsersTest() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test.getDownVotedUsers(), userList2);
    }

    @Test
    public void testEqualsSelf() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test, test);
    }

    @Test
    public void testNotEqualsOtherObject() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertNotEquals(test, 5);
    }

    @Test
    public void testEqualsIdenticalLecturePace() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);
        LecturePace test2 = new LecturePace(1, 3, 5, room, userList1, userList2);

        Assertions.assertEquals(test, test2);
    }

    @Test
    public void testNotEqualsDifferentLecturePace() {
        Room room = new Room(LocalDateTime.now());
        String roomId = room.getId();

        Room otherRoom = new Room(LocalDateTime.now());

        User user1 = new User("john", roomId, false);
        List<User> userList1 = List.of(user1);

        User user2 = new User("max", roomId, false);
        List<User> userList2 = List.of(user2);

        LecturePace test = new LecturePace(1, 3, 5, room, userList1, userList2);

        LecturePace test2 = new LecturePace(2, 3, 5, room, userList1, userList2);
        Assertions.assertNotEquals(test, test2);

        LecturePace test3 = new LecturePace(1, 21, 5, room, userList1, userList2);
        Assertions.assertNotEquals(test, test3);

        LecturePace test4 = new LecturePace(1, 3, 27, otherRoom, userList1, userList2);
        Assertions.assertNotEquals(test, test4);

        LecturePace test5 = new LecturePace(1, 3, 5, room, userList2, userList2);
        Assertions.assertNotEquals(test, test5);

        LecturePace test6 = new LecturePace(1, 3, 5, room, userList1, userList1);
        Assertions.assertNotEquals(test, test6);
    }
}
