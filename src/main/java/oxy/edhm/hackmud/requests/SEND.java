package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;

import static oxy.edhm.hackmud.HMAPI.*;

public class SEND implements Runnable{
    private final String channelOrTarget; private final String message;

    public SEND(String channelOrTarget, String message) {
        this.channelOrTarget = channelOrTarget; this.message = message;
    }

    @Override
    public void run(){
            Logger.info("SEND active:");
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
                    "channel":"%s",
                    "msg":"%s"
                }""", token, hmUser, channelOrTarget, message));
            Logger.info("SEND response: "+response);
        }
}
