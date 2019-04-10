package pl.edu.wat.wcy.tim.blackduck.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.ImageRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;
import pl.edu.wat.wcy.tim.blackduck.services.ImageService;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.ImageServiceImpl;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value="/images")
public class ImageController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageServiceImpl imageServiceImpl;

    @Autowired
    ImageService imageService;

    @PostMapping("/addimage")
    public ResponseEntity<?> addImage(@Valid @RequestBody ImageRequest request) {

        ResponseMessage response = imageService.addImage(request);

        if(response.equals("bad")){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/getimage/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = imageServiceImpl.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "multipart/byteranges")
                .body(file);
    }
}

