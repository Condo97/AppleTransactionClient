package appletransactionclient.http.response.status.error;

public class AppStoreErrorResponse {

    private String errorMessage;
    private Long errorCode;

    public AppStoreErrorResponse() {

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "AppStoreErrorResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }

}
