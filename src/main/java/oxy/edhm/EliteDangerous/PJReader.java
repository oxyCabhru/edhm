package oxy.edhm.EliteDangerous;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

public class PJReader {
    protected static Gson gson = new Gson();

    @Nullable
    public static File getLatestJournal() { //get latest journal file
        File logFolder = Paths.get(System.getProperty("user.home")+"/Saved Games/Frontier Developments/Elite Dangerous").toFile();
        try {
            File currentLog = null;
            File[] logs = logFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".log"));

            for (File file : logs) {
                    if (currentLog==null || file.lastModified() > currentLog.lastModified()) {
                        currentLog = file;
                    }
                } //im tired
            return currentLog;
        } catch (Exception e) {
            System.out.println("no files in log directory.");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Map<String, Object>> formatJournal(File latestLog) { //organize journal file to a Map
        ArrayList<Map<String, Object>> readLog = new ArrayList<>();
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
    public static ArrayList<Map<String, Object>> getLog() { //chain above methods together:
        return formatJournal(getLatestJournal());
    }

    public static ArrayList<Map<String, Object>> searchForAllEventsInLog(ArrayList<Map<String, Object>> log, String event) {
        ArrayList<Map<String, Object>> output = new ArrayList<>();
        for (Map<String, Object> map: log) {
            if (map.get("event").equals(event)) {
                 output.add(map);
            }
        }
        return output;
    }

    public static Map<String, Object> getLatestEventInEventArray(ArrayList<Map<String, Object>> log) {
        Map<String, Object> currentMap = null;
        for (Map<String, Object> map: log) {
            if (currentMap==null || compareTimestamps((String) map.get("timestamp"), (String) currentMap.get("timestamp"))) { //TODO continue
                currentMap = map;
            }
        }
        return currentMap;
    }

    public static boolean compareTimestamps(String timestampA, String timestampB) {
        return getTimeFromTimestamp(timestampA).compareTo(getTimeFromTimestamp(timestampB)) > 0;
    }

    //timestamp = 2022-05-07T12:28:10Z
    public static Calendar getTimeFromTimestamp(String timestamp) {
        String[] TS = timestamp.split("[^0-9]"); // 2022, 05, 07, 12, 28, 10, []
        return new Calendar.Builder()                  //   0    1   2   3   4   5   6
                .setDate(Integer.parseInt(TS[0]), Integer.parseInt(TS[1]), Integer.parseInt(TS[2]))
                .setTimeOfDay(Integer.parseInt(TS[3]), Integer.parseInt(TS[4]), Integer.parseInt(TS[5]))
                .build();
    }

    public static Map<String, Object> getLatestEventFromLatestLog(String event) {
        return PJReader.getLatestEventInEventArray(
                PJReader.searchForAllEventsInLog(
                        PJReader.getLog(), event));
    }
}

