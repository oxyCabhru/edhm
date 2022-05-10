package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;

import static oxy.edhm.hackmud.APIHandler.*;

public class SEND implements Runnable{
    @SuppressWarnings("FieldMayBeFinal")
    private String body;

    public SEND(String body) {
        this.body = body;
    }

    @Override
    public void run(){
            Logger.debug("SEND active:");
            String response = POST(hackmudURL+"create_chat.json", body);
            Logger.debug("SEND response: "+response);
        }
}
