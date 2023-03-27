package nl.tudelft.oopp.demo.services;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.LecturePace;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.LecturePaceRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
@AutoConfigureMockMvc
class LecturePaceServiceTest {
    @Autowired
    private LecturePaceService lecturePaceService;

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LecturePaceRepository lecturePaceRepository;


    Room room;
    Room room2;
    Room room3;
    User user;
    User user2;
    User user3;
    LecturePace lecturePace;
    LecturePace lecturePace2;
    LecturePace lecturePace3;
    List upVotedUsers;
    List downVotedUsers;

    @BeforeEach
    void beforeEach() {

        upVotedUsers = new ArrayList();
        downVotedUsers = new ArrayList();
        room = new Room();
        room2 = new Room();
        room3 = new Room();
        user = new User("username",room.getId());
        user2 = new User("username2", room2.getId());
        user3 = new User("username2", room3.getId());
        user.setId(1L);
        user2.setId(2L);
        user3.setId(3L);
        lecturePace = room.getLecturePace();
        lecturePace.setUpVotedUsers(upVotedUsers);
        lecturePace.setDownVotedUsers(downVotedUsers);
        lecturePace.getUpVotedUsers().add(user);
        lecturePace.setPaceUpvote(1);
        lecturePace2 = room2.getLecturePace();
        lecturePace2.setUpVotedUsers(upVotedUsers);
        lecturePace2.setDownVotedUsers(downVotedUsers);
        lecturePace2.getDownVotedUsers().add(user2);
        lecturePace2.setPaceDownvote(1);
        lecturePace3 = room3.getLecturePace();
        lecturePace3.setUpVotedUsers(upVotedUsers);
        lecturePace3.setDownVotedUsers(downVotedUsers);
    }

    @Test
    void upvotePaceWhenUserAlreadyUpVoted() {



        Mockito.when(roomRepository.findById(room.getId()))
                .thenReturn(java.util.Optional.ofNullable(room));

        Mockito.when(lecturePaceRepository
                .findByRoom(room)).thenReturn(java.util.Optional.ofNullable(lecturePace));
        Mockito.when(userRepository
                .getOneByIdString(Long.toString(user.getId()))).thenReturn(user);
        Mockito.when(lecturePaceRepository
                .save(lecturePace)).thenReturn(lecturePace);


        Assertions.assertEquals(lecturePaceService
                .upvotePace(room.getId(),user.getId()), lecturePace);
    }

    @Test
    void upvotePaceWhenUserAlreadyDownVoted() {

        Mockito.when(roomRepository.findById(room2.getId()))
                .thenReturn(java.util.Optional.ofNullable(room2));
        Mockito.when(lecturePaceRepository
                .findByRoom(room2)).thenReturn(java.util.Optional.ofNullable(lecturePace2));
        Mockito.when(userRepository
                .getOneByIdString(Long.toString(user2.getId()))).thenReturn(user2);
        Mockito.when(lecturePaceRepository
                .save(lecturePace2)).thenReturn(lecturePace2);


        Assertions.assertEquals(lecturePaceService
                .upvotePace(room2.getId(),user2.getId()), lecturePace2);
    }


    @Test
    void upvotePaceWhenUserHoldsNoVote() {

        Mockito.when(roomRepository.findById(room3.getId()))
                .thenReturn(java.util.Optional.ofNullable(room3));

        Mockito.when(lecturePaceRepository
                .findByRoom(room3)).thenReturn(java.util.Optional.ofNullable(lecturePace3));
        Mockito.when(userRepository
                .getOneByIdString(Long.toString(user3.getId()))).thenReturn(user3);
        Mockito.when(lecturePaceRepository
                .save(lecturePace3)).thenReturn(lecturePace3);


        Assertions.assertEquals(lecturePaceService
                .upvotePace(room3.getId(),user3.getId()), lecturePace3);
    }


    @Test
    void downVotePaceWhenUserAlreadyDownVoted() {



        Mockito.when(roomRepository.findById(room2.getId()))
                .thenReturn(java.util.Optional.ofNullable(room2));

        Mockito.when(lecturePaceRepository
                .findByRoom(room2)).thenReturn(java.util.Optional.ofNullable(lecturePace2));
        Mockito.when(userRepository
                .getOneByIdString(Long.toString(user2.getId()))).thenReturn(user2);
        Mockito.when(lecturePaceRepository
                .save(lecturePace2)).thenReturn(lecturePace2);


        Assertions.assertEquals(lecturePaceService
                .downVotePace(room2.getId(),user2.getId()), lecturePace2);
    }

    @Test
    void downVotePaceWhenUserAlreadyUpVoted() {



        Mockito.when(roomRepository.findById(room.getId()))
                .thenReturn(java.util.Optional.ofNullable(room));

        Mockito.when(lecturePaceRepository
                .findByRoom(room)).thenReturn(java.util.Optional.ofNullable(lecturePace));
        Mockito.when(userRepository
                .getOneByIdString(Long.toString(user.getId()))).thenReturn(user);
        Mockito.when(lecturePaceRepository
                .save(lecturePace)).thenReturn(lecturePace);


        Assertions.assertEquals(lecturePaceService
                .downVotePace(room.getId(),user.getId()), lecturePace);
    }

    @Test
    void downVotePaceWhenUserHoldsNoVote() {



        Mockito.when(roomRepository.findById(room3.getId()))
                .thenReturn(java.util.Optional.ofNullable(room3));

        Mockito.when(lecturePaceRepository
                .findByRoom(room3)).thenReturn(java.util.Optional.ofNullable(lecturePace3));
        Mockito.when(userRepository
                .getOneByIdString(Long.toString(user3.getId()))).thenReturn(user3);
        Mockito.when(lecturePaceRepository
                .save(lecturePace3)).thenReturn(lecturePace3);


        Assertions.assertEquals(lecturePaceService
                .downVotePace(room3.getId(),user3.getId()), lecturePace3);
    }

    @Test
    void findFeedback() {
        Mockito.when(roomRepository.findById(room.getId()))
                .thenReturn(java.util.Optional.ofNullable(room));
        Mockito.when(lecturePaceRepository
                .findByRoom(room)).thenReturn(java.util.Optional.ofNullable(lecturePace));

        Assertions.assertEquals(lecturePaceService.findFeedback(room.getId()), lecturePace);

    }

    @Test
    void findFeedbackNotEquals() {
        Mockito.when(roomRepository.findById(room.getId()))
                .thenReturn(java.util.Optional.ofNullable(room));
        Mockito.when(lecturePaceRepository
                .findByRoom(room)).thenReturn(java.util.Optional.ofNullable(lecturePace));

        Assertions.assertNotEquals(lecturePaceService
                .findFeedback(room.getId()), lecturePace2);

    }
}