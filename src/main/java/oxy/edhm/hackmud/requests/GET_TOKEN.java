package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static oxy.edhm.hackmud.HMAPI.*;

public class GET_TOKEN implements Runnable{
    @SuppressWarnings("FieldMayBeFinal")
    private String body;

    public GET_TOKEN(String body) {
        this.body = body;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(){
        Logger.info("GET_TOKEN active:");                            //    {
synchronized (this) {
    String response = POST(hackmudURL + "get_token.json", body);//         "ok":true,
    Logger.info("Response: " + response);                          //        "chat_token":"<token>"
    Map<String, Object> r = gson.fromJson(response, Map.class);   //    }
    token = (String) r.get("chat_token");
    Logger.info("Token get: " + token);
    notifyAll();
}
        try {
            Logger.info("Saving token to disk..");
            FileWriter tokenSave = new FileWriter("token.txt");
            tokenSave.write(token);
            tokenSave.close();
            Logger.info("Token "+token+" saved to disk.");
        } catch (IOException e) {
            Logger.warn("Couldn't save token to disk:");
            Logger.warn(e.getMessage());
        }
    Logger.info("GET_TOKEN: Job's done!");
    }
}
