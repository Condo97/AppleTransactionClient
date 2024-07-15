package appletransactionclient.http.response.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppStoreStatusResponseLastTransactionItem {

    private String signedRenewalInfo, signedTransactionInfo;
    private Long originalTransactionId;
    private Integer status;

    public AppStoreStatusResponseLastTransactionItem() {

    }

    public String getSignedRenewalInfo() {
        return signedRenewalInfo;
    }

    public String getSignedTransactionInfo() {
        return signedTransactionInfo;
    }

    public Long getOriginalTransactionId() {
        return originalTransactionId;
    }

    public Integer getStatus() {
        return status;
    }

}
