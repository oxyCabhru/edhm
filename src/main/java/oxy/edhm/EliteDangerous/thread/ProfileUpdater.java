package oxy.edhm.EliteDangerous.thread;

import org.pmw.tinylog.Logger;
import oxy.edhm.EliteDangerous.PJReader;
import static oxy.edhm.EliteDangerous.EDProfile.*;

import java.util.ArrayList;
import java.util.Map;

public class ProfileUpdater implements Runnable{

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Logger.info("Profile Updater thread started..");

        while(!Thread.currentThread().isInterrupted()) {
            //get info from PJReader: (LoadGame)
            Map<String, Object> logLoadGame = PJReader.getLatestEventFromLatestLog("LoadGame");
            cmdrName = (String) logLoadGame.get("Commander"); //oxyJinn
            balance =  ((Double) logLoadGame.get("Credits")).longValue();  //46477292

            //get info from PJReader: (FSDJump)
            Map<String, Object> logFSDJump = PJReader.getLatestEventFromLatestLog("FSDJump");
            systemAddress = ((Double) logFSDJump.get("SystemAddress")).longValue();
            starPos = (ArrayList<Float>) logFSDJump.get("StarPos");

            //get info from PJReader: (ShieldState)
            Map<String, Object> logShieldState = PJReader.getLatestEventFromLatestLog("ShieldState");
            shieldState = (Boolean) logShieldState.get("ShieldsUp");

            Logger.info(cmdrName+": "+balance);
            Logger.info(systemAddress+": "+starPos);
            Logger.info(shieldState);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Logger.warn("Profile Updater thread interrupted, exiting..");
                return;
            }
        }
    }
}
