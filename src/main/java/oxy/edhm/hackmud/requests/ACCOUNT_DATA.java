package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;
import oxy.edhm.hackmud.HMAPI;

import java.util.ArrayList;
import java.util.Map;

import static oxy.edhm.hackmud.HMAPI.*;

public class ACCOUNT_DATA implements Runnable {
    protected int iter;

    public ACCOUNT_DATA() {
        this.iter = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Logger.info("ACCOUNT_DATA active:");
        iter++;
        if (token == null) {
            synchronized (this) {
                Logger.warn("Token was not found. Calling and waiting for GET_TOKEN to do its job..");
                request("GET_TOKEN");
                try {
                    wait(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        synchronized (this) {
            assert token != null;
            Logger.info("Token found: " + token);
            String response = POST(hackmudURL + "account_data.json",
                    String.format("""
                        {
                            "chat_token":"%s"
                        }""", token));
            Map<String, Object> usersAndChannels = (Map<String, Object>)
                    gson.fromJson(response, Map.class).get("users");
            for (String user : usersAndChannels.keySet()) {
                if (user.startsWith("cmdr")) {
                    HMAPI.hmUser = user;
                    HMAPI.channelsAndMembers = (Map<String, ArrayList<String>>) gson.fromJson
                            (usersAndChannels.get(user).toString(), Map.class);
                }
            }
            if (hmUser == null) {
                Logger.warn("No cmdr user found in the provided account.");
                return;
            }
            Logger.info("User: " + hmUser);
            Logger.info("channels: " + channelsAndMembers.keySet());
            Logger.info("ACCOUNT_DATA finished iteration: " + iter);
            notifyAll();
        }
    }
}