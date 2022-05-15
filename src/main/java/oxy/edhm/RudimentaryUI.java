package oxy.edhm;

import org.pmw.tinylog.Logger;
import oxy.edhm.EliteDangerous.EDProfile;
import oxy.edhm.hackmud.HMAPI;
import oxy.edhm.hackmud.HMProfile;
import oxy.edhm.hackmud.requests.Requests;

import java.util.Scanner;

public class RudimentaryUI {
    protected Scanner scanner = new Scanner(System.in);
    protected EDProfile edProfile = new EDProfile();

    public void start() {
        while(true) {
            System.out.println("> ");
            String[] userInput = scanner.nextLine().split(" ");
            switch (userInput[0]) {
                case "stop": {
                    HMAPI.stopServices();
                    edProfile.stopUpdating();
                    return;
                }
                case "request":
                    if (userInput.length>2) {
                        HMAPI.request(Requests.valueOf(userInput[1]), userInput[2], userInput[3]);
                    } else {
                        HMAPI.request(Requests.valueOf(userInput[1]));
                    }
                    break;
                case "set":
                    switch (userInput[1]) {
                        case "password":
                            HMAPI.password = userInput[2];
                            Logger.info("Password set: "+userInput[2]);
                            break;
                        case "token":
                            HMAPI.token = userInput[2];
                            Logger.info("Token set: "+userInput[2]);
                            break;
                        default:
                            Logger.warn("?");
                            break;
                    } break;
                case "EDProfile":
                    switch (userInput[1]) {
                        case "start":
                            edProfile.startUpdating();
                            break;
                        case "stop":
                            edProfile.stopUpdating();
                            break;
                        default:
                            Logger.warn("?");
                            break;
                    } break;
                case "get":
                    switch (userInput[1]) {
                        case "sysloc":
                            Logger.info("Sys.loc: " + HMProfile.getSysLoc());
                            break;
                        case "balance":
                            Logger.info("Balance: " + HMProfile.getBalance());
                            break;
                        default:
                            Logger.warn("?");
                            break;
                    }
                    break;
                default:
                    Logger.warn("?");
                    break;
            }
        }
    }
}
