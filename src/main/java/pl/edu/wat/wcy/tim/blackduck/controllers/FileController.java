package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.wcy.tim.blackduck.responses.FilenameResponse;
import pl.edu.wat.wcy.tim.blackduck.services.FileService;
import pl.edu.wat.wcy.tim.blackduck.services.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/file")
public class FileController {

    private final PostService postService;
    private final FileService fileService;

    @Autowired
    public FileController(PostService postService, FileService fileService) {
        this.postService = postService;
        this.fileService = fileService;
    }

    @GetMapping("/{text}")
    public void getFile(@PathVariable String text,
                        HttpServletResponse response) {

        try {
            postService.getFile(text, response);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity putFile(@RequestParam("file") MultipartFile file, HttpServletRequest req){
        if(!file.isEmpty()){
            FilenameResponse response = new FilenameResponse(fileService.putFile(file, req));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
