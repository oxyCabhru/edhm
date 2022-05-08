package oxy.edhm.logic;

import oxy.edhm.profile.Profile;
import oxy.edhm.handlers.PlayerJournalReader;

import java.util.ArrayList;
import java.util.Map;

public class Matchmake {


    public Profile matchmake() {
        ArrayList<Map> log = PlayerJournalReader.getLog();
        Long systemAddress = (Long) PlayerJournalReader.searchForEventInLog(log, "FSDJump").get("SystemAddress");//get current star system, FSDJump
return null;
    }
}
