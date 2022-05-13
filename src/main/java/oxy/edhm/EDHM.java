package oxy.edhm;

import oxy.edhm.hackmud.HMAPI;


public class EDHM {
    public static void main(String[] args) { //entrypoint
//        Logger.info("Starting EDProfile thread:");
//        EDProfile edProfile = new EDProfile();

//        Logger.info("Starting HMProfile thread:");
        HMAPI.token = "<>";
        HMAPI api = new HMAPI();

//        APIHandler.request("GET_TOKEN");


    }
}
