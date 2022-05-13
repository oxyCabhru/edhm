package oxy.edhm.hackmud;

import okhttp3.*;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import oxy.edhm.hackmud.requests.*;

public class HMAPI {
    public static final OkHttpClient client = new OkHttpClient();                           // http handler
    public static final Gson gson = new Gson();                                             // json handler
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");  // helper vars for http
    public static final String hackmudURL = "https://www.hackmud.com/mobile/";              //

    public static volatile String token; public static volatile String hmUser; public static volatile String password; //
    public static volatile Map<String, ArrayList<String>> channelsAndMembers;                                               // variables for use with our threads
    public static volatile ArrayList<Map<String, String>> chats;                                     // this is where we hold info
    protected static ScheduledThreadPoolExecutor profileUpdater = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);                                   //manage our tasks with this

    public HMAPI() {
        Logger.info("Starting APIHandler, starting services:");
        request("ACCOUNT_DATA");
//        request("CHATS");
    }

    public static void request(String request) {
        switch (request) {
            case "GET_TOKEN" -> profileUpdater.execute(GET_TOKEN(password));
            case "ACCOUNT_DATA" -> {
                Logger.info("Starting ACCOUNT_DATA service");
                profileUpdater.scheduleWithFixedDelay(ACCOUNT_DATA(token), 0, 10, TimeUnit.SECONDS);
            }
            case "CHATS" -> {
                Logger.info("Starting CHATS service");
                profileUpdater.scheduleWithFixedDelay(CHATS(token, hmUser), 0, 5, TimeUnit.SECONDS);
            }
            default -> Logger.warn("Invalid request to API, did you use SEND or TELL? Add channel|target and a message.");
        }
    }
    public static void request(String sendOrTell, String channelOrTarget, String message) {
        switch (sendOrTell) {
            case "SEND":
                profileUpdater.execute(SEND(token, hmUser, channelOrTarget, message));
            case "TELL":
                profileUpdater.execute(TELL(token, hmUser, channelOrTarget, message));
            default:
                Logger.warn("Invalid request to API");
        }
    }

    public void stopService() {
        profileUpdater.shutdown();
    }

    public static Runnable GET_TOKEN(String password) {
        String body = String.format("""
                {
                    "pass":"%s"
                }""", password);
        return new GET_TOKEN(body);
    }

    public static Runnable ACCOUNT_DATA(String token) {
        String body = String.format("""
                {
                    "chat_token":"%s"
                }""", token);
        return new ACCOUNT_DATA(body);
    }

    public static Runnable CHATS(String token, String username) {
        String body = String.format("""
            {
                "chat_token":"%s",
                "usernames":"%s",
                "before": %s
            }""", token, username, System.currentTimeMillis());
        return new CHATS(body);
    }

    public static Runnable SEND(String token, String username, String channel, String message) {
        String body = String.format("""
                {
                    "chat_token":"%s",
                    "username":"%s",
                    "channel":"%s",
                    "msg":"%s"
                }""", token, username, channel, message);
        return new SEND(body);
    }

    public static Runnable TELL(String token, String username, String target, String message) {
        String body = String.format("""
                {
                    "chat_token":"%s",
                    "username":"%s",
                    "tell":"%s",
                    "msg":"%s"
                }""", token, username, target, message);
        return new TELL(body);
    }

    @SuppressWarnings("ConstantConditions")
    public static String POST(String URL, String json) { //working!! do not TOUCH
        RequestBody body = RequestBody.create(json, JSON); //craft POST body
        Logger.info("Body: " + body);
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build(); //package POST request
        Logger.info("[Thread "+Thread.currentThread().getId()+"] Request: " + request);
        while(true) {
            try (Response response = client.newCall(request).execute()) { //try execute
                return response.body().string();
            } catch (IOException e) {
                Logger.warn("Connectivity problem or timeout, unsure if other side got the request:");
                Logger.warn(e.getMessage());
                Logger.warn("Trying again in 5..");
            }
        }
    }
}
