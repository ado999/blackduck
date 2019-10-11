package pl.edu.wat.wcy.tim.blackduck.services;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.wat.wcy.tim.blackduck.models.Comment;
import pl.edu.wat.wcy.tim.blackduck.models.Post;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.CommentRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.PostRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.CommentRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.CommentResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    CommentRepository commentRepository;

    UserRepository userRepository;

    JwtProvider jwtProvider;

    PostRepository postRepository;

    ResponseMapper responseMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, JwtProvider jwtProvider, PostRepository postRepository, ResponseMapper responseMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.postRepository = postRepository;
        this.responseMapper = responseMapper;
    }

    public CommentResponse post (@Valid @RequestBody CommentRequest request, HttpServletRequest req) throws IllegalArgumentException, AuthenticationException {
        validateRequest(req);
        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));
        Optional<Post> post = postRepository.findById(request.getPostId());
        Comment comment = ObjectMapper.toObject(request);
        comment.setAuthor(user.get());
        if(post.isPresent()){
            comment.setRootPost(post.get());
            } else {
            throw new AuthenticationException("Comment not found");
        }
        commentRepository.save(comment);
        return responseMapper.toResponse(comment);
    }

    public CommentResponse getComment(Integer id, HttpServletRequest req) throws IllegalArgumentException{
        Optional<Comment> comment = Optional.ofNullable(commentRepository.findCommentById(id));
        if (comment.isPresent()){
            Comment com = comment.get();
            return responseMapper.toResponse(com);
        } else {
            throw new IllegalArgumentException("Comment not found");
        }
    }

    private void validateRequest(HttpServletRequest req) throws AuthenticationException{
        Optional<User> user = userRepository.findByUsername(
                jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req))
        );
        if(!user.isPresent()){
            throw new AuthenticationException("User not found");
        }
    }
}
