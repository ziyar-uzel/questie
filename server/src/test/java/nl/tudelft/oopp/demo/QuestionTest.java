package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.entities.Question;

import org.junit.Assert;

public class QuestionTest {

    /**
     * Object used for tests.
     */
    private Question question1;

    /**
     * Test default constructor.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        question1 = new Question();
        Assert.assertNotNull(question1);
    }

    /**
     * Test modified constructor.
     */
    @org.junit.Test
    public void testModifiedConstructor() {
        question1 = new Question("test1", "student1");
        Assert.assertNotNull(question1);
    }

    /**
     * Test the getter for content.
     */
    @org.junit.Test
    public void testGetContent() {
        question1 = new Question("test1", "student1");
        String content = question1.getContent();
        Assert.assertEquals("Failed to get the correct content.",
                "test1", content);
    }

    /**
     * Test the getter for username.
     */
    @org.junit.Test
    public void testGetUsername() {
        question1 = new Question("test1", "student1");
        String username = question1.getUsername();
        Assert.assertEquals("Failed to get the correct username.",
                "student1", username);
    }

    /**
     * Test the setter for username.
     */
    @org.junit.Test
    public void testSetUsername() {
        question1 = new Question("test1", "student1");
        question1.setUsername("anotherStudent");
        Assert.assertEquals("Newly set username wasn't the same as expected.",
                "anotherStudent", question1.getUsername());
    }

    /**
     * Test the getter for score.
     */
    @org.junit.Test
    public void testGetScore() {
        question1 = new Question("test1", "student1");
        int score = question1.getScore();
        Assert.assertEquals("Score was expected to be 0, but wasn't.",
                0, score);
    }

    /**
     * Test the setter for roomId.
     */
    @org.junit.Test
    public void testSetRoomId() {
        question1 = new Question("test1", "student1");
        question1.setRoomId("123456");
        String roomId = question1.getRoomId();
        Assert.assertEquals("Roomid was expected to be the same, but wasn't.",
                roomId, "123456");
    }

    /**
     * Test 1 whether the equals method works as intended.
     */
    @org.junit.Test
    public void testEqualsSame() {
        question1 = new Question("test1", "student1");
        Assert.assertTrue("Objects are the same.", question1.equals(question1));
    }

    /**
     * Test 2 whether the equals method works as intended.
     */
    @org.junit.Test
    public void testEqualsDifferentContent() {
        question1 = new Question("test1", "student1");
        Question question2 = new Question("test2", "student1");
        Assert.assertFalse("The same username, but not the same object",
                question1.equals(question2));
    }

    /**
     * Test 3 whether the equals method works as intended.
     */
    @org.junit.Test
    public void testEqualsDifferentUsername() {
        question1 = new Question("test1", "student1");
        Question question3 = new Question("test1", "student2");
        Assert.assertFalse("The same content, but not the same object",
                question1.equals(question3));
    }
}

