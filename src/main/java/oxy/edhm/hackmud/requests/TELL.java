package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;

import static oxy.edhm.hackmud.HMAPI.*;

public class TELL implements Runnable {
    private final String channelOrTarget; private final String message;

    public TELL(String channelOrTarget, String message) {
        this.channelOrTarget = channelOrTarget; this.message = message;
    }

    @Override
    public void run(){
            Logger.info("TELL active:");
        if (hmUser==null || token==null) {
            synchronized (this){
                Logger.warn("No user|token detected, calling and waiting on ACCOUNT_DATA..");
                request("ACCOUNT_DATA");
                try {
                    wait(2000);
                } catch (InterruptedException ignored) {}
            }
        }
        assert hmUser != null; assert token != null;
        Logger.info("User found: "+hmUser);
            String response = POST(hackmudURL+"create_chat.json", String.format("""
                {
                    "chat_token":"%s",
                    "username":"%s",
                    "tell":"%s",
                    "msg":"%s"
                }""", token, hmUser, channelOrTarget, message));
            Logger.info("Tell response: "+response);
        }
    }
