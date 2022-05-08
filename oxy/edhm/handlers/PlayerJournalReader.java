package oxy.edhm.handlers;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;

public class PlayerJournalReader {
    protected static Gson gson;

    public static File getLatestJournalFile() { //get latest journal file
        File logFolder = Paths.get(System.getProperty("user.home")+"Saved Games/Frontier Developments/Elite Dangerous").toFile();
        try {
            File currentLog = null;
            for (File file : logFolder.listFiles()) {
                if (file.getName().matches("/.*\\\\.log$/gm")) { //if it's a log file
                    if (file.lastModified() > currentLog.lastModified() || currentLog==null) {
                        currentLog = file;
                    }
                } //im tired
            }
            return currentLog;
        } catch (Exception e) {
            System.out.println("no files in log directory.");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Map> getPJournal(File latestLog) { //organize journal file to a Map
        ArrayList<Map> readLog = new ArrayList<>();
        try { //organize file into array that holds the rows as json -> maps
            Scanner log = new Scanner(latestLog);
            while (log.hasNextLine()) {
                readLog.add(gson.fromJson(log.nextLine(), Map.class));
            }
        } catch (Exception e) {
            System.out.println("error transforming log into PJournal:");
            e.printStackTrace();
        }
        return readLog;
    }

    public static ArrayList<Map> getLog() { //chain above methods together:
        return getPJournal(getLatestJournalFile());
    }

    public static Map searchForEventInLog(ArrayList<Map> log, String event) {
        for (Map map: log) {
            if (map.get("event").equals(event)) {
                return map;
            }
        }
        return null;
    }
}

