package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.models.Folder;
import pl.edu.wat.wcy.tim.blackduck.models.Post;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.FolderRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.PostRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.PostRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.PostResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Service
public class PostService {

    UserRepository userRepository;

    PostRepository postRepository;

    FolderRepository folderRepository;

    JwtProvider jwtProvider;

    ResponseMapper responseMapper;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository, FolderRepository folderRepository, JwtProvider jwtProvider, ResponseMapper responseMapper) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.folderRepository = folderRepository;
        this.jwtProvider = jwtProvider;
        this.responseMapper = responseMapper;
    }

    public void post (@Valid @RequestBody PostRequest request, HttpServletRequest req) throws IllegalArgumentException, AuthenticationException{
        validateRequest(req);
        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));
        Optional<Folder> folder = folderRepository.findById(request.getFolderId());
        Post post = ObjectMapper.toObject(request);
        post.setAuthor(user.get());
        if(folder.isPresent()){
            post.setRootFolder(folder.get());
        } else {
            throw new AuthenticationException("Folder not found");
        }
        postRepository.save(post);
        //url
        //contenttype
    }

    public PostResponse getPost(Integer id, HttpServletRequest req) throws IllegalArgumentException, AuthenticationException{
        validateRequest(req);
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()){
            return responseMapper.toResponse(post.get());
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    public void validateRequest(HttpServletRequest req) throws AuthenticationException{
        Optional<User> user = userRepository.findByUsername(
                jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req))
        );
        if(!user.isPresent()){
            throw new AuthenticationException("User not found");
        }
    }
}
