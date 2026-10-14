/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.partoneapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
/**
 *
 * @author ananny
 */


 /* a single QuickChat message, plus the sent and stored message lists.
 */
public class Message {

    // Messages kept in one place so the app and the unit tests always agree
    public static final String CELL_OK = "Cell phone number successfully captured.";
    public static final String CELL_BAD = "Cell phone number is incorrectly formatted or does not contain "
            + "an international code. Please correct the number and try again.";
    public static final String READY = "Message ready to send.";
    public static final String SENT = "Message successfully sent.";
    public static final String DISREGARDED = "Press 0 to delete the message.";
    public static final String STORED = "Message successfully stored.";

    private static final Random RANDOM = new Random();
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static String jsonFilePath = "messages.json";

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Randomly generated ten-digit message ID
    public static String generateMessageID() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(RANDOM.nextInt(10));
        }
        return id.toString();
    }

    // The message ID must be no more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Reuses the cell phone check from Part 1 (login.checkCellPhoneNumber)
    public String checkRecipientCell() {
        login cellChecker = new login("", "", recipient, "", "");
        if (cellChecker.checkCellPhoneNumber()) {
            return CELL_OK;
        }
        return CELL_BAD;
    }

    // A message may not be longer than 250 characters
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return READY;
        }
        int over = messageText.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    // Hash = first two digits of the ID : message number : FIRSTWORDLASTWORD (e.g. 00:0:HITONIGHT)
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String prefix = messageID.substring(0, Math.min(2, messageID.length()));
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // 1 = send, 2 = disregard, 3 = store to send later
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                return SENT;
            case 2:
                return DISREGARDED;
            case 3:
                storeMessage();
                return STORED;
            default:
                return "Invalid option; please choose 1, 2 or 3.";
        }
    }

    // Full details of this message, in the order the brief asks for
    public String details() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Returns all the messages sent while the program is running
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder all = new StringBuilder();
        for (Message m : sentMessages) {
            all.append(m.details()).append("\n\n");
        }
        return all.toString().trim();
    }

    // Returns the total number of messages sent
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Writes every stored message to a JSON file
    public void storeMessage() {
        storedMessages.add(this);
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(escape(m.messageID)).append("\",\n");
            json.append("    \"messageHash\": \"").append(escape(m.messageHash)).append("\",\n");
            json.append("    \"recipient\": \"").append(escape(m.recipient)).append("\",\n");
            json.append("    \"message\": \"").append(escape(m.messageText)).append("\"\n");
            json.append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]\n");
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not save to " + jsonFilePath + ": " + e.getMessage());
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }

    // Used by the unit tests
    public static void setJsonFilePath(String path) { jsonFilePath = path; }
    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
    }
}
