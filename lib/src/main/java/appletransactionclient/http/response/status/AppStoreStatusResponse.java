package appletransactionclient.http.response.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppStoreStatusResponse {

    private String environment, bundleId;
    private Long appAppleId;
    private AppStoreStatusResponseSubscriptionGroupIdentifierItem[] data;

    public AppStoreStatusResponse() {

    }

    public String getEnvironment() {
        return environment;
    }

    public String getBundleId() {
        return bundleId;
    }

    public Long getAppAppleId() {
        return appAppleId;
    }

    public AppStoreStatusResponseSubscriptionGroupIdentifierItem[] getData() {
        return data;
    }

}
