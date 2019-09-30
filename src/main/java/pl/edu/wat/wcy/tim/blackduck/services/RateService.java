package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.models.Post;
import pl.edu.wat.wcy.tim.blackduck.models.Rate;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.PostRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.RateRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.RateRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.RateResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Service
public class RateService {

    UserRepository userRepository;
    JwtProvider jwtProvider;
    RateRepository rateRepository;
    ResponseMapper responseMapper;
    PostRepository postRepository;

    @Autowired
    public RateService(UserRepository userRepository, JwtProvider jwtProvider, RateRepository rateRepository, ResponseMapper responseMapper, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.rateRepository = rateRepository;
        this.responseMapper = responseMapper;
        this.postRepository = postRepository;
    }

    public void add (@Valid @RequestBody RateRequest request, HttpServletRequest req) throws AuthenticationException {
        validateRequest(req);
        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));
        Optional<Post> post = postRepository.findById(request.getPost_id());
        Rate rate = ObjectMapper.toObject(request);
        if(post.isPresent()){
            rate.setRootPost(post.get());
        }else{
            throw new AuthenticationException("Post not found");
        }
        if(user.isPresent()){
            rate.setFromUser(user.get());
        } else {
            throw new AuthenticationException("User not found");
        }
        rateRepository.save(rate);
    }
//
//    public RateResponse getRate(Integer id, HttpServletRequest req) throws IllegalArgumentException{
//        Optional<Rate> rate = rateRepository.findById(id);
//        if (rate.isPresent()){
//            return responseMapper.toResponse(rate.get());
//        } else {
//            throw new IllegalArgumentException("Rate not found");
//        }
//    }

    private void validateRequest(HttpServletRequest req) throws AuthenticationException{
        Optional<User> user = userRepository.findByUsername(
                jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req))
        );
        if(!user.isPresent()){
            throw new AuthenticationException("User not found");
        }
    }
}
