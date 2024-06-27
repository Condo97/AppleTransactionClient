package appletransactionclient;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class SubscriptionStatusJWTGenerator {

    public static String generateJWT(JWTSigner signer, String issuerID, String bundleID) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        return signer.signJWT(Map.of(
                "iss", issuerID,
                "iat", System.currentTimeMillis() / 1000l,
                "exp", System.currentTimeMillis() / 1000l + 80,
                "aud", "appstoreconnect-v1",
                "bid", bundleID
        ));
    }

}
