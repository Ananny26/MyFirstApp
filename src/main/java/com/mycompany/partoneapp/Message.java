/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.partoneapp;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ananny
 */

/**
 * Handles one message at a time.
 */
public class Message {
    public static final String CELL_OK = "Cell phone number successfully captured.";
    public static final String CELL_BAD = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    public static final String READY = "Message ready to send.";
    public static final String SENT = "Message successfully sent.";
    public static final String DISREGARDED = "Press 0 to delete the message.";
    public static final String STORED = "Message successfully stored.";
    public static final String INVALID = "Invalid choice.";

    // shared by all messages
    private static Random random = new Random();
    private static int totalMessages = 0;

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // builds a ten digit ID using string manipulation: a random number is
    // padded with leading zeros to ten characters, then cut to length 10
    public static String generateMessageID() {
        long number = (long) (random.nextDouble() * 10000000000L);
        String id = String.format("%010d", number);
        return id.substring(0, 10);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        login checker = new login("", "", recipient, "", "");
        if (checker.checkCellPhoneNumber()) {
            return CELL_OK;
        }
        return CELL_BAD;
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return READY;
        }
        int over = messageText.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    public String createMessageHash() {
        String trimmed = messageText.trim();
        int firstSpace = trimmed.indexOf(' ');
        String firstWord = firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace);
        int lastSpace = trimmed.lastIndexOf(' ');
        String lastWord = lastSpace == -1 ? trimmed : trimmed.substring(lastSpace + 1);
        firstWord = firstWord.replaceAll("[^A-Za-z0-9]", "");
        lastWord = lastWord.replaceAll("[^A-Za-z0-9]", "");
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // 1 = send, 2 = disregard, 3 = store; anything else is invalid.
    // Each choice records the message in the matching array.
    public String sentMessage(int choice) {
        if (choice == 1) {
            totalMessages++;
            MessageManager.add(this, MessageManager.SENT);
            return SENT;
        } else if (choice == 2) {
            MessageManager.add(this, MessageManager.DISREGARDED);
            return DISREGARDED;
        } else if (choice == 3) {
            storeMessage();
            return STORED;
        }
        return INVALID;
    }

    // adds this message to the stored array and saves all stored messages as JSON
    public void storeMessage() {
        MessageManager.add(this, MessageManager.STORED);
        MessageManager.saveStoredToJson();
    }

    // details of every message sent while the program is running
    public static String printMessages() {
        return MessageManager.report(MessageManager.SENT);
    }

    public String details() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    public static int returnTotalMessages() { return totalMessages; }
    public static int returnStoredCount() { return MessageManager.getStoredMessages().size(); }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }

    public static void resetAll() {
        totalMessages = 0;
        MessageManager.reset();
    }
}