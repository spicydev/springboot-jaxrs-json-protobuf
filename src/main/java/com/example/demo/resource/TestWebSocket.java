package com.example.demo.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@ServerEndpoint("/hello")
public class TestWebSocket {

    private final static Logger logger = LoggerFactory.getLogger(TestWebSocket.class);

    @OnOpen
    public void myOnOpen (Session session) {
        logger.info("Websocket opened on ID: {} and SSL: {}",session.getId(), session.isSecure());

    }

    @OnMessage
    public void myOnMessage(ByteBuffer buffer, Session session) {
        logger.info("Websocket Received data: {}", StandardCharsets.UTF_8.decode(buffer));
    }

}
