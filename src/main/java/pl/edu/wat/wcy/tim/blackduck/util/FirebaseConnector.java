package pl.edu.wat.wcy.tim.blackduck.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.tim.blackduck.responses.FirebaseMessageResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

@Component
public class FirebaseConnector {

    private final String SERVER_KEY = "key=AAAAQjKyLPU:APA91bG5-iFrgSt48-m1-QEnilrFCTqnv-CMibT1FaVJTk3N1UCnOOKjiLZidx0KYkNMbU5kNVcBspX1kPFndMmGhw6DYWrDedXBAz3zR0htBhXPXIfUjnh-R7sN1a6zREC3K4MKD5Vm";

    public void sendNotification(FirebaseMessageResponse messageResponse){
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("dd.MM.yyyy, HH:mm:ss");
        String json = gb.create().toJson(messageResponse);
        System.out.print(json);
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", SERVER_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            byte[] input = json.getBytes();
            os.write(input, 0, input.length);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String responseLine;
            while((responseLine = br.readLine()) != null){
                sb.append(responseLine.trim());
            }

        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

}
