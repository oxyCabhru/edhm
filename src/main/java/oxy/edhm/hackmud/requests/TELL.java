package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;

import static oxy.edhm.hackmud.APIHandler.*;

public class TELL implements Runnable {
    @SuppressWarnings("FieldMayBeFinal")
    private String body;

    public TELL(String body) {
        this.body = body;
    }

    @Override
    public void run(){
            Logger.debug("TELL active:");
            String response = POST(hackmudURL+"create_chat.json", body);
            Logger.debug("Tell response: "+response);
        }
    }
