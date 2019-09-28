package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.repositories.CommentRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.CommentRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.CommentResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;
import pl.edu.wat.wcy.tim.blackduck.services.CommentService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    CommentService commentService;

    public CommentController() {
    }

    @PostMapping("/sendComment")
    public ResponseEntity<?> sendComment(@Valid @RequestBody CommentRequest request){
        ResponseMessage responseMessage = commentService.sendComment((request));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/getComment")
    public ResponseEntity<?> getCommentInfo(@PathVariable Integer id){
        CommentResponse commentResponse = commentService.getCommentInfo(id);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }
}
