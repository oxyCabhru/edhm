package oxy.edhm.EliteDangerous;

import org.pmw.tinylog.Logger;
import oxy.edhm.EliteDangerous.thread.ProfileUpdater;

import java.util.ArrayList;

public class EDProfile {
    public static volatile String cmdrName; public static volatile long balance;
    public static volatile long systemAddress; public static volatile ArrayList<Float> starPos;
    public static volatile boolean shieldState;

    protected Thread profileUpdater = new Thread(new ProfileUpdater());


    public void startUpdating() {
        Logger.debug("Starting EDProfile thread!");
        profileUpdater.start();
    }

    public void stopUpdating() {
        Logger.debug("Stopping EDProfile thread.");
        profileUpdater.interrupt();
    }
}
