package oxy.edhm.handlers;

import java.net.http.HttpClient;

public class HTTPHandler {
    protected HttpClient httpClient;
    public HTTPHandler() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }
}
