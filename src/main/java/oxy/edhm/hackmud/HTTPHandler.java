package oxy.edhm.hackmud;

import okhttp3.*;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;

public class HTTPHandler {
    public static final OkHttpClient client = new OkHttpClient();
    public static final Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String hackmudURL = "www.hackmud.com/mobile/";
    volatile String token; volatile String hmUser; volatile ArrayList<String> channels; volatile ArrayList<Map<String, String>> chats;

    public HTTPHandler(String token) {
        httpClient.start();
    }
    protected Thread httpClient = new Thread(() -> {
        Logger.info("HTTP client started!");
        while (!Thread.currentThread().isInterrupted()) {
            while (!Thread.currentThread().isInterrupted()) {
            //run requests
            //what do i do here;
            // i honestly do not know
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Logger.warn("HTTP client interrupted, exiting");
                System.out.println(e.getMessage());
                return;
            }
        }
    });
    public void stopClient() {
        Logger.info("Stopping HTTPHandler..");
        httpClient.interrupt();
    }

    @SuppressWarnings("unchecked")
    public String GET_TOKEN(String password) {
        String body = String.format("""
                {
                    "pass":"%s"
                }""", password);
        Thread t = new Thread(() -> {
            Logger.debug("GET_TOKEN active:");                            //    {
            String response = POST(hackmudURL+"get_token.json",body);//         "ok":true,
            Logger.debug("Response: "+response);                          //        "chat_token":"<token>"
            Map<String, Object> r = gson.fromJson(response, Map.class);   //    }
            this.token = (String) r.get("chat_token");
        });
        t.start();
        return this.token;
    }

    @SuppressWarnings("unchecked")
    public void ACCOUNT_DATA(String token) {
        String body = String.format("""
                {
                    "chat_token":"%s"
                }""", token);
        Thread t = new Thread(() -> {
            Logger.debug("ACCOUNT_DATA active:");
            String response = POST(hackmudURL+"account_data.json",body);
            Logger.debug("Account data: "+response);
            Map<String, String> r = gson.fromJson(response, Map.class);
            //cracks fingers
            //we need to find a user that starts with "cmdr" and return it and its joined channels:
            Map<String, String> users = gson.fromJson(r.get("users"), Map.class);
            for (String user: users.keySet()) {
                if (user.startsWith("cmdr")){
                    this.hmUser = user; //gets first user that starts with cmdr. yeah fuck you
                    this.channels = gson.fromJson(users.get(hmUser), ArrayList.class); //get joined channels for user
                } //i hope this works lmao
            }
        });
        t.start();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, String>> CHATS(String token, String username) {
        String body = String.format("""
            {
                "chat_token":"%s",
                "usernames":"%s",
                "before": %s
            }""", token, username, System.currentTimeMillis());
        Thread t = new Thread(() -> {
            Logger.debug("CHATS active:");
            String response = POST(hackmudURL+"chats.json", body);
            Logger.debug("Chats: "+response);
            this.chats = (ArrayList<Map<String, String>>) gson.fromJson(response, Map.class);
        });
        t.start();
        return this.chats;
    }

    public void SEND(String token, String username, String channel, String message) {
        String body = String.format("""
                {
                    "chat_token":"%s",
                    "username":"%s",
                    "channel":"%s",
                    "msg":"%s"
                }""", token, username, channel, message);
        Thread t = new Thread(() -> {
            Logger.debug("SEND active:");
            String response = POST(hackmudURL+"create_chat.json", body);
            Logger.debug("SEND response: "+response);
        });
        t.start();
    }

    public void TELL(String token, String username, String target, String message) {
        String body = String.format("""
                {
                    "chat_token":"%s",
                    "username":"%s",
                    "tell":"%s",
                    "msg":"%s"
                }""", token, username, target, message);
        Thread t = new Thread(() -> {
            Logger.debug("TELL active:");
            String response = POST(hackmudURL+"create_chat.json", body);
            Logger.debug("Tell response: "+response);
        });
        t.start();
    }

    @SuppressWarnings("ConstantConditions")
    public String POST(String URL, String json) {
        RequestBody body = RequestBody.create(json, JSON); //craft POST body
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build(); //package POST request
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
                Logger.warn("Connectivity problem or timeout, unsure if other side got the request:");
                Logger.warn(e.getMessage());
                return null;
            } //execute
    }
}
