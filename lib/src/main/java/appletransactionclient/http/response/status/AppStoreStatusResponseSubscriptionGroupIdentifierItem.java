package appletransactionclient.http.response.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppStoreStatusResponseSubscriptionGroupIdentifierItem {

    private String subscriptionGroupIdentifier;
    private AppStoreStatusResponseLastTransactionItem[] lastTransactions;

    public AppStoreStatusResponseSubscriptionGroupIdentifierItem() {

    }

    public String getSubscriptionGroupIdentifier() {
        return subscriptionGroupIdentifier;
    }

    public AppStoreStatusResponseLastTransactionItem[] getLastTransactions() {
        return lastTransactions;
    }

}
