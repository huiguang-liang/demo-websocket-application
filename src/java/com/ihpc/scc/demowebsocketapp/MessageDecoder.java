package com.ihpc.scc.demowebsocketapp;

import java.io.StringReader;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * @author Huiguang Liang
 */
public class MessageDecoder implements Decoder.Text<Message> {
    
    // Get a Logger object
    private final static Logger LOGGER = Helper.getLogger(MessageDecoder.class.getName());

    /**
     * Test whether the input string can be successfully decoded
     * @param string The input string to be tested.
     * @return True if the input string can be successfully decoded
     */
    @Override
    public boolean willDecode(String string) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(string));
            jsonReader.read();
            return true;
        } catch (JsonException e) {
            LOGGER.info(e.toString());
            return false;
        }
    }
    
    /**
     * Decode the given input string into a Message object
     * @param string The input string to be decoded
     * @return The Message object wrapping the input string
     * @throws DecodeException 
     */
    @Override
    public Message decode(String string) throws DecodeException {
        JsonObject jsonObject = Json.createReader(new StringReader(string)).readObject();
        return new Message(jsonObject);
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
