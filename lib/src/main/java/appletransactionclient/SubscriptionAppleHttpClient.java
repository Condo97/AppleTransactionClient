package appletransactionclient;

import appletransactionclient.exception.AppStoreErrorResponseException;
import appletransactionclient.http.AppleHttpClient;
import appletransactionclient.http.response.status.AppStoreStatusResponse;
import appletransactionclient.http.response.status.error.AppStoreErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import httpson.Httpson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class SubscriptionAppleHttpClient extends AppleHttpClient {

    private String baseURL, sandboxURL, getSubscriptionStatusURLPath;

    public SubscriptionAppleHttpClient(String baseURL, String sandboxURL, String getSubscriptionStatusURLPath) {
        this.baseURL = baseURL;
        this.sandboxURL = sandboxURL;
        this.getSubscriptionStatusURLPath = getSubscriptionStatusURLPath;
    }

    public AppStoreStatusResponse getStatusResponseV1(Long transactionID, String jwt) throws IOException, InterruptedException, URISyntaxException, AppStoreErrorResponseException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Try to do the request with baseURL and if not successful try again with sandboxURL
        try {
            // Get status response with the base url
            return getStatusResponseV1(transactionID, baseURL, getSubscriptionStatusURLPath, jwt);
        } catch (JsonProcessingException e) {
            // JSON parsing/mapping failed (e.g. Apple returned plaintext "Unauthenticated" on 401)
            System.out.println("Trying Sandbox, here's error information from trying with Prod:");
            e.printStackTrace();
        } catch (AppStoreErrorResponseException e) {
            // If the error code is not TRANSACTION_ID_NOT_FOUND throw e, otherwise proceed to try with sandbox
            if (e.getAppStoreErrorResponse().getErrorCode() == AppStoreErrorResponseCodes.INVALID_TRANSACTION_ID.getErrorCode()) {
                System.out.println("Error code: " + e.getAppStoreErrorResponse().getErrorCode());
                System.out.println("Doesn't match: " + AppStoreErrorResponseCodes.INVALID_TRANSACTION_ID.getErrorCode());
                throw e;
            }

            System.out.println("Trying Sandbox, here's error information from trying with Prod:");
            e.getAppStoreErrorResponse();
            e.printStackTrace();
        }

        // Since the statusResponse was not successful using the baseURL, try again with sandboxURL
        return getStatusResponseV1(transactionID, sandboxURL, getSubscriptionStatusURLPath, jwt);
    }

    private AppStoreStatusResponse getStatusResponseV1(Long transactionID, String url, String path, String jwt) throws URISyntaxException, IOException, InterruptedException, AppStoreErrorResponseException {
        // Get transaction id and create path parameter
        String transactionIDPathParameter = "/" + transactionID;

        // Create URI
        URI getStatusURI = new URI(url + path + transactionIDPathParameter);

        // Get JWT and create authorizationToken
//        String token = JWTGenerator.generateJWT(jwsPath, issuerID, bundleID, privateKeyID);
        String authorizationToken = "Bearer " + jwt;

        // Do the get request
        JsonNode response = Httpson.sendGET(AppleHttpClient.getClient(), getStatusURI, builder -> {
            builder.header("Authorization", authorizationToken);
        });

        try {
            // Get appStoreStatusResponse
            AppStoreStatusResponse appStoreStatusResponse = new ObjectMapper().treeToValue(response, AppStoreStatusResponse.class);

            // If appStoreReceipt is null, throw AppStoreStatusResponseException
            if (appStoreStatusResponse == null || appStoreStatusResponse.getData() == null)
                throw new AppStoreErrorResponseException("AppStoreStatusResponse or data were null", null);

            return appStoreStatusResponse;
        } catch (JsonProcessingException | AppStoreErrorResponseException e) {
            // Try to parse error response, and throw it in AppStoreStatusResponseException
            System.out.println(new ObjectMapper().writeValueAsString(response));
            // TODO: Handle errors if necessary
//            e.printStackTrace();

            try {
                // There was an issue processing the JSON, so try processing it as an error response and returning as an AppStoreErrorResponse
                throw new AppStoreErrorResponseException("Got error response...", new ObjectMapper().treeToValue(response, AppStoreErrorResponse.class));
            } catch (JsonProcessingException errorResponseE) {
                throw new AppStoreErrorResponseException("Error parsing AppStoreStatusResponse and AppStoreErrorResponse... " + new ObjectMapper().writeValueAsString(response), errorResponseE, null);
            }
        }

    }



}
