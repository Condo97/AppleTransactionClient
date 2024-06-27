package appletransactionclient.http.response.status;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignedRenewalInfo {

    @JsonProperty("originalTransactionId")
    private String originalTransactionID;

    @JsonProperty("productId")
    private String productID;

    @JsonProperty("recentSubscriptionStartDate")
    private String recentSubscriptionStartDate;

    public SignedRenewalInfo() {

    }

    public SignedRenewalInfo(String originalTransactionID, String productID, String recentSubscriptionStartDate) {
        this.originalTransactionID = originalTransactionID;
        this.productID = productID;
        this.recentSubscriptionStartDate = recentSubscriptionStartDate;
    }

    public String getOriginalTransactionID() {
        return originalTransactionID;
    }

    public String getProductID() {
        return productID;
    }

    public String getRecentSubscriptionStartDate() {
        return recentSubscriptionStartDate;
    }

}


//"originalTransactionId": "1420000004101993",
//        "autoRenewProductId": "chitchatultraunlimitedyearly",
//        "productId": "chitchatultraunlimitedyearly",
//        "autoRenewStatus": 1,
//        "renewalPrice": 79990,
//        "currency": "USD",
//        "signedDate": 1719496440885,
//        "environment": "Production",
//        "recentSubscriptionStartDate": 1710366755000,
//        "renewalDate": 1749765155000