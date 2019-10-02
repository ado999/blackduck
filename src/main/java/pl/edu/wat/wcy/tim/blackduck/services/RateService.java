package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.List;
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
        Optional<Post> post = postRepository.findById(request.getPostId());
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
        Optional<Rate> ratee = rateRepository.findByFromUserAndRootPost(user.get(), post.get());
        if(!ratee.isPresent()){
            rateRepository.save(rate);
        }else{
            Rate newRate = ratee.get();
            newRate.setRate(request.getRate());
            rateRepository.save(newRate);
        }

        Optional<Post> updatePost = postRepository.findById(request.getPostId());
        Post p = updatePost.get();
        int sum = 0;
        List<Rate> rates = p.getRates();
        for(Rate r: rates){
            sum += rate.getRate();
        }
        p.setRate((double)sum/rates.size());
        postRepository.save(p);

    }

    public RateResponse getRate(Integer id, HttpServletRequest req) throws IllegalArgumentException{
        Optional<Rate> rate = rateRepository.findById(id);
        if (rate.isPresent()){
            Rate r = rate.get();
            return responseMapper.toResponse(r);
        } else {
            throw new IllegalArgumentException("Rate not found");
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
