package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.LecturePace;
import nl.tudelft.oopp.demo.services.LecturePaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/feedback")
public class LecturePaceController {

    private final LecturePaceService lecturePaceService;

    @Autowired
    public LecturePaceController(LecturePaceService lecturePaceService) {
        this.lecturePaceService = lecturePaceService;
    }



    @PutMapping("/upvote/{roomId}/{userId}")
    public LecturePace sendUpVoteFeedback(@PathVariable  String roomId,
                                    @PathVariable long userId) {
        return lecturePaceService.upvotePace(roomId, userId);
    }

    @PutMapping("/downvote/{roomId}/{userId}")
    public LecturePace sendDownVoteFeedback(@PathVariable  String roomId,
                                    @PathVariable long userId) {
        return lecturePaceService.downVotePace(roomId, userId);
    }

    @GetMapping(path = "/receive/{roomId}")
    public LecturePace findFeedback(@PathVariable String roomId) {

        return lecturePaceService.findFeedback(roomId);
    }
}
