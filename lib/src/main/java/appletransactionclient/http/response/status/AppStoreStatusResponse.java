package appletransactionclient.http.response.status;

public class AppStoreStatusResponse {

    private String environment, bundleId;
    private Integer appAppleId;
    private AppStoreStatusResponseSubscriptionGroupIdentifierItem[] data;

    public AppStoreStatusResponse() {

    }

    public String getEnvironment() {
        return environment;
    }

    public String getBundleId() {
        return bundleId;
    }

    public Integer getAppAppleId() {
        return appAppleId;
    }

    public AppStoreStatusResponseSubscriptionGroupIdentifierItem[] getData() {
        return data;
    }

}
