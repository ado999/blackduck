package pl.edu.wat.wcy.tim.blackduck.util;

import java.nio.charset.Charset;
import java.util.Random;

public class RandomString {
    public static String generateUUID (){
        byte[] array = new byte[64]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}
