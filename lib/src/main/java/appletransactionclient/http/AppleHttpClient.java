package appletransactionclient.http;

import java.net.http.HttpClient;
import java.time.Duration;

public class AppleHttpClient {

    private static final Integer APPLE_CONNECTION_TIMEOUT = 5;

    private static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(APPLE_CONNECTION_TIMEOUT)).build();

    public static HttpClient getClient() {
        return client;
    }


}
