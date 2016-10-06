package com.ihpc.scc.demowebsocketapp;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Huiguang Liang
 */
public class Helper {
    
    /**
     * A useful function to get a logger and configure it generically
     * @param className Name of the calling class
     * @return The created logger
     */
    public static final Logger getLogger(String className) {
        // Get the logger associated with the className
        Logger logger = Logger.getLogger(className);
        
        // Declare the handler to print to the system console
        //ConsoleHandler consoleHandler = new ConsoleHandler();
        //consoleHandler.setFormatter(new SimpleFormatter());
        //consoleHandler.setLevel(Level.ALL);
        
        // Add handler to logger
        //logger.addHandler(consoleHandler);
        
        // Finally, return the created logger
        return logger;
    }
    
    /**
     * A useful String concatenation function.
     * @param strings Input strings
     * @return The concatenated string
     */
    public static final String stringConcat(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (String string : strings) {
                stringBuilder.append(string);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return new String();
        }
    }
    
    /**
     * Wrap a given string in a HTML span element
     * @param id
     * @param string
     * @return String wrapped in a span element
     */
    public static String wrapInSpan(String id, String string) {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("<span id=\"")
            .append(id)
            .append("\">")
            .append(string)
            .append("</span>");
        
        return stringBuilder.toString();
    }
}
