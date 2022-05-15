package oxy.edhm.hackmud;

import org.pmw.tinylog.Logger;
import oxy.edhm.hackmud.requests.ACCOUNT_DATA;
import oxy.edhm.hackmud.requests.Requests;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;

import static oxy.edhm.hackmud.HMAPI.*;

public class HMProfile {
    protected HMAPI api;
    protected static File shellTXT = Paths.get
                    (System.getProperty("user.home")+"/AppData/Roaming/hackmud/shell.txt")
                    .toFile();

    public HMProfile() {
        api = new HMAPI();

    }


    //read from api
    public static String getHMUser() {
        return hmUser;
    }
    
    public static Map<String, ArrayList<String>> getChannelsAndMembers() {
        return channelsAndMembers;
    }
    
    public static ArrayList<Map> getChats() {
        return chats;
    }

    //read from shell.txt
    public static long getBalance() {
        //Balance: 31B415M926K535GC == 31,415,926,535 == 31415926535
        for (String line: readShellTXT()) { //go from top to bottom
            if (line.matches("Balance: ((\\d*)([A-Z]*))*")) {
                return Long.parseLong(line.replaceAll("[^\\d]", ""));
            }
        }
        return -1;
    }

    public static String getSysLoc() {
        if (hmUser==null) {
                Logger.warn("No user detected, executing ACCOUNT_DATA");
                HMAPI.request(Requests.ACCOUNT_DATA);
            while (hmUser == null) {
                Thread.onSpinWait();
            }
        }
        String previous = null;
        for (String line: readShellTXT()) {
            if (previous == null) previous = line;
            if (line.matches(">>sys.loc")) { //look for user call for sys.loc
                if (previous.startsWith(hmUser)) return previous; //and return the next line in chat
            } else {
                previous = line;
            }
        }
        return null;
    }

    public static Deque<String> readShellTXT() {
        Deque<String> read = new ArrayDeque<>();
        try {
            Scanner shell = new Scanner(shellTXT);
            while (shell.hasNextLine()) {
                read.addFirst(shell.nextLine().replaceAll("<color=#\\w{8}>|</color>", "")); //I hope this works
            } //    }<color=#101215FF>LOCK_UNLOCKED</color> == }LOCK_UNLOCKED
        } catch (FileNotFoundException e) {
            Logger.warn("No shell.txt file found.");
        }
        return read;
    }
}
