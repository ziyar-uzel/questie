package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

@DataJpaTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void constructorTest() {
        User user = new User();
        Assertions.assertNotNull(user);
    }

    @Test
    public void testGetSetUsername() {
        User test = new User();
        test.setUsername("doge");
        Assertions.assertEquals("doge", test.getUsername());
    }

    @Test
    public void testConstructorWithUsername() {
        User user = new User("doge");
        Assertions.assertEquals("doge", user.getUsername());
    }

    @Test
    public void testConstructorWithUsernameAndMod() {
        User user = new User("doge", "1234", true);
        Assertions.assertEquals("doge", user.getUsername());
        Assertions.assertEquals("1234", user.getRoomId());
        Assertions.assertTrue(user.isModerator());
    }

    @Test
    public void testGetSetAuthToken() {
        User test = new User();
        test.setAuthToken("fa5725fc-5d07-47c0-93d1-08de8f43d1ad");
        Assertions.assertEquals("fa5725fc-5d07-47c0-93d1-08de8f43d1ad", test.getAuthToken());
    }

    @Test
    public void testGetSetRoomID() {
        User test = new User();
        Assertions.assertNull(test.getRoomId(),
                "Newly instantiated room must have roomid set to null");
        String id = "7febc3c";
        test.setRoomId(id);
        Assertions.assertEquals(id, test.getRoomId());
    }

    @Test
    public void joinRoomTest() {
        User test = new User();

        String id = "7febc3c";
        test.joinRoom(id);
        Assertions.assertEquals(id, test.getRoomId());
    }

    @Test
    public void testConstructorWithRoom() {
        String id = "7febc3c";
        User test = new User("doge", id);

        Assertions.assertEquals(id, test.getRoomId());
    }

    @Test
    public void testGenerateNewAuthToken() {
        User test = new User();

        String authToken = test.generateNewAuthToken();
        Assertions.assertEquals(authToken, test.getAuthToken());

        test.generateNewAuthToken();
        Assertions.assertNotEquals(authToken, test.getAuthToken());
    }

    @Test
    public void testPromoteToMod() {
        User test = new User();
        // default user should not a mod
        Assertions.assertFalse(test.isModerator());
        test.promoteToModerator();

        Assertions.assertTrue(test.isModerator());
    }

    @Test
    public void testUpvoteQuestion() {
        User test = new User();
        test.setUsername("dogecoin_slayer");
        Question question = new Question("hello", "dogecoin_slayer");
        test.upvoteQuestion(question);
        Assertions.assertTrue(test.hasUpvotedQuestion(question));
    }

    @Test
    public void testRemoveUpvoteFromQuestion() {
        User test = new User();
        test.setUsername("dogecoin_slayer");
        Question question = new Question("hello", "dogecoin_slayer");
        test.upvoteQuestion(question);
        test.removeUpvoteFromQuestion(question);
        Assertions.assertFalse(test.hasUpvotedQuestion(question));
    }

    @Test
    public void testEquals() {
        User test = new User();
        test.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        test.setUsername("dogecoin_slayer");
        test.setRoomId("7febc3c");

        Assertions.assertEquals(test, test, "Users should be equal to themselves");

        // different object
        int notUser = 5;
        Assertions.assertFalse(test.equals(notUser),
                "A user cannot be equal to a different object");

        // identical user
        User user0 = new User();
        user0.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        user0.setRoomId("7febc3c");
        user0.setUsername("dogecoin_slayer");

        Assertions.assertEquals(test, user0, "Two identical users should be equal");

        // user w/different authtoken
        User user1 = new User();
        user1.setAuthToken("9a22-47c0-93d1-08de8f43m7hc");
        user1.setRoomId("7febc3c");
        user1.setUsername("dogecoin_slayer");

        Assertions.assertFalse(test.equals(user1),
                "Users with different authtokens should not be equal");

        // user w/different roomId
        User user2 = new User();
        user2.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        user2.setRoomId("5");
        user2.setUsername("dogecoin_slayer");

        Assertions.assertFalse(test.equals(user2), "Users with different Ids should not be equal");


        // user w/different Username
        User user3 = new User();
        user3.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        user3.setRoomId("7febc3c");
        user3.setUsername("protector_dogecoin");

        Assertions.assertFalse(test.equals(user2),
                "Users with different usernames should not be equal");
    }

}