package oxy.edhm;

import com.google.gson.Gson;
import org.pmw.tinylog.Logger;
import oxy.edhm.EliteDangerous.EDProfile;
import oxy.edhm.hackmud.APIHandler;

import java.util.Map;

public class EDHM {
    public static void main(String[] args) { //entrypoint
//        Logger.info("Starting EDProfile thread:");
//        EDProfile edProfile = new EDProfile();

//        Logger.info("Starting HMProfile thread:");
        APIHandler api = new APIHandler();
        api.request("ACCOUNT_DATA");

    }
}
