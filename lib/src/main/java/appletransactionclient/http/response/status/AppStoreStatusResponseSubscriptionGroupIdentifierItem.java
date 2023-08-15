package appletransactionclient.http.response.status;

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
