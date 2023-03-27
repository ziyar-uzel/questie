package nl.tudelft.oopp.demo.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class Api {

    protected User currentUser;
    @Autowired
    protected UserRepository userRepository;


    Map<String, String> getCookiesFromRequest(HttpServletRequest request) {
        Map<String, String> cookies = new HashMap<>();
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies != null) {
            for (Cookie cookie : requestCookies) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookies;
    }

    // root of current user setting
    protected User getUserFromRequest(HttpServletRequest request) throws Exception {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> cookies = getCookiesFromRequest(request);

        String userId = null;
        String authToken = null;

        System.out.println("[#] request params are " + requestParams.entrySet());
        System.out.println("[#] cookies are " + cookies.entrySet());

        if (requestParams.get("userId") != null) {
            userId = requestParams.get("userId")[0];
        } else {
            userId = cookies.get("userId");
        }

        if (requestParams.get("authToken") != null) {
            authToken = requestParams.get("authToken")[0];
        } else {
            authToken = cookies.get("authToken");
        }

        System.out.println("user stuff: >>>>" + authToken + "<<<<" + userId);

        if ((userId != null) && userRepository.existsByIdString(userId)) {
            User user = userRepository.getOneByIdString(userId);
            if (user.getAuthToken().equals(authToken)) {
                System.out.println("[#] welcome back [" + user + "]");

                // check if user is banned
                if (user.isBanned()) {
                    System.out.println("[#] user [" + user + "] is BANNED");
                    throw new Exception("You're banned");
                }

                return user;
            }
        }

        return null;
    }

    protected User getUserFromRequest(HttpServletRequest request, HttpServletResponse response)
                                                                            throws Exception {
        User user = getUserFromRequest(request);
        if (user != null) {
            updateUserAuthAndSave(user, response); // re-new the auth token
        }
        return user;
    }



    protected User getOrCreateUserFromRequest(HttpServletRequest request) throws Exception {
        User currentUser = getUserFromRequest(request);

        if (currentUser == null) {
            currentUser = createNewUser(request.getParameter("username"));
            System.out.println("[#] created user " + currentUser);
        }
        return currentUser;
    }

    protected User setCurrentUserOrThrow(HttpServletRequest request) throws Exception {
        User currentUser = getUserFromRequest(request);
        if (currentUser == null) {
            throw new Exception("Could not authenticate user.");
        }
        this.currentUser = currentUser;
        return currentUser;
    }

    protected User setCurrentUserIfModOrThrow(HttpServletRequest request) throws Exception {
        setCurrentUserOrThrow(request);
        if (!(currentUser.isModerator())) {
            throw new Exception("You are not a moderator!");
        }

        return currentUser;
    }

    /**
     * <p>
     * Generates a unique string, and sets it as the user's next
     * auth token. It appends a cookie called "authToken" which
     * will match the value stored in the database.
     * </p>
     *
     * <p>
     * To authorize a requests as a particular user in the future,
     * the request needs to have a cookie "authToken" which will match
     * the token in the database.
     * </p>
     *
     * <p>>
     * After each successful request, the authToken should be re-newed,
     * in order to increase security.
     * </p>
     *
     * <p>
     * "Note: the 'response' parameter is a HttpServletResponse object
     * </p>
     * @param user the user for whom the generate and update their auth token
     * @param response the http response of the request, in order to add the
     *                  "authToken" cookie.
     */
    public void updateUserAuthAndSave(User user, HttpServletResponse response) {
        user.generateNewAuthToken();
        userRepository.saveAndFlush(user);

        updateUserCookies(user, response);
    }


    /**
     * creates a user instance with passed username.
     * @return the created user instance
     */

    public User createNewUser(String username) {
        User user;
        user = new User(username);
        return user;
    }

    /**
     * updates the cookies of the user that is passed within parameters.
     */

    public void updateUserCookies(User user, HttpServletResponse response) {
        System.out.println("[#] updating user cookies: id:[" + user.id + "] + authToken: ["
                + user.getAuthToken() + " ]!");

        Cookie authCookie = new Cookie("authToken", user.getAuthToken());
        authCookie.setPath("/"); // session cookies
        authCookie.setHttpOnly(true); // session cookies
        System.out.println("[#] setting auth cookie: [" + user.id + "]!");
        response.addCookie(authCookie);

        System.out.println("[#] user.id: [" + user.id + "]!");
        Cookie cookie = new Cookie("userId", user.id.toString());
        cookie.setPath("/"); // session cookies
        cookie.setHttpOnly(true); // session cookies
        System.out.println("[#] setting userId cookie: [" + user.id + "]!");

        response.addCookie(cookie);
    }

}
