package oxy.edhm.hackmud.requests;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static oxy.edhm.hackmud.HMAPI.*;

public class CHATS implements Runnable{
    protected int iter;

    public CHATS() {
        iter = 0;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void run() {
        Logger.info("CHATS active:");
        iter++;
        if (hmUser==null || token==null) {
            synchronized (this){
                Logger.warn("No user|token detected, calling and waiting on ACCOUNT_DATA..");
                request(Requests.ACCOUNT_DATA);
                try {
                    wait(2000);
                } catch (InterruptedException ignored) {}
            }
        }
        assert hmUser != null; assert token != null;
        Logger.info("User found: "+hmUser);
        String response = POST(hackmudURL+"chats.json",
                String.format("""
                {
                    "chat_token":"%s",
                    "usernames":["%s"],
                    "before": %s
                }""", token, hmUser, System.currentTimeMillis()/1000));

        List<Map> a = (List) ((Map) gson.fromJson(response, Map.class).get("chats")).get(hmUser);
        chats = new ArrayList<>();
        chats.addAll(a);
        if (chats.size()!=0) {
            Logger.info("Chats msgs number: " + chats.size());
            Logger.info("first message: " + chats.get(0));
        }

//                "id": "627e92edef354e571c88808a",
//                "t": 1652462317.062,
//                "from_user": "fr3shd4nk_m3m3s",
//                "msg": "dank",
//                "channel": "0000"
    }
}