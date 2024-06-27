package appletransactionclient.exception;

import appletransactionclient.http.response.status.error.AppStoreErrorResponse;

public class AppStoreErrorResponseException extends Exception {

    AppStoreErrorResponse response;

    public AppStoreErrorResponseException(String message, AppStoreErrorResponse response) {
        super(message);
        this.response = response;
    }

    public AppStoreErrorResponseException(String message, Throwable cause, AppStoreErrorResponse response) {
        super(message, cause);
        this.response = response;
    }

    public AppStoreErrorResponseException(Throwable cause, AppStoreErrorResponse response) {
        super(cause);
        this.response = response;
    }

    public AppStoreErrorResponse getAppStoreErrorResponse() {
        return response;
    }

}
