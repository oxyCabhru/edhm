package oxy.edhm.EliteDangerous;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;

public class PJReader {
    protected static Gson gson = new Gson();

    public static File getLatestJournal() {
        File logFolder = Paths.get
                (System.getProperty("user.home")+"/Saved Games/Frontier Developments/Elite Dangerous")
                .toFile();
        try {
            File currentLog = null;
            File[] logs = logFolder.listFiles //get files from folder
                    ((dir, name) -> name.toLowerCase().endsWith(".log")); //get only .log files

            if (logs==null) throw new FileNotFoundException();
            for (File file: logs) { //get the last modified log, default to first found if there is none to compare to
                if (currentLog==null || file.lastModified() > currentLog.lastModified()) {
                    currentLog = file;
                }
            }
            return currentLog;
        } catch (FileNotFoundException e) {
            System.out.println("no files in log directory.");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Map<String, Object>> formatJournal(File latestLog) { //organize journal file to a Map
        ArrayList<Map<String, Object>> readLog = new ArrayList<>();
        try {
            Scanner log = new Scanner(latestLog); //read the log
            while (log.hasNextLine()) {
                readLog.add(gson.fromJson(log.nextLine(), Map.class)); //each row is a json object, map it and add to array
            }
        } catch (Exception e) {
            System.out.println("error transforming log into PJournal:"); //honestly I don't know what could go wrong
            e.printStackTrace();
        }
        return readLog; //return array of events from the log
    }
    public static ArrayList<Map<String, Object>> getLog() { //chain above methods together:
        return formatJournal(getLatestJournal());
    }

    public static ArrayList<Map<String, Object>> searchForAllEventsInLog(ArrayList<Map<String, Object>> log, String event) {
        ArrayList<Map<String, Object>> output = new ArrayList<>();
        for (Map<String, Object> map: log) {
            if (map.get("event").equals(event)) { //look through the formatted log and find ALL relevant events
                 output.add(map);                 //could probably make this into a stream
            }
        }
        return output; //log array -> log array with only specific event type
    }

    public static Map<String, Object> getLatestEventInEventArray(ArrayList<Map<String, Object>> log) {
        Map<String, Object> currentMap = null;
        for (Map<String, Object> map: log) {
            if (currentMap==null || //compare timestamps between each event, get the latest one + null safety (is that the term lol)
                    compareTimestamps((String) map.get("timestamp"), (String) currentMap.get("timestamp")))
                currentMap = map;
        }
        return currentMap;
    }

    public static boolean compareTimestamps(String timestampA, String timestampB) {
        return getTimeFromTimestamp(timestampA)
                .compareTo(getTimeFromTimestamp(timestampB))
                > 0;
    }

    //timestamp = 2022-05-07T12:28:10Z
    public static Calendar getTimeFromTimestamp(String timestamp) {
        String[] TS = timestamp.split("[^0-9]"); // 2022, 05, 07, 12, 28, 10, []
        return new Calendar.Builder()                  //   0    1   2   3   4   5   6
                .setDate(Integer.parseInt(TS[0]), Integer.parseInt(TS[1]), Integer.parseInt(TS[2]))
                .setTimeOfDay(Integer.parseInt(TS[3]), Integer.parseInt(TS[4]), Integer.parseInt(TS[5]))
                .build(); //im sure that this is redundant somehow but oh well
    }

    public static Map<String, Object> getLatestEventFromLatestLog(String event) {
        return PJReader.getLatestEventInEventArray(
                PJReader.searchForAllEventsInLog(
                        PJReader.getLog(), event)); //another helper method to chain them
    }
}

