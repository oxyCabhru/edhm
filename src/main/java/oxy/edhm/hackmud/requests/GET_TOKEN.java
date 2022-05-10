package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;
import oxy.edhm.hackmud.APIHandler;

import java.util.Map;

import static oxy.edhm.hackmud.APIHandler.*;

public class GET_TOKEN implements Runnable{
    @SuppressWarnings("FieldMayBeFinal")
    private String body;

    public GET_TOKEN(String body) {
        this.body = body;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(){
        Logger.debug("GET_TOKEN active:");                            //    {
        String response = POST(hackmudURL+"get_token.json",body);//         "ok":true,
        Logger.debug("Response: "+response);                          //        "chat_token":"<token>"
        Map<String, Object> r = gson.fromJson(response, Map.class);   //    }
        APIHandler.token = (String) r.get("chat_token");
        Logger.info("HMtoken: "+APIHandler.token);
        }
}
