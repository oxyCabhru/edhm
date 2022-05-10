package oxy.edhm.hackmud;

import java.util.ArrayList;
import java.util.Map;

public class HMProfile {
    protected APIHandler api;

    public static String hmUser;
    public static ArrayList<String> channels;
    public static ArrayList<Map<String, String>> chats;
    public static long balance;

    public HMProfile() {
        api = new APIHandler();
    }




}
