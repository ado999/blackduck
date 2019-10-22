package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.requests.PostRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.PostRequestPreuploaded;
import pl.edu.wat.wcy.tim.blackduck.responses.PostResponse;
import pl.edu.wat.wcy.tim.blackduck.services.PostService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity getPosts(HttpServletRequest req) {
        try {
            return new ResponseEntity<>(postService.getPosts(req), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public ResponseEntity sendPost(@Valid @ModelAttribute PostRequest request, HttpServletRequest req) {
        try {
            postService.post(request, req);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/sendPostUF")
    public ResponseEntity sendPostWithUploadedFiles(@RequestBody PostRequestPreuploaded post, HttpServletRequest req){
        try {
            postService.preuploadedPost(post, req);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/post")
    public ResponseEntity<Object> getPost(@QueryParam("id") int id) {
        try {
            PostResponse response = postService.getPost(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/search/{text}")
    public ResponseEntity<List<PostResponse>> getSearch(@PathVariable String text) {
        List<PostResponse> response = postService.getPostSearch(text);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<Object> myPosts(HttpServletRequest req){
        try {
            return new ResponseEntity<>(postService.myPosts(req), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity foreignPosts(@PathVariable String username, HttpServletRequest req){
        try {
            return new ResponseEntity<>(postService.foreignPosts(username, req), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


//    @PostMapping("/file")
//    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        String message = "";
//        try {
//            postService.store(file);
//            files.add(file.getOriginalFilename());
//
//            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } catch (Exception e) {
//            message = "FAIL to upload " + file.getOriginalFilename() + "!";
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
//        }
//    }

//
//    @GetMapping("/file/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        Resource file = postService.loadFile(filename);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//                .body(file);
//    }
//
//
//    List<String> files = new ArrayList<String>();
}
