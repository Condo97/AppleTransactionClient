package appletransactionclient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;

public class SubscriptionOfferPacker {

    public static class PackedSubscriptionOffer {

        private String appBundleID, keyID, nonce, signature;
        long timestamp;

        public PackedSubscriptionOffer(String appBundleID, String keyID, String nonce, String signature, long timestamp) {
            this.appBundleID = appBundleID;
            this.keyID = keyID;
            this.nonce = nonce;
            this.signature = signature;
            this.timestamp = timestamp;
        }

        public String getAppBundleID() {
            return appBundleID;
        }

        public String getKeyID() {
            return keyID;
        }

        public String getNonce() {
            return nonce;
        }

        public String getSignature() {
            return signature;
        }

        public long getTimestamp() {
            return timestamp;
        }

    }

    public PackedSubscriptionOffer pack(JWTSigner signer, String appBundleID, String keyID, String productID, String offerID, String applicationUsernameOrAppAccountToken) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Generate nonce and timestamp
        String nonce = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis() / 1000l;

        // Create payloadString in the following format -> appBundleId + '\u2063' + keyIdentifier + '\u2063' + productIdentifier + '\u2063' + offerIdentifier + '\u2063' + appAccountToken + '\u2063' + nonce + '\u2063' + timestamp
        String INVISIBLE_SEPARATOR = "\u2063";
        String payloadString = appBundleID
                + INVISIBLE_SEPARATOR
                + keyID
                + INVISIBLE_SEPARATOR
                + productID
                + INVISIBLE_SEPARATOR
                + offerID
                + INVISIBLE_SEPARATOR
                + applicationUsernameOrAppAccountToken
                + INVISIBLE_SEPARATOR
                + nonce
                + INVISIBLE_SEPARATOR
                + timestamp;

        // Generate signature by signing JWT
        String signature = signer.signJWT(payloadString);

        // Return in PackedSubscriptionOffer
        return new PackedSubscriptionOffer(
                appBundleID,
                keyID,
                nonce,
                signature,
                timestamp
        );
    }

}
