/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.partoneapp;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Message {
    public String messageID;
    public String recipient;
    public String messageText;
    public String messageHash;
    public int messageNumber;
    
    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static ArrayList<String> storedJson = new ArrayList<>();
    public static int totalMessages = 0;

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public Message() {}

    public String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        String s = String.valueOf(id);
        if (s.length() > 10) s = s.substring(0, 10);
        messageID = s;
        return s;
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell(String cell) {
        if (cell.matches("\\+27\\d{9,10}") && cell.length() <= 13) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    public String checkRecipientCell() {
        return checkRecipientCell(this.recipient);
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.split("\\s+");
        String first = words[0].toUpperCase().replaceAll("[^A-Z]", "");
        String last = words[words.length-1].toUpperCase().replaceAll("[^A-Z]", "");
        messageHash = firstTwo + ":" + messageNumber + ":" + first + last;
        return messageHash;
    }

    public String createMessageHash(String id, int num, String text) {
        messageID = id;
        messageNumber = num;
        messageText = text;
        return createMessageHash();
    }

    public String SentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(printMessagesSingle());
            totalMessages++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        }
        return "Invalid choice.";
    }

    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            return "Message exceeds 250 characters by " + (message.length()-250) + ", please reduce number.";
        }
    }

    public String printMessages() {
        if (sentMessages.size() == 0) return "No messages sent.";
        String all = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            all = all + sentMessages.get(i) + "\n------------------\n";
        }
        return all;
    }

    public String printMessagesSingle() {
        return "MessageID: " + messageID + "\nMessageHash: " + messageHash + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    public int returnTotalMessagess() {
        return totalMessages;
    }

    public void storeMessage() {
        String json = "{\"MessageID\":\"" + messageID + "\",\"MessageHash\":\"" + messageHash + "\",\"Recipient\":\"" + recipient + "\",\"Message\":\"" + messageText + "\"}";
        storedJson.add(json);
        try {
            FileWriter w = new FileWriter("storedMessages.json");
            w.write("[\n");
            for (int i = 0; i < storedJson.size(); i++) {
                w.write(storedJson.get(i));
                if (i < storedJson.size()-1) w.write(",\n");
            }
            w.write("\n]");
            w.close();
        } catch (IOException e) {}
    }
}