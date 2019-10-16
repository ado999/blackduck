package pl.edu.wat.wcy.tim.blackduck.services;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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
import pl.edu.wat.wcy.tim.blackduck.requests.PostRequestPreuploaded;
import pl.edu.wat.wcy.tim.blackduck.responses.PostResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    FileService fileService;

    FrameGrabber frameGrabber;

    private final Path fileLocation = Paths.get("upload-dir");

    @Autowired
    public PostService(
            UserRepository userRepository,
            PostRepository postRepository,
            FolderRepository folderRepository,
            JwtProvider jwtProvider,
            ResponseMapper responseMapper,
            HashtagRepository hashtagRepository,
            RequestValidationComponent validationComponent,
            FileService fileService,
            FrameGrabber frameGrabber
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.folderRepository = folderRepository;
        this.jwtProvider = jwtProvider;
        this.responseMapper = responseMapper;
        this.hashtagRepository = hashtagRepository;
        this.validationComponent = validationComponent;
        this.hashtagRepository = hashtagRepository;
        this.fileService = fileService;
        this.frameGrabber = frameGrabber;
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

        /////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //URL

        if(request.getFile().isEmpty()) throw new IllegalArgumentException("File must be specified");
        String fileExtension = Utils.getExtension(request.getFile().getOriginalFilename());
        if(fileExtension.equals("jpg") || fileExtension.equals("png")){
            post.setContentUrl(fileService.store(request.getFile(), fileExtension));
            post.setContentType(ContentType.PHOTO);
        } else if(fileExtension.equals("mov") || fileExtension.equals("mp4")){
            post.setContentUrl(fileService.store(request.getFile(), fileExtension));
            post.setContentType(ContentType.VIDEO);
            if(request.getVidPhoto().isEmpty()) throw new IllegalArgumentException("Video thumbnail must be specified");
            String thumbnailExtension = Utils.getExtension(request.getVidPhoto().getOriginalFilename());
            if(!thumbnailExtension.equals("jpg") && !thumbnailExtension.equals("png")){
                throw new IllegalArgumentException("Video thumbnail must be a photo");
            }
            post.setVidPhotoUrl(fileService.store(request.getFile(), thumbnailExtension));
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

    public void preuploadedPost(PostRequestPreuploaded request, HttpServletRequest req) throws AuthenticationException {
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

        /////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //URL

        if(request.getFile().isEmpty()) throw new IllegalArgumentException("File must be specified");
        String fileExtension = Utils.getExtension(request.getFile());
        if(fileExtension.equals("jpg") || fileExtension.equals("png")){
            post.setContentUrl(request.getFile());
            post.setContentType(ContentType.PHOTO);
        } else if(fileExtension.equals("mov") || fileExtension.equals("mp4")){
            post.setContentUrl(request.getFile());
            post.setContentType(ContentType.VIDEO);
            if(request.getVidPhoto() == null || request.getVidPhoto().isEmpty()){
                request.setVidPhoto(frameGrabber.saveThumbnail(fileService.loadFile(request.getFile())));
            }
            String thumbnailExtension = Utils.getExtension(request.getVidPhoto());
            if(!thumbnailExtension.equals("jpg") && !thumbnailExtension.equals("png")){
                throw new IllegalArgumentException("Video thumbnail must be a photo");
            }
            post.setVidPhotoUrl(request.getVidPhoto());
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

    public List<PostResponse> getPosts(HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        List<Post> posts = postRepository.findAllByAuthorInOrderByCreationDateDesc(user.getFollowedUsers());
        List<PostResponse> response = new ArrayList<>();
        for(Post p: posts)
            response.add(responseMapper.toResponse(p));
        return response;

    }

    public List<PostResponse> getPostSearch(String text) {
        List<Post> posts = postRepository.findAll();

        return posts.stream().filter(
                post -> {
                    if(post.getTitle().toLowerCase().contains(text.toLowerCase())) return true;
                    if(post.getDescription().toLowerCase().contains(text.toLowerCase())) return true;
                    if(post.getAuthor().getUsername().toLowerCase().contains(text.toLowerCase())) return true;
                    if(post.getAuthor().getFullName().toLowerCase().contains(text.toLowerCase())) return true;
                    return false;
                })
                .map(p -> responseMapper.toResponse(p))
                .collect(Collectors.toList());
    }

    public List<PostResponse> myPosts(HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        return postRepository.findAllByAuthorOrderByCreationDateDesc(user).stream().map(it -> responseMapper.toResponse(it)).collect(Collectors.toList());
    }

    public void getFile(String name, HttpServletResponse response) {
        try {
            File file = fileLocation.resolve(name).toFile();
            String extension = Utils.getExtension(file.getName());
            switch (extension) {
                case "jpg":
                    response.setContentType("image/jpeg");
                    break;
                case "png":
                    response.setContentType("image/png");
                    break;
                case "mp4":
                    response.setContentType("video/mp4");
                    break;
                case "mov":
                    response.setContentType("video/quicktime");
                    break;
            }
            InputStream is = new FileInputStream(file);
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found");
        }
    }
}
