package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;
import oxy.edhm.hackmud.APIHandler;

import java.util.ArrayList;
import java.util.Map;

import static oxy.edhm.hackmud.APIHandler.*;

public class CHATS implements Runnable{
    @SuppressWarnings("FieldMayBeFinal")
    private String body;

    public CHATS(String body) {
        this.body = body;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Logger.debug("CHATS active:");
        String response = POST(hackmudURL+"chats.json", body);
        Logger.debug("Chats: "+response);
        APIHandler.chats = (ArrayList<Map<String, String>>) gson.fromJson(response, Map.class);
    }
}

