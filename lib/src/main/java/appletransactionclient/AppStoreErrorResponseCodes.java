package appletransactionclient;

public enum AppStoreErrorResponseCodes {

    INVALID_TRANSACTION_ID(4040010L);


    private Long errorCode;

    AppStoreErrorResponseCodes(Long errorCode) {
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }

}
