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
import pl.edu.wat.wcy.tim.blackduck.util.RequestValidationComponent;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private UserRepository userRepository;

    private JwtProvider jwtProvider;

    private FolderRepository folderRepository;

    private ResponseMapper responseMapper;

    private RequestValidationComponent validationComponent;

    @Autowired
    public FolderService(
            UserRepository userRepository,
            JwtProvider jwtProvider,
            FolderRepository folderRepository,
            ResponseMapper responseMapper,
            RequestValidationComponent validationComponent
    ) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.folderRepository = folderRepository;
        this.responseMapper = responseMapper;
        this.validationComponent = validationComponent;
    }

    public List<FolderResponse> myFolders(HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        return user.getFolders().stream().map(folder -> responseMapper.toResponse(folder)).collect(Collectors.toList());
    }

    public int add(@Valid @RequestBody FolderRequest request, HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        Folder folder1 = ObjectMapper.toObject(request);
        folder1.setOwner(user);
        folderRepository.save(folder1);
        Optional<Folder> f = folderRepository.findByOwnerAndFolderName(user, request.getFolderName());
        return f.map(Folder::getId).orElse(-1);
    }

    public FolderResponse getFolder(Integer id) throws IllegalArgumentException {
        Optional<Folder> folder = folderRepository.findById(id);
        if (folder.isPresent()) {
            return responseMapper.toResponse(folder.get());
        } else {
            throw new IllegalArgumentException("Folder not found");
        }
    }
}
