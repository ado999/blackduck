package pl.edu.wat.wcy.tim.blackduck.util;

import java.io.File;

public class Utils {

    public static void createUploadDir(){
        String path = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");

        new File(path+separator+"upload-dir").mkdirs();
    }

    public static String getExtension(String filename){

        String fileExtension;
        try {
            fileExtension = filename.split("\\.")[1].toLowerCase();
        } catch (Exception e){
            throw new IllegalArgumentException("Couldn't recognize media type");
        }

        switch (fileExtension) {
            case "jpg":
                return "jpg";
            case "png":
                return "png";
            case "mov":
                return "mov";
            case "mp4":
                return "mp4";
            default:
                throw new IllegalArgumentException("Media type is not supported");
        }
    }

}
