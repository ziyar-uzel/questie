package nl.tudelft.oopp.demo.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.api.Api;
import nl.tudelft.oopp.demo.entities.User;

import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends Api {

    @Autowired
    private UserRepository userRepository;

    /**
     * Bans the user with the given id.
     * @param id - the user's Id to ban
     * @param httpRequest - the httpRequest to grab the user creds from
     */
    public User setBan(boolean banned, long id, HttpServletRequest httpRequest,
                             HttpServletResponse response) throws Exception {

        setCurrentUserIfModOrThrow(httpRequest);
        // user requesting to ban is a mod

        if (!userRepository.existsById(id)) {
            throw new Exception("User with id " + id + " does not exist");
        }

        User targetUser = userRepository.findById(id).get();
        targetUser.setBanned(banned);

        userRepository.saveAndFlush(targetUser);
        return targetUser;
    }

    /**
     * Checks if the user identified in the request is banned.
     *
     * @param httpRequest the http request
     * @param response    the response
     * @return whether the user is banned
     */
    public boolean isUserBanned(HttpServletRequest httpRequest,
                                HttpServletResponse response) {
        try {
            setCurrentUserOrThrow(httpRequest);
        } catch (Exception e) {
            if (e.getMessage().equals("You're banned")) {
                return true;
            }
        }
        return false;
    }
}
