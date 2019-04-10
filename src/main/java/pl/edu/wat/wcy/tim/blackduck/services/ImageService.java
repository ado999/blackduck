package pl.edu.wat.wcy.tim.blackduck.services;


import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.models.Image;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.ImageRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.ImageServiceImpl;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ImageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageServiceImpl imageServiceImpl;

    List<String> files = new ArrayList<String>();

    public ImageService() {
    }

    public ImageService(List<String> files) {
        this.files = files;
    }

    public ResponseMessage addImage(@org.jetbrains.annotations.NotNull @Valid @RequestBody ImageRequest request) {
        String randid = UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(request.getOrginalfilename());
        String filename = request.getUsername().replace(" ","_") + "_" + randid;
        byte[] data = Base64.getDecoder().decode(request.getFile());
        imageServiceImpl.store(new ByteArrayInputStream(data), filename + "." + extension);

        BufferedImage thumbImg = null;
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 150, Scalr.OP_ANTIALIAS);
            ImageIO.write( img, "png", baos );
            imageServiceImpl.store(new ByteArrayInputStream(baos.toByteArray()), filename +"_thumb" + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(request.getFile() != null) {
            files.add(filename + "." + extension);
            User user = userRepository.findByUsername(request.getUsername());

            List<Image> image = new ArrayList<>();
            image.add(new Image(filename + "." + extension, filename +"_thumb" + ".png", user));
            imageRepository.saveAll(image);

            return new ResponseMessage(filename + "." + extension);
        }else{
            return new ResponseMessage("bad");
        }
    }

}
