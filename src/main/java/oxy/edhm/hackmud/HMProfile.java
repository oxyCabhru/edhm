package oxy.edhm.hackmud;

public class HMProfile {
    protected HTTPHandler httpHandler;

    public HMProfile(String token) {
        httpHandler = new HTTPHandler(token);
    }


}
