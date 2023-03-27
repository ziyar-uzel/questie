package nl.tudelft.oopp.demo.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.demo.api.Api;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api/users")
public class UserController extends Api {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**.
     * Bans a user with a provided room id. For the request to be granted,
     * you have to be a moderator.
     * /api/users/ban/1 will ban user with id 1 if you're a moderator
     * @return room
     */
    @PostMapping(path = "/ban/{userId}")
    public User ban(HttpServletRequest request,
                       HttpServletResponse response,
                       @PathVariable long userId) throws Exception {

        return userService.setBan(true, userId, request, response);
    }

    @DeleteMapping(path = "/ban/{userId}")
    public User unban(HttpServletRequest request,
                       HttpServletResponse response,
                       @PathVariable long userId) throws Exception {

        return userService.setBan(false, userId, request, response);
    }

    @GetMapping(path = "/amIBanned")
    public boolean unban(HttpServletRequest request,
                      HttpServletResponse response) throws Exception {
        return userService.isUserBanned(request, response);
    }

}
