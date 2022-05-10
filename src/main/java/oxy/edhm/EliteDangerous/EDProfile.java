package oxy.edhm.EliteDangerous;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.Map;

public class EDProfile {
    protected String cmdrName; protected long balance;
    protected long systemAddress; protected ArrayList<Float> starPos;
    protected boolean shieldState;

    @SuppressWarnings("unchecked")
    protected Thread profileUpdater = new
            Thread(() -> {
                Logger.info("Profile Updater thread started..");

                while(!Thread.currentThread().isInterrupted()) { //be VERY careful here
                    //get info from PJReader: (LoadGame)
                    Map<String, Object> logLoadGame = PJReader.getLatestEventFromLatestLog("LoadGame");
                    this.cmdrName = (String) logLoadGame.get("Commander"); //oxyJinn
                    this.balance =  ((Double) logLoadGame.get("Credits")).longValue();  //46477292

                    Map<String, Object> logFSDJump = PJReader.getLatestEventFromLatestLog("FSDJump");
                    this.systemAddress = ((Double) logFSDJump.get("SystemAddress")).longValue();
                    this.starPos = (ArrayList<Float>) logFSDJump.get("StarPos");

                    Map<String, Object> logShieldState = PJReader.getLatestEventFromLatestLog("ShieldState");
                    this.shieldState = (Boolean) logShieldState.get("ShieldsUp");

                    Logger.info(cmdrName+": "+balance);
                    Logger.info(systemAddress);
                    Logger.info(shieldState);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) { //TODO: thread pools! [https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/]
//                        System.out.println(e);
                        Logger.warn("Profile Updater thread interrupted, exiting..");
                        return;
                    }
                }

            });
    public EDProfile() {
        Logger.debug("Starting EDProfile thread!");
        profileUpdater.start();
    }
    public void stopUpdating() {
        Logger.debug("Stopping EDProfile thread.");
        profileUpdater.interrupt();
    }
}
