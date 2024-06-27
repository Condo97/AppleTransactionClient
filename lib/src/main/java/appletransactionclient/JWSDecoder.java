package appletransactionclient;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;

public class JWSDecoder {

    public static JsonNode decodeJWSPayload(String token) throws IOException {
        DecodedJWT decodedJWT = JWT.decode(token);

        byte[] decodedPayloadBytes = Base64.getDecoder().decode(decodedJWT.getPayload());
        String decodedPayload = new String(decodedPayloadBytes);

        return new ObjectMapper().readValue(decodedPayload, JsonNode.class);
    }

}
