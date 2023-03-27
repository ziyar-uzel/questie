package nl.tudelft.oopp.demo;

import java.time.LocalDateTime;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;

import nl.tudelft.oopp.demo.repositories.RoomRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RoomTest {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void constructorTest() {
        Room room = new Room();
        Assertions.assertNotNull(room);
    }

    @Test
    public void modifiedConstructorTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        Room room = new Room(currentTime);
        Assertions.assertNotNull(room);
    }

    @Test
    public void getIdTest() {
        Room room = new Room();
        Assertions.assertNotNull(room.getId());
    }

    @Test
    public void getIdLengthTest() {
        Room room = new Room();
        Assertions.assertEquals(room.getId().length(),8);
    }

    @Test
    public void uniqueIdTest() {
        Room room = new Room();
        Room room2 = new Room();
        Assertions.assertNotEquals(room.getId(), room2.getId());
    }

    @Test
    public void getLecturePaceTest() {
        Room room = new Room();
        Assertions.assertNotNull(room.getLecturePace());
    }

    @Test
    public void setandGetOpenTest() {
        Room room = new Room();
        LocalDateTime currentTime = LocalDateTime.now();
        room.setOpen(currentTime);
        Assertions.assertEquals(room.getOpen(),currentTime);
    }


    @Test
    public void setClosedDateTest() {
        Room room = new Room();
        room.setClosingDate();
        Assertions.assertEquals(room.getOpen().getYear(), 4000);
    }

    @Test
    public void isClosedTest() {
        Room room = new Room();
        Assertions.assertNotNull(room.isClosed());
    }

    @Test
    public void setClosedTest() {
        Room room = new Room();
        room.setClosed(true);
        Assertions.assertTrue(room.isClosed());
    }


    @Test
    public void getModPasswordTest() {
        Room room = new Room();
        Assertions.assertNotNull(room.getModPassword());
    }

    @Test
    public void getModPasswordLengthTest() {
        Room room = new Room();
        Assertions.assertEquals(room.getModPassword().length(), 5);
    }

    @Test
    public void setModPasswordTest() {
        Room room = new Room();
        room.setModPassword("123ab");
        Assertions.assertEquals(room.getModPassword(), "123ab");
    }
    

    @Test
    public void testEqualsSelf() {
        Room test = new Room();

        Assertions.assertEquals(test, test, "Rooms should be equal to themselves");
    }

    @Test
    public void testNotEqualsOtherObject() {
        Room test = new Room();

        // different object
        int notRoom = 5;
        Assertions.assertFalse(test.equals(notRoom), "A Room cannot be equal to another object");
    }

    @Test
    public void testNotEqualsOtherRoom() {
        Room room1 = new Room();
        Room room2 = new Room();
        Assertions.assertNotEquals(room1, room2);
    }

    @Test
    public void saveAndRetrieveRoomTest() {
        Room room = new Room();
        String id = room.getId();
        roomRepository.save(room);

        Room room2 = roomRepository.getOne(id);
        Assertions.assertEquals(room.getId(), room2.getId());
    }

    @Test
    public void questionListTest() {
        Room room = new Room();
        List<Question> q = room.getQuestions();
        Assertions.assertNotNull(q);
    }

    @Test
    public void emptyQuestionListTest() {
        Room room = new Room();
        List<Question> q = room.getQuestions();
        Assertions.assertEquals(q.size(), 0,
                "Newly instantiated room's list of questions must be empty");
    }
}
