package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.requests.FolderRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.FolderResponse;
import pl.edu.wat.wcy.tim.blackduck.services.FolderService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/folders")
public class FolderController {

    private FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService){
        this.folderService = folderService;
    }

    @PostMapping
    public ResponseEntity addFolder (@Valid @RequestBody FolderRequest request, HttpServletRequest req){
        try {
            return new ResponseEntity<>(folderService.add(request, req), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<Object> myFolders(HttpServletRequest req){
        try {
            return new ResponseEntity<>(folderService.myFolders(req), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getFolder(@PathVariable Integer id){
        try {
            FolderResponse response = folderService.getFolder(id);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
