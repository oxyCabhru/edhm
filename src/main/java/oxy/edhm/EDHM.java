package oxy.edhm;

import oxy.edhm.hackmud.HMAPI;


public class EDHM {
    public static void main(String[] args) { //entrypoint
//        Logger.info("Starting EDProfile thread:");
//        EDProfile edProfile = new EDProfile();

//        Logger.info("Starting HMProfile thread:");
        HMAPI.token = "<>";
//        System.out.println(System.currentTimeMillis()/1000);

//        HMAPI.request("ACCOUNT_DATA");
//        HMAPI.request("CHATS");
        HMAPI.request("SEND", "0000", "test");
    }
}
