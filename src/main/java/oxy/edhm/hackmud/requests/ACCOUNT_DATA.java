package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;
import oxy.edhm.hackmud.APIHandler;

import java.util.ArrayList;
import java.util.Map;

import static oxy.edhm.hackmud.APIHandler.*;

public class ACCOUNT_DATA implements Runnable {
    @SuppressWarnings("FieldMayBeFinal")
    private String body;

    public ACCOUNT_DATA(String body) {
        this.body = body;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Logger.info("ACCOUNT_DATA active:");
        String response = POST(hackmudURL + "account_data.json", body);
        Logger.info("Account data: " + response);
        Map<String, String> r = gson.fromJson(response, Map.class);
        //cracks fingers
        //we need to find a user that starts with "cmdr" and return it and its joined channels:
        Map<String, String> users = gson.fromJson(r.get("users"), Map.class);
        for (String user : users.keySet()) {
            if (user.startsWith("oxy")) {
                APIHandler.hmUser = user; //gets first user that starts with cmdr. yeah fuck you
                APIHandler.channels = gson.fromJson(users.get(hmUser), ArrayList.class); //get joined channels for user
            }
        }
    }
}