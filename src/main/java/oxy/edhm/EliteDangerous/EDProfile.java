package oxy.edhm.EliteDangerous;

import org.pmw.tinylog.Logger;
import oxy.edhm.EliteDangerous.thread.ProfileUpdater;

import java.util.ArrayList;
import java.util.Map;

public class EDProfile {
    public static String cmdrName; public static long balance;
    public static long systemAddress; public static ArrayList<Float> starPos;
    public static boolean shieldState;

    protected Thread profileUpdater = new Thread(new ProfileUpdater());

    public EDProfile() {
        Logger.debug("Starting EDProfile thread!");
        profileUpdater.start();
    }
    public void stopUpdating() {
        Logger.debug("Stopping EDProfile thread.");
        profileUpdater.interrupt();
    }
}
