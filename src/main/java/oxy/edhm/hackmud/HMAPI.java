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

    public static volatile String token; public static volatile String hmUser; public static volatile String password;                           //
    public static volatile Map<String, ArrayList<String>> channelsAndMembers;// variables for use with our threads
    @SuppressWarnings("rawtypes")
    public static volatile ArrayList<Map> chats;                                                                               // this is where we hold info
    protected static ScheduledThreadPoolExecutor profileUpdater = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5); //manage our tasks with this

    public static void request(String request) {
        switch (request) {
            case "GET_TOKEN":
                profileUpdater.execute(new GET_TOKEN());
                break;

            case "ACCOUNT_DATA":
                Logger.info("Starting ACCOUNT_DATA service");
                profileUpdater.scheduleWithFixedDelay(new ACCOUNT_DATA(), 0, 10, TimeUnit.SECONDS);
                break;
            case "CHATS":
                Logger.info("Starting CHATS service");
                profileUpdater.scheduleWithFixedDelay(new CHATS(), 0, 10, TimeUnit.SECONDS);
                break;
            default:
                Logger.warn("Invalid/Unhandled request to API, did you use SEND or TELL? Add channel|target and a message.");
        }
    }
    public static void request(String sendOrTell, String channelOrTarget, String message) {
        switch (sendOrTell) {
            case "SEND":
                profileUpdater.execute(new SEND(channelOrTarget, message));
                break;
            case "TELL":
                profileUpdater.execute(new TELL(channelOrTarget, message));
                break;
            default:
                Logger.warn("Invalid/Unhandled request to API");
        }
    }

    public void stopService() {
        profileUpdater.shutdown();
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
