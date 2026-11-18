/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.partoneapp;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author anann
 */

   
// unit tests for the Part 3 arrays and features, using the Part 3 test data
public class MessageManagerTest {
    private static final String TEXT_1 = "Did you get the cake?";
    private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT_3 = "Yohoooo, I am at your gate.";
    private static final String TEXT_4 = "It is dinner time!";
    private static final String TEXT_5 = "Ok, I am leaving without you.";
    private static final String RECIPIENT_2 = "+27838884567";

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    // developer entry of the test data: 1 and 4 sent, 2 and 5 stored, 3 disregarded
    @BeforeEach
    public void setUp() throws Exception {
        Files.deleteIfExists(Paths.get(MessageManager.JSON_FILE));
        Message.resetAll();
        message1 = new Message("1000000001", 0, "+27834557896", TEXT_1);
        message2 = new Message("1000000002", 1, RECIPIENT_2, TEXT_2);
        message3 = new Message("1000000003", 2, "+27834484567", TEXT_3);
        // the brief's developer number 0838884567 is used as this message's ID and recipient
        message4 = new Message("0838884567", 3, "0838884567", TEXT_4);
        message5 = new Message("1000000005", 4, RECIPIENT_2, TEXT_5);
        message1.sentMessage(1);
        message2.sentMessage(3);
        message3.sentMessage(2);
        message4.sentMessage(1);
        message5.sentMessage(3);
    }

    @Test public void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT_1, TEXT_4), MessageManager.getSentMessages());
    }

    @Test public void disregardedMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT_3), MessageManager.getDisregardedMessages());
    }

    @Test public void storedMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT_2, TEXT_5), MessageManager.getStoredMessages());
    }

    @Test public void hashAndIdArraysCorrectlyPopulated() {
        assertEquals(4, MessageManager.getMessageHashes().size());
        assertTrue(MessageManager.getMessageHashes().contains(message2.getMessageHash()));
        assertTrue(MessageManager.getMessageIDs().contains("0838884567"));
    }

    @Test public void displayLongestMessage() {
        assertEquals(TEXT_2, MessageManager.longestStoredMessage());
    }

    @Test public void searchForMessageID() {
        assertEquals(TEXT_4, MessageManager.searchByMessageID("0838884567"));
        assertEquals(MessageManager.ID_NOT_FOUND, MessageManager.searchByMessageID("9999999999"));
    }

    @Test public void searchMessagesForRecipient() {
        assertEquals(Arrays.asList(TEXT_2, TEXT_5), MessageManager.searchByRecipient(RECIPIENT_2));
    }

    @Test public void deleteMessageUsingHash() {
        assertEquals("Message: \"" + TEXT_2 + "\" successfully deleted.",
                MessageManager.deleteByHash(message2.getMessageHash()));
        assertFalse(MessageManager.getStoredMessages().contains(TEXT_2));
        assertEquals(MessageManager.HASH_NOT_FOUND, MessageManager.deleteByHash("NOT:A:HASH"));
    }

    @Test public void senderAndRecipientsOfStoredMessages() {
        MessageManager.setSender("Anan");
        String output = MessageManager.displaySendersAndRecipients();
        assertTrue(output.contains("Sender: Anan | Recipient: " + RECIPIENT_2));
        MessageManager.reset();
        assertEquals(MessageManager.NO_STORED, MessageManager.displaySendersAndRecipients());
    }

    @Test public void displayReportShowsHashRecipientAndMessage() {
        String report = MessageManager.report(MessageManager.SENT);
        assertTrue(report.contains(message1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains(TEXT_1));
        assertTrue(report.contains(message4.getMessageHash()));
        assertTrue(report.contains(TEXT_4));
        assertFalse(report.contains(TEXT_2));
    }

    @Test public void storedMessagesReadBackFromJson() {
        MessageManager.reset();
        assertEquals(2, MessageManager.loadStoredMessages());
        assertEquals(Arrays.asList(TEXT_2, TEXT_5), MessageManager.getStoredMessages());
        assertEquals(0, MessageManager.loadStoredMessages());
    }
}

