package com.ihpc.scc.demowebsocketapp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/** 
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 */
@ServerEndpoint(value="/echo", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class}) 
public class EchoServer {
    
    // Get a Logger object
    private final static Logger LOGGER = Helper.getLogger(EchoServer.class.getName());
    
    // Maintain a set of sessions for reference
    private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet());
    
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     * @param session Represents the session that is opened
     */
    @OnOpen
    public void onOpen(Session session){
        LOGGER.info(Helper.stringConcat(session.getId(), " has opened a connection."));
        SESSIONS.add(session);
        
        LOGGER.info(Helper.stringConcat("Open Sessions: ", String.valueOf(SESSIONS.size())));
        // Alert everyone that a new session has been created
        Message message = new Message(Json.createObjectBuilder()
            .add("type", "text")
            .add("data", Helper.stringConcat("User ", session.getId(), " has connected."))
            .add("sender", "server")
            .add("received_time", System.currentTimeMillis())
            .build());
        sendMessageToAll(message);
    }
    
    /**
     * This method will intercept the message sent by the client to the server
     * @param message Message sent by the client
     * @param session Session which the data was sent on
     */
    @OnMessage
    public void onMessage(Message message, Session session) {
        
        // Sanity check
        if (message == null || session == null) {
            return;
        }
        
        // Continue if everything checks out
        LOGGER.info(Helper.stringConcat("Received ", message.toString(), " from ", session.getId()));

        // Lets see what was sent
        JsonObject jsonObject = message.getJsonObject();
        
        // Prepare the response
        JsonObject returnObject = jsonObject;
        JsonObjectBuilder returnObjectBuilder = Json.createObjectBuilder();

        // Since JsonObject is immutable, we have to do a hard-copy of content
        returnObject.entrySet().stream().forEach((entry) -> {
            returnObjectBuilder.add(entry.getKey(), entry.getValue());
        });
        
        // Then set the sender and received time by the server
        returnObjectBuilder.add("sender", session.getId());
        returnObjectBuilder.add("received_time", System.currentTimeMillis());
        
        // Build the return response
        returnObject = returnObjectBuilder.build();
        
        // Send the return response
        sendMessageToAll(returnObject);
        
        //returnObject.put("sender", session.getId());
        /*
        try {
            String type = jsonObject.getString("type");
            if (type.equalsIgnoreCase("text")) {
                returnObject = Json.createObjectBuilder()
                        .add("type", "text")
                        .add("data", Helper.stringConcat("User ", session.getId(), " sent ", "\"", Helper.wrapInSpan("quote",jsonObject.getString("data")) ,"\""))
                        .build();
            }
            if (type.equalsIgnoreCase("image")) {
                //returnObject = jsonObject;
            }
            
            Message newMessage = new Message(returnObject);
            sendMessageToAll(newMessage);
        }
        catch (NullPointerException | ClassCastException e) {
            LOGGER.info(e.toString());
        }
        */
    }
    
    /**
     * Intercept the closing of a session.
     * @param session session to be closed
     */
    @OnClose
    public void onClose(Session session){
        
        SESSIONS.remove(session);
        LOGGER.info(Helper.stringConcat("Session ", session.getId(), " has ended."));
        LOGGER.info(Helper.stringConcat("Open Sessions: ", String.valueOf(SESSIONS.size())));
        
        // Alert everyone that a session has ended
        Message message = new Message(Json.createObjectBuilder()
            .add("type", "text")
            .add("data", Helper.stringConcat("User ", session.getId(), " has disconnected."))
            .add("sender", "server")
            .add("received_time", System.currentTimeMillis())
            .build());
        sendMessageToAll(message);
    }
    
    /**
     * Set a message to all sessions
     * @param message 
     */
    private void sendMessageToAll(Message message) {
        
        // Sanity check
        if (message == null) {
            LOGGER.info("Trying to send a null message!");
            return;
        }
        
        for(Session session : SESSIONS){
            try {
                LOGGER.info(Helper.stringConcat("Trying to send message ", message.toString(), " to ", session.getId()));
                
                // Get a remote endpoint
                RemoteEndpoint.Basic remoteEndpoint = session.getBasicRemote();
                
                // Sanity check
                if (remoteEndpoint == null) {
                    LOGGER.info(Helper.stringConcat("Could not get remote endpoint for ", session.getId()));
                    continue;
                }
                
                // Send the message only if remoteEndpoint is not null
                remoteEndpoint.sendText(message.toString());
                LOGGER.info("Sent message successfully!");
            } catch (Exception ex) {
                LOGGER.warning(ex.toString());
                //ex.printStackTrace();
            }
        }
    }
    
    private void sendMessageToAll(JsonObject jsonObject) {
        
        // Sanity check
        if (jsonObject == null) {
            LOGGER.info("Trying to send a null object!");
            return;
        }
        
        // Wrap the jsonObject and send it
        Message message = new Message(jsonObject);
        sendMessageToAll(message);
    }
}
