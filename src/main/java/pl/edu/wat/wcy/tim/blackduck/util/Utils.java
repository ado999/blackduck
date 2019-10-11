package pl.edu.wat.wcy.tim.blackduck.util;

import java.io.File;

public class Utils {

    public static void createUploadDir(){
        String path = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");

        new File(path+separator+"upload-dir").mkdirs();
    }

}
