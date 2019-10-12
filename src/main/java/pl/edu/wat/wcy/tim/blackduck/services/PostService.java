package pl.edu.wat.wcy.tim.blackduck.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.wat.wcy.tim.blackduck.models.*;
import pl.edu.wat.wcy.tim.blackduck.repositories.FolderRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.HashtagRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.PostRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.PostRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.PostResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.RequestValidationComponent;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    UserRepository userRepository;

    PostRepository postRepository;

    FolderRepository folderRepository;

    JwtProvider jwtProvider;

    ResponseMapper responseMapper;

    HashtagRepository hashtagRepository;

    RequestValidationComponent validationComponent;

    @Autowired
    public PostService(
            UserRepository userRepository,
            PostRepository postRepository,
            FolderRepository folderRepository,
            JwtProvider jwtProvider,
            ResponseMapper responseMapper,
            HashtagRepository hashtagRepository,
            RequestValidationComponent validationComponent
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.folderRepository = folderRepository;
        this.jwtProvider = jwtProvider;
        this.responseMapper = responseMapper;
        this.hashtagRepository = hashtagRepository;
        this.validationComponent = validationComponent;
        this.hashtagRepository = hashtagRepository;
    }


    public void post(PostRequest request, HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        Optional<Folder> folder = folderRepository.findById(request.getFolderId());
        Post post = ObjectMapper.toObject(request);
        post.setAuthor(user);

        //FOLDER
        if (folder.isPresent()) {
            post.setRootFolder(folder.get());
        } else {
            Optional<Folder> defaultFolder = folderRepository.findByOwnerAndFolderName(user, "default");
            if (defaultFolder.isPresent()) {
                post.setRootFolder(defaultFolder.get());
            } else {
                Folder newFolder = new Folder(user, "default", "default");
                folderRepository.save(newFolder);
                post.setRootFolder(newFolder);
            }
        }

        store(request.getFile());

        /////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //URL
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path(user.getUsername()).path("/").path(request.getFile().getOriginalFilename()).toUriString();
        System.out.println(url);
        post.setContentUrl(url);

        //CONTENT TYPE
        String fileType = request.getFile().getOriginalFilename().split("\\.")[1];
        System.out.println(fileType);
        ContentType contentType;

        if (fileType.toLowerCase().equals("png") || fileType.toLowerCase().equals("jpg")) {
            contentType = ContentType.PHOTO;
        } else if (fileType.toLowerCase().equals("mov") || fileType.toLowerCase().equals("mp4")) {
            contentType = ContentType.VIDEO;
        } else {
            throw new AuthenticationException("File not recognized");
        }
        post.setContentType(contentType);

        //VID URL
        String vurl = ServletUriComponentsBuilder.fromCurrentContextPath().path(user.getUsername()).path("/").path(request.getFile().getOriginalFilename()).toUriString();
        System.out.println(vurl);
        if (vurl != null && (fileType.toLowerCase().equals("png") || fileType.toLowerCase().equals("jpg"))) {
            post.setVidPhotoUrl(vurl);
        }else{
            throw new AuthenticationException("File is not a type of photo");
        }

        //HASHTAGS
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        List<Hashtag> finalH = new ArrayList<>();
        Hashtag hashtag;
        String whole = request.getDescription();
        String[] splited = whole.split("\\s+");
        for (String s: splited){
            if(s.contains("#")){
                hashtag = hashtagRepository.findByName(s);
                finalH.add(hashtag);
                if(!hashtagList.contains(hashtag)){
                    Hashtag hash = new Hashtag();
                    hash.setName(s);
                    hashtagRepository.save(hash);
                    finalH.add(hash);
                }
            }
        }
        post.setHashtags(finalH);

        postRepository.save(post);
    }

    public PostResponse getPost(Integer id) throws IllegalArgumentException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            //loadFile(post.get().getContentUrl());

            return responseMapper.toResponse(post.get());
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get(System.getProperty("user.dir") + "\\upload-dir");

    public void store(MultipartFile file) {

        try {
            String location = rootLocation + "\\" + file.getOriginalFilename();
            System.out.println(location);
            File fileToSave = new File(location);
            OutputStream os = new FileOutputStream(fileToSave);
            os.write(file.getBytes());
            os.close();
//        this.rootLocation.resolve(file.getOriginalFilename())
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
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

    public Page<PostResponse> getPosts(Pageable pageable, HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        Page<Post> posts = postRepository.findAllByAuthorInOrderByCreationDate(user.getFollowedUsers(), pageable);
        return posts.map(p -> responseMapper.toResponse(p));

    }

    public List<PostResponse> getPostSearch(String text) {
        List<Post> results = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (post.getTitle().contains(text) || post.getTitle().equalsIgnoreCase(text)) {
                results.add(post);
            }
        }
        if (results.size() == 0) {
            throw new IllegalArgumentException("Post not found");
        }

        List<PostResponse> pr = new ArrayList<>();
        for (Post post : results) {
            pr.add(responseMapper.toResponse(post));
        }
        return pr;
    }

    public List<PostResponse> myPosts(HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        return postRepository.findAllByAuthor(user).stream().map(it -> responseMapper.toResponse(it)).collect(Collectors.toList());
    }
}
