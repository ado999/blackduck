package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.models.Folder;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.FolderRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.FolderRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.FolderResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Service
public class FolderService {

    UserRepository userRepository;

    JwtProvider jwtProvider;

    FolderRepository folderRepository;

    ResponseMapper responseMapper;

    @Autowired
    public FolderService(UserRepository userRepository, JwtProvider jwtProvider, FolderRepository folderRepository, ResponseMapper responseMapper) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.folderRepository = folderRepository;
        this.responseMapper = responseMapper;
    }

    public void add (@Valid @RequestBody FolderRequest request, HttpServletRequest req) throws AuthenticationException {
        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));
        Folder folder1 = ObjectMapper.toObject(request);
        if(user.isPresent()){
            folder1.setOwner(user.get());
        } else {
            throw new AuthenticationException("User not found");
        }
        folderRepository.save(folder1);
    }

    public FolderResponse getFolder(Integer id) throws IllegalArgumentException{
        Optional<Folder> folder = folderRepository.findById(id);
        if (folder.isPresent()){
            return responseMapper.toResponse(folder.get());
        } else {
            throw new IllegalArgumentException("Folder not found");
        }
    }
}
