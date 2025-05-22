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

    private Session userSession=null;

    public ClientWebsocketTest(String endpoint)  {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpoint));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            logger.error("Error connecting to WebSocket endpoint: {}", endpoint, e);
            throw new RuntimeException("Failed to connect to WebSocket: " + endpoint, e);
        }
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
