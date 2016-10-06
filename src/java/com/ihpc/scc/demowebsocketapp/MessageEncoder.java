package com.ihpc.scc.demowebsocketapp;

import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author Huiguang Liang
 */
public class MessageEncoder implements Encoder.Text {
    
    // Get a Logger object
    private final static Logger LOGGER = Helper.getLogger(MessageEncoder.class.getName());
    
    /**
     * Encodes a given Message object into a string that is ready to be sent over a websocket
     * @param message The Message object to be encoded
     * @return The string representation of the message
     * @throws EncodeException 
     */
    @Override
    public String encode(Object message) throws EncodeException {
        if (!(message instanceof Message)) {
            LOGGER.info("Input object is not a object of Message class.");
            throw new EncodeException(message, "Input object is not a object of Message class.");
        }
        return ((Message)message).getJsonObject().toString();
    }
    
    /**
     * Simple overrides that are required when implementing Decoder.Text<T>
     * @param config 
     */
    @Override
    public void init(EndpointConfig config) {
        LOGGER.info("Initialize.");
    }
    @Override
    public void destroy() {
        LOGGER.info("Destroy.");
    }
}
