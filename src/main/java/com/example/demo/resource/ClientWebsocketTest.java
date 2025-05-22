package com.example.demo.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class ClientWebsocketTest implements AutoCloseable {

    private final static Logger logger = LoggerFactory.getLogger(ClientWebsocketTest.class);

    private String endpointUri;
    private Session userSession=null;

    public ClientWebsocketTest(String endpoint)  {
        this.endpointUri = endpoint;
    }

    public void connect() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(this.endpointUri));
        // Catch block from original constructor is not needed here as per instructions,
        // the method signature declares the exceptions to be handled by the caller.
    }

    @OnOpen
    public void myClientOpen(Session session) {
        userSession = session;
        logger.info("Websocket-Client opened on ID: {} and SSL: {}",session.getId(), session.isSecure());
    }

    public void sendMessage(String message) throws IOException {
        if (userSession != null && userSession.isOpen()) {
            userSession.getBasicRemote().sendText(message);
        } else {
            throw new IOException("WebSocket session is not open.");
        }
    }

    @Override
    public void close() throws IOException {
        if (userSession != null && userSession.isOpen()) {
            logger.info("Closing WebSocket session for ID: {}", userSession.getId());
            userSession.close();
        }
    }
}
