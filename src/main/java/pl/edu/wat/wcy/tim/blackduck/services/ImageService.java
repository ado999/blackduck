package pl.edu.wat.wcy.tim.blackduck.services;


import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.models.Image;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.ImageRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ImageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    List<String> files = new ArrayList<String>();

    public ImageService() {
    }

    public ImageService(List<String> files) {
        this.files = files;
    }

    public void store(InputStream file, String filename) {
        try {
            Files.copy(file, this.rootLocation.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
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
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    public void deleteImage(String path){
        try {
            Files.delete(this.rootLocation.resolve(path));
        } catch (Exception e) {
            throw new RuntimeException("FAIL DELETING FILE");
        }
    }

    public ResponseMessage addImage(@org.jetbrains.annotations.NotNull @Valid @RequestBody ImageRequest request) throws UserNotFoundException {
        String randid = UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(request.getOrginalfilename());
        String filename = request.getUsername().replace(" ","_") + "_" + randid;
        byte[] data = Base64.getDecoder().decode(request.getFile());
        store(new ByteArrayInputStream(data), filename + "." + extension);

        BufferedImage thumbImg = null;
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 150, Scalr.OP_ANTIALIAS);
            ImageIO.write( img, "png", baos );
            store(new ByteArrayInputStream(baos.toByteArray()), filename +"_thumb" + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(request.getFile() != null) {
            files.add(filename + "." + extension);
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                    ()-> new UserNotFoundException()
            );

            List<Image> image = new ArrayList<>();
            image.add(new Image(filename + "." + extension, filename +"_thumb" + ".png", user));
            imageRepository.saveAll(image);

            return new ResponseMessage(filename + "." + extension);
        }else{
            return new ResponseMessage("bad");
        }
    }

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("upload");



}
