package pl.edu.wat.wcy.tim.blackduck.services;

import javaxt.io.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.wcy.tim.blackduck.util.Utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final Path rootLocation = Paths.get(System.getProperty("user.dir") + "\\upload-dir");


    public String putFile(MultipartFile file, HttpServletRequest req) {
        String fileExtension = Utils.getExtension(file.getOriginalFilename());
        if(fileExtension.equals("png") || fileExtension.equals("jpg")){
            return storeImage(file, fileExtension);
        } else {
            return store(file, fileExtension);
        }
    }

    private String storeImage(MultipartFile file, String fileExtension){
        String randUUID = UUID.randomUUID().toString();
        File fileToSave = new File(rootLocation + "\\" + randUUID + "." + fileExtension);
        File thumbToSave = new File(rootLocation + "\\" + randUUID + "_thumb" + "." + fileExtension);
        try {
            Image image = new Image(file.getBytes());
            if(image.getWidth() > image.getHeight()){
                image.setWidth(1024);
            } else {
                image.setHeight(1024);
            }
            image.saveAs(fileToSave);
            image.resize(300, 300, true);
            image.saveAs(thumbToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileToSave.getName();
    }

    String store(MultipartFile file, String fileExtension) {
        File fileToSave;
        try {
            String location = rootLocation + "\\" + UUID.randomUUID().toString() + "." + fileExtension;
            System.out.println(location);
            fileToSave = new File(location);
            OutputStream os = new FileOutputStream(fileToSave);
            os.write(file.getBytes());
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return fileToSave.getName();
    }

    File loadFile(String filename){
        return new File(rootLocation + "\\" + filename);
    }

}
