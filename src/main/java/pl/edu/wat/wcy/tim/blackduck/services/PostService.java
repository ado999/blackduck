package pl.edu.wat.wcy.tim.blackduck.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.wat.wcy.tim.blackduck.models.*;
import pl.edu.wat.wcy.tim.blackduck.repositories.FolderRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.PostRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.PostRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.CommentResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.PostResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;


import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public void post (PostRequest request, HttpServletRequest req) throws AuthenticationException{
        validateRequest(req);
        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));
        Optional<Folder> folder = folderRepository.findById(request.getFolderId());
        Post post = ObjectMapper.toObject(request);
        post.setAuthor(user.get());

        //FOLDER
        if(folder.isPresent()){
            post.setRootFolder(folder.get());
        } else {
            Optional<Folder> defaultFolder = folderRepository.findByOwnerAndFolderName(user.get(), "default");
            if(defaultFolder.isPresent()){
                post.setRootFolder(defaultFolder.get());
            }else {
                Folder newFolder = new Folder(user.get(), "default", "default");
                folderRepository.save(newFolder);
                post.setRootFolder(newFolder);
            }
        }

        store(request.getFile());

        /////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //URL
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path(rootLocation.toString()).path(request.getFile().getName()).toUriString();
        post.setContentUrl(url);

        //CONTENT TYPE
        String fileType = request.getFile().getOriginalFilename().split("\\.")[1];
        System.out.println(fileType);
        ContentType contentType = null;

        if(fileType.equals("png") || fileType.equals("jpg")){
            contentType = ContentType.PHOTO;
        }else if(fileType.equals("mov") || fileType.equals("mp4")){
            contentType = ContentType.VIDEO;
        }else{
            throw new AuthenticationException("File not recognized");
        }
        post.setContentType(contentType);

        postRepository.save(post);
    }

    public PostResponse getPost(Integer id, HttpServletRequest req) throws IllegalArgumentException, AuthenticationException{
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()){
            loadFile(post.get().getContentUrl());
            return responseMapper.toResponse(post.get());
        } else {
            throw new IllegalArgumentException("Post not found");
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

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("\\upload-dir");

    public void store(MultipartFile file) {

        try {
            String location = System.getProperty("user.dir") + rootLocation + "\\" + file.getOriginalFilename();
            System.out.println(location);
            File fileToSave = new File(location);
            OutputStream os = new FileOutputStream(fileToSave);
            os.write(file.getBytes());
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
//        this.rootLocation.resolve(file.getOriginalFilename())
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }


}
