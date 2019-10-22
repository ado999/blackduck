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

    public String storeImage(MultipartFile file, String fileExtension){
        File fileToSave = new File(rootLocation + "\\" + UUID.randomUUID().toString() + "." + fileExtension);
        try {
            Image image = new Image(file.getBytes());
            if(image.getWidth() > image.getHeight()){
                image.setWidth(1024);
            } else {
                image.setHeight(1024);
            }
            image.saveAs(fileToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileToSave.getName();
    }

    public String store(MultipartFile file, String fileExtension) {
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

    public File loadFile(String filename){
        return new File(rootLocation + "\\" + filename);
    }

}
