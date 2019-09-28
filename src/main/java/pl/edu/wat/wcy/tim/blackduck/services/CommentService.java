package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.models.Comment;
import pl.edu.wat.wcy.tim.blackduck.models.Image;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.CommentRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.CommentRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.CommentResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    public ResponseMessage sendComment (@Valid @RequestBody CommentRequest request){

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        Optional<Image> image = imageRepository.findById(Integer.parseInt(request.getImageId()));

        Comment comment = new Comment(request.getDescription(), user.get(), new Date(), image);

        commentRepository.save(comment);
        return new ResponseMessage("ok");
    }

    public CommentResponse getCommentInfo(@PathVariable Integer id){
        Comment comment = commentRepository.findCommentById(id);

        return new CommentResponse(Integer.toString(comment.getId()), comment.getDescription(), comment.getDate().toString(), Integer.toString(comment.getImage().getId()), comment.getUser().getUsername());
    }
}