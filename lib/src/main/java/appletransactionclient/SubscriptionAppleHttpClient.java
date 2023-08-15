package appletransactionclient;

import appletransactionclient.exception.AppStoreStatusResponseException;
import appletransactionclient.http.AppleHttpClient;
import appletransactionclient.http.response.status.AppStoreStatusResponse;
import appletransactionclient.http.response.status.error.AppStoreErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    public AppStoreStatusResponse getStatusResponseV1(Long transactionID, String jwt) throws IOException, InterruptedException, URISyntaxException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Try to do the request with baseURL and if not successful try again with sandboxURL
        try {
            // Get status response with the base url
            Object statusResponse = getStatusResponseV1(transactionID, baseURL, getSubscriptionStatusURLPath, jwt);

            // If statusResponse is AppStoreStatusResponse, return using cast
            if (statusResponse instanceof AppStoreStatusResponse)
                return (AppStoreStatusResponse)statusResponse;
        } catch (JsonMappingException | AppStoreStatusResponseException e) {
            // Just print the stack trace and proceed
            e.printStackTrace();
        }

        // Since the statusResponse was not successful using the baseURL, try again with sandboxURL
        Object statusResponse = getStatusResponseV1(transactionID, sandboxURL, getSubscriptionStatusURLPath, jwt);

        // If statusResponse is AppStoreStatusResponse, return using cast
        if (statusResponse instanceof AppStoreStatusResponse)
            return (AppStoreStatusResponse)statusResponse;

        // Otherwise, throw AppStoreStatusResponseException
        throw new AppStoreStatusResponseException("Error getting response from Prod and Sandbox Apple Server...\n" + statusResponse.toString());
    }

    private Object getStatusResponseV1(Long transactionID, String url, String path, String jwt) throws URISyntaxException, IOException, InterruptedException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
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
            if (appStoreStatusResponse == null)
                throw new AppStoreStatusResponseException("AppStoreStatusResponse was null");

            return appStoreStatusResponse;
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            // There was an issue processing the JSON, so try processing it as an error response and returning the error
            return new ObjectMapper().treeToValue(response, AppStoreErrorResponse.class);
        }

    }



}
