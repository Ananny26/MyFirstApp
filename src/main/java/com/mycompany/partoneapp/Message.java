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
 * @author anann
 */


public class Message {
    public static final String CELL_OK = "Cell phone number successfully captured.";
    public static final String CELL_BAD = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    public static final String READY = "Message ready to send.";
    public static final String SENT = "Message successfully sent.";
    public static final String DISREGARDED = "Press 0 to delete the message.";
    public static final String STORED = "Message successfully stored.";

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

    public static String generateMessageID() {
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }
        return id;
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

    public String sentMessage(int choice) {
        if (choice == 1) {
            totalMessages++;
            return SENT;
        } else if (choice == 2) {
            return DISREGARDED;
        } else {
            return STORED;
        }
    }

    public String details() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public static void resetAll() { totalMessages = 0; }
}