package appletransactionclient;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class JWTSigner {

    private String jwsPath, privateKeyID;

    public JWTSigner(String jwsPath, String privateKeyID) {
        this.jwsPath = jwsPath;
        this.privateKeyID = privateKeyID;
    }

    public String signJWT(String payload) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ECPrivateKey privateKey = getPrivateKey();

        Algorithm algorithm = Algorithm.ECDSA256(null, privateKey);
        String jwt = JWT.create()
                .withPayload(payload)
                .withHeader(Map.of(
                        "alg", "ES256",
                        "kid", privateKeyID,
                        "typ", "JWT"
                ))
                .sign(algorithm);

        return jwt;
    }

    public String signJWT(Map<String, ?> payload) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeySpecException {
        ECPrivateKey privateKey = getPrivateKey();

        Algorithm algorithm = Algorithm.ECDSA256(null, privateKey);
        String jwt = JWT.create()
                .withPayload(payload)
                .withHeader(Map.of(
                        "alg", "ES256",
                        "kid", privateKeyID,
                        "typ", "JWT"
                ))
                .sign(algorithm);

        return jwt;
    }

    private ECPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] p8Key = Files.readAllBytes(Paths.get(jwsPath));

        // Remove begin and end private key header and footer
        String p8KeyStringRemovedHeaderFooterText = removeBeginEndPrivateKeyText(new String(p8Key));
        byte[] p8KeyRemovedHeaderFooterText = p8KeyStringRemovedHeaderFooterText.getBytes();

        PKCS8EncodedKeySpec p8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(p8KeyRemovedHeaderFooterText));

        return (ECPrivateKey)KeyFactory.getInstance("EC").generatePrivate(p8EncodedKeySpec);
    }


    private static String removeBeginEndPrivateKeyText(String decodedKey) {
        char headerFooterStartDelimiter = '-';
        String beginPrivateKeyText = "BEGIN PRIVATE KEY";
        String endPrivateKeyText = "END PRIVATE KEY";
        String newLineDelimiter = "\n";

        // Add private key lines to output, skipping beginPrivateKeyText and endPrivateKeyText
        StringBuilder output = new StringBuilder();
        String[] keySplitByLine = decodedKey.split(newLineDelimiter);
        for (String line: keySplitByLine) {
            // Ensure that the line is part of the key otherwise skip it
            boolean shouldSkipLine = false;

            // Check if first char in the line is the headerFooterStartDelimiter
            if (line.charAt(0) == headerFooterStartDelimiter) {
                // Check if the line contains either beginPrivateKeyText or endPrivateKeyText and if so set shouldSkipLine to true TODO: This may be slow
                if (line.contains(beginPrivateKeyText) || line.contains(endPrivateKeyText))
                    shouldSkipLine = true;
            }

            // If not should skip line, add the line with newLineDelimiter to output
            if (!shouldSkipLine) {
                output.append(line);
            }
        }

        // If the last characters in output are a new line delimiter, remove them
        if (output.substring(output.length() - newLineDelimiter.length(), output.length()).equals(newLineDelimiter))
            output = output.delete(output.length() - newLineDelimiter.length(), output.length());

        return output.toString();
    }

}
