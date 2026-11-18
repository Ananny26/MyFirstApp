/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.partoneapp;

/**
 *
 * @author anann
 */
public class MessageManager {
    package com.mycompany.partoneapp;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Holds the arrays for Part 3: sent, disregarded and stored messages plus the
 * parallel arrays (ID, hash, recipient, text, flag) used for searching.
 */
public class MessageManager {
    public static final String SENT = "Sent";
    public static final String DISREGARDED = "Disregarded";
    public static final String STORED = "Stored";
    public static final String JSON_FILE = "storedMessages.json";
    public static final String ID_NOT_FOUND = "Message ID not found.";
    public static final String HASH_NOT_FOUND = "Message hash not found.";
    public static final String NO_STORED = "No stored messages.";

    // one array per message type
    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    private static ArrayList<String> storedMessages = new ArrayList<>();

    // parallel arrays: index i in each describes the same sent/stored message
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageRecipients = new ArrayList<>();
    private static ArrayList<String> messageTexts = new ArrayList<>();
    private static ArrayList<String> messageFlags = new ArrayList<>();

    private static String sender = "";

    // matches one JSON object written by saveStoredToJson (string values with escapes)
    private static final String JSON_STRING = "\"((?:[^\"\\\\]|\\\\.)*)\"";
    private static final Pattern JSON_OBJECT = Pattern.compile(
            "\"messageID\"\\s*:\\s*" + JSON_STRING
            + "\\s*,\\s*\"messageHash\"\\s*:\\s*" + JSON_STRING
            + "\\s*,\\s*\"recipient\"\\s*:\\s*" + JSON_STRING
            + "\\s*,\\s*\"message\"\\s*:\\s*" + JSON_STRING, Pattern.DOTALL);

    public static void setSender(String name) { sender = name; }

    public static void add(Message message, String flag) {
        addRaw(message.getMessageID(), message.getMessageHash(), message.getRecipient(),
                message.getMessageText(), flag);
    }

    private static void addRaw(String id, String hash, String recipient, String text, String flag) {
        if (flag.equals(DISREGARDED)) {
            disregardedMessages.add(text);
            return;
        }
        messageIDs.add(id);
        messageHashes.add(hash);
        messageRecipients.add(recipient);
        messageTexts.add(text);
        messageFlags.add(flag);
        arrayFor(flag).add(text);
    }

    private static ArrayList<String> arrayFor(String flag) {
        if (flag.equals(SENT)) return sentMessages;
        if (flag.equals(STORED)) return storedMessages;
        return disregardedMessages;
    }

    // a. sender and recipient of every stored message
    public static String displaySendersAndRecipients() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < messageFlags.size(); i++) {
            if (messageFlags.get(i).equals(STORED)) {
                out.append("Sender: ").append(sender)
                   .append(" | Recipient: ").append(messageRecipients.get(i)).append("\n");
            }
        }
        return out.length() == 0 ? NO_STORED : out.toString();
    }

    // b. longest stored message
    public static String longestStoredMessage() {
        if (storedMessages.isEmpty()) return NO_STORED;
        String longest = storedMessages.get(0);
        for (String text : storedMessages) {
            if (text.length() > longest.length()) longest = text;
        }
        return longest;
    }

    // c. search by message ID
    public static String searchByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? ID_NOT_FOUND : messageTexts.get(index);
    }

    public static String recipientByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? ID_NOT_FOUND : messageRecipients.get(index);
    }

    // d. every sent or stored message for one recipient
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (int i = 0; i < messageRecipients.size(); i++) {
            if (messageRecipients.get(i).equals(recipient)) found.add(messageTexts.get(i));
        }
        return found;
    }

    // e. delete a message using its hash (removes it from every array)
    public static String deleteByHash(String hash) {
        int index = messageHashes.indexOf(hash);
        if (index == -1) return HASH_NOT_FOUND;
        String text = messageTexts.get(index);
        String flag = messageFlags.get(index);
        arrayFor(flag).remove(text);
        messageIDs.remove(index);
        messageHashes.remove(index);
        messageRecipients.remove(index);
        messageTexts.remove(index);
        messageFlags.remove(index);
        if (flag.equals(STORED)) saveStoredToJson();
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // f. report with the full details of all sent or stored messages
    public static String report(String flag) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < messageFlags.size(); i++) {
            if (messageFlags.get(i).equals(flag)) {
                out.append("Message ID: ").append(messageIDs.get(i))
                   .append("\nMessage Hash: ").append(messageHashes.get(i))
                   .append("\nRecipient: ").append(messageRecipients.get(i))
                   .append("\nMessage: ").append(messageTexts.get(i)).append("\n\n");
            }
        }
        return out.length() == 0 ? "No " + flag.toLowerCase() + " messages." : out.toString();
    }

    // writes every stored message to the JSON file.
    // The layout follows the JSON specification at https://www.json.org
    public static void saveStoredToJson() {
        StringBuilder json = new StringBuilder("[\n");
        boolean first = true;
        for (int i = 0; i < messageFlags.size(); i++) {
            if (!messageFlags.get(i).equals(STORED)) continue;
            if (!first) json.append(",\n");
            first = false;
            json.append("  {\n")
                .append("    \"messageID\": \"").append(escape(messageIDs.get(i))).append("\",\n")
                .append("    \"messageHash\": \"").append(escape(messageHashes.get(i))).append("\",\n")
                .append("    \"recipient\": \"").append(escape(messageRecipients.get(i))).append("\",\n")
                .append("    \"message\": \"").append(escape(messageTexts.get(i))).append("\"\n")
                .append("  }");
        }
        json.append("\n]\n");
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not save messages: " + e.getMessage());
        }
    }

    // reads the JSON file back into the stored array; returns how many new messages were added
    public static int loadStoredMessages() {
        int added = 0;
        if (!Files.exists(Paths.get(JSON_FILE))) return added;
        try {
            String json = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
            Matcher matcher = JSON_OBJECT.matcher(json);
            while (matcher.find()) {
                String id = unescape(matcher.group(1));
                if (!messageIDs.contains(id)) {
                    addRaw(id, unescape(matcher.group(2)), unescape(matcher.group(3)),
                            unescape(matcher.group(4)), STORED);
                    added++;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read messages: " + e.getMessage());
        }
        return added;
    }

    // escapes characters that are not allowed raw inside a JSON string
    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private static String unescape(String s) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);
                if (next == 'n') out.append('\n');
                else if (next == 'r') out.append('\r');
                else if (next == 't') out.append('\t');
                else out.append(next);
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    public static ArrayList<String> getSentMessages() { return sentMessages; }
    public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<String> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }

    // clears the arrays (the JSON file is left alone)
    public static void reset() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageIDs.clear();
        messageHashes.clear();
        messageRecipients.clear();
        messageTexts.clear();
        messageFlags.clear();
        sender = "";
    }
}
    
}
