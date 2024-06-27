package appletransactionclient;

import appletransactionclient.http.response.status.SignedRenewalInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SubscriptionJWSDecoder {

    public static SignedRenewalInfo decodeSignedRenewalInfo(String jwsToken) throws IOException {
        return new ObjectMapper().treeToValue(JWSDecoder.decodeJWSPayload(jwsToken), SignedRenewalInfo.class);
    }

}
