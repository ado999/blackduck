package pl.edu.wat.wcy.tim.blackduck.util;

import javaxt.io.Image;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FrameGrabber {

    public String saveThumbnail(File loadFile) {

        try {
            Picture picture = FrameGrab.getFrameFromFile(loadFile, 10);
            BufferedImage image = AWTUtil.toBufferedImage(picture);
            String filename = UUID.randomUUID().toString() + ".jpg";
            File savedFile = new File(loadFile.getParent() + "\\" + filename);
            Image img = new Image(image);
            img.rotate(90);
            img.saveAs(savedFile);
            return filename;
        } catch (IOException | JCodecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
