package nl.tudelft.oopp.demo.communication;

import java.net.URI;
import nl.tudelft.oopp.demo.data.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserClassTest {
    @Test
    public void testUserNotNull() {
        User test = new User();
        Assertions.assertNotNull(test);
    }

    @Test
    public void testGetSetAuthToken() {
        User test = new User();
        test.setAuthToken("fa5725fc-5d07-47c0-93d1-08de8f43d1ad");
        Assertions.assertEquals(test.getAuthToken(), "fa5725fc-5d07-47c0-93d1-08de8f43d1ad");
    }

    @Test
    public void testGetSetId() {
        User test = new User();
        test.setId("3");
        Assertions.assertEquals(test.getId(), "3");
    }

    @Test
    public void testGetSetUsername() {
        User test = new User();
        test.setUsername("DemoUser");
        Assertions.assertEquals(test.getUsername(), "DemoUser");
    }

    @Test
    public void testExistsTrue() {
        User test = new User();
        test.setAuthToken("fa5725fc-5d07-47c0-93d1-08de8f43d1ad");
        Assertions.assertTrue(test.exists());
    }

    @Test
    public void testExistsFalse() {
        User test = new User();
        Assertions.assertFalse(test.exists());
    }

    @Test
    public void testExistsEmpty() {
        User test = new User();
        test.setAuthToken("");
        Assertions.assertFalse(test.exists());
    }

    @Test
    public void testSign() {
        // FIXME needs to be tested after normal method is fixed
        User test = new User();
        test.setId("1");
        test.setUsername("DemoUser");
        test.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        URI demoUrl = URI.create("test/api/rooms");
        Assertions.assertEquals(URI.create(
                "test/api/rooms?userId=1&authToken=5d07-47c0-93d1-08de8f43d1ad&username=DemoUser"),
                test.sign(demoUrl));
    }

    @Test
    public void testEquals() {
        User test = new User();
        test.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        test.setId("1");
        test.setUsername("dogecoin_slayer");

        Assertions.assertEquals(test, test, "Users should be equal to themselves");

        // different object
        int notUser = 5;
        Assertions.assertFalse(test.equals(notUser), "A user cannot be equal to another object");

        // identical user
        User user0 = new User();
        user0.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        user0.setId("1");
        user0.setUsername("dogecoin_slayer");

        Assertions.assertEquals(test, user0, "Identical user objects should be equal");

        // user w/different authtoken
        User user1 = new User();
        user1.setAuthToken("9a22-47c0-93d1-08de8f43m7hc");
        user1.setId("1");
        user1.setUsername("dogecoin_slayer");

        Assertions.assertFalse(test.equals(user1),
                "Users with different authtokens should not be equal");

        // user w/different Id
        User user2 = new User();
        user2.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        user2.setId("5");
        user2.setUsername("dogecoin_slayer");

        Assertions.assertFalse(test.equals(user2),"Users with different Ids should not be equal");

        // user w/different Username
        User user3 = new User();
        user3.setAuthToken("5d07-47c0-93d1-08de8f43d1ad");
        user3.setId("1");
        user3.setUsername("protector_dogecoin");

        Assertions.assertFalse(test.equals(user2),
                "Users with different usernames should not be equal");

    }
}
