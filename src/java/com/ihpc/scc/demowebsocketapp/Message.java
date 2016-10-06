package com.ihpc.scc.demowebsocketapp;

import java.io.StringWriter;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 * @author Huiguang Liang
 */
public class Message {
    
    // Get a Logger object
    private final static Logger LOGGER = Helper.getLogger(Message.class.getName());
    
    // The internal JSON Object that this message contains
    private JsonObject jsonObject;
    
    /**
     * Constructor
     * @param jsonObject The JSON Object to be wrapped
     */
    public Message(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    
    /**
     * Return the JSON Object
     * @return the internal JSON Object wrapped by this class
     */
    public JsonObject getJsonObject() {
        return jsonObject;
    }
    
    /**
     * Set the JSON Object
     * @param jsonObject The JSON object to be wrapped
     */
    public void setJson(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Get the string representation of the internal JSON Object
     * @return String representation of the wrapped JSON Object
     */
    @Override
    public String toString(){
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(jsonObject);
        
        LOGGER.info(Helper.stringConcat("JSON Object is ", writer.toString()));
        return writer.toString();
    }
}
