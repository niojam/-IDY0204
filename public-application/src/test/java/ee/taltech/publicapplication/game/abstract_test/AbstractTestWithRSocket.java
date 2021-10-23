package ee.taltech.publicapplication.game.abstract_test;

import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import static ee.taltech.publicapplication.game.utils.TestUtils.PLAYER_TOKEN_WITHOUT_BEARER;

public class AbstractTestWithRSocket extends AbstractTestWithRepository {

    @Autowired
    private RSocketRequester.Builder builder;

    public RSocketRequester createRSocketRequester() {
        MimeType authMimeType = MimeTypeUtils.parseMimeType(
                "message/x.rsocket.authentication.bearer.v0");
        return builder.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .setupMetadata(PLAYER_TOKEN_WITHOUT_BEARER, authMimeType)
                .connect(WebsocketClientTransport.create(8016)).block();
    }

}
