package nl.tudelft.oopp.demo.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.LecturePaceRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;




@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ApiTest extends Api {

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LecturePaceRepository lecturePaceRepository;

    public MockHttpServletRequest request;
    public MockHttpServletResponse response;
    public User testUser;
    public User badUser;

    void setUserCookies() {
        Cookie[] cookies = new Cookie[] {
            new Cookie("userId", Long.toString(testUser.getId())),
            new Cookie("authToken", testUser.getAuthToken())
        };
        request.setCookies(cookies);
    }

    void setUser(long id, String auth) {
        testUser = new User();
        testUser.setId(id);
        testUser.setAuthToken(auth);
        setUserCookies();

        Mockito.when(userRepository.existsByIdString(Long.toString(id)))
                .thenReturn(true);

        Mockito.when(userRepository.getOneByIdString(Long.toString(id)))
                .thenReturn(testUser);
    }

    void assertCookies(Map<String, String> cookies,
                          long expectedUserId,
                          String expectedAuth) {

        Assertions.assertEquals(cookies.get("userId"), Long.toString(expectedUserId));
        Assertions.assertEquals(cookies.get("authToken"), expectedAuth);
    }
    
    @BeforeEach
    void beforeEach() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        setUser(1, "42069");
    }

    @Test
    void getCookiesFromRequestTest() {
        Map<String, String> c = getCookiesFromRequest(request);
        assertCookies(c, 1, "42069");
    }

    @Test
    void getUserFromRequestTest() throws Exception {
        User cookieUser = getUserFromRequest(request);
        Assertions.assertEquals(testUser, cookieUser);
    }

    @Test
    void getUserFromRequestOverloadTest() throws Exception {
        // this method updates the authtoken as well and embeds
        // it in the response
        Assertions.assertEquals(testUser.getAuthToken(), "42069");
        User cookieUser = getUserFromRequest(request, response);
        Assertions.assertEquals(testUser, cookieUser);
        Assertions.assertNotEquals(testUser.getAuthToken(), "42069");
    }

    @Test
    void updateUserCookiesTest() throws Exception {
        updateUserCookies(testUser, response);
        Cookie [] cookies = response.getCookies();
        Map<String, String> c = new HashMap<>();
        c.put("userId", cookies[1].getValue());
        c.put("authToken", cookies[0].getValue());
        assertCookies(c, testUser.getId(), testUser.getAuthToken());
    }

    @Test
    void createNewUserTest() {
        User u = createNewUser("bob");
        Assertions.assertEquals(u.getUsername(), "bob");
    }

    @Test
    void updateUserAuthAndSaveTest() {
        Assertions.assertEquals(testUser.getAuthToken(), "42069");
        updateUserAuthAndSave(testUser, response);
        Assertions.assertNotEquals(testUser.getAuthToken(), "42069");
    }

    @Test
    void setCurrentUserOrThrowTest() {
        HttpServletRequest emptyRequest = new MockHttpServletRequest();
        Assertions.assertThrows(Exception.class, () -> {
            setCurrentUserOrThrow(emptyRequest);
        });

        Assertions.assertDoesNotThrow(() -> {
            setCurrentUserOrThrow(request);
        });

        Assertions.assertEquals(currentUser, testUser);
    }

    @Test
    void setCurrentUserIfModOrThrowTest() {
        Assertions.assertThrows(Exception.class, () -> {
            setCurrentUserIfModOrThrow(request);
        });

        testUser.promoteToModerator();
        Assertions.assertDoesNotThrow(() -> {
            setCurrentUserIfModOrThrow(request);
        });

        Assertions.assertEquals(currentUser, testUser);
    }

    @Test
    void getOrCreateUserFromRequestTest() {
        Assertions.assertDoesNotThrow(() -> {
            User ruser = getOrCreateUserFromRequest(request);
            Assertions.assertEquals(ruser, testUser);
        });

        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest req = new MockHttpServletRequest();
            User ruser = getOrCreateUserFromRequest(req);
            Assertions.assertNotNull(ruser);
        });
    }
}