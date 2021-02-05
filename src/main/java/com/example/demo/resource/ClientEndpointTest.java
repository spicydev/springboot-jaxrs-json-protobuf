package com.example.demo.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@ClientEndpoint
public class ClientEndpointTest {

    private final static Logger logger = LoggerFactory.getLogger(ClientEndpointTest.class);

    private MessageHandler messageHandler;
    private Session userSession=null;

    public ClientEndpointTest(String endpoint)  {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8080/hello"));
        } catch (Exception r) {
            throw new RuntimeException(r);
        }
    }


    @OnOpen
    public void myClientOpen(Session session) {
        userSession = session;
        logger.info("Websocket-Client opened on ID: {} and SSL: {}",session.getId(), session.isSecure());
    }

    public void sendMessage(String message) throws IOException {
        userSession.getBasicRemote().sendBinary(ByteBuffer.wrap(StandardCharsets.UTF_8.encode(message).array()));
    }
}
