package oxy.edhm;

import oxy.edhm.EliteDangerous.EDProfile;
import oxy.edhm.hackmud.HMAPI;
import oxy.edhm.hackmud.HMProfile;
import oxy.edhm.hackmud.requests.Requests;

public class EDHM {
    public static void main(String[] args) { //entrypoint
        RudimentaryUI ui = new RudimentaryUI();
        ui.start();
//        EDProfile edProfile = new EDProfile();
//        edProfile.startUpdating();
//        HMAPI.hmUser = "jinn";
//        System.out.println(HMProfile.getSysLoc());
    }
}
