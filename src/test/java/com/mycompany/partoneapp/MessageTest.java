/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.partoneapp;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class MessageTest {

    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    private Message message1;
    private Message message2;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        Message.resetAll();
        Message.setJsonFilePath(tempDir.resolve("messages.json").toString());
        // IDs are fixed so the hash can be checked; the app auto-generates real ones
        message1 = new Message("0012345678", 0, "+27718693002", TEXT_1);
        message2 = new Message("0198765432", 1, "08575975889", TEXT_2);
    }

    // ---------- assertEquals ----------

    @Test
    public void messageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void messageLengthFailure() {
        Message tooLong = new Message("0012345678", 0, "+27718693002", "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                tooLong.checkMessageLength());
    }

    @Test
    public void recipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void recipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    @Test
    public void messageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void messageHashesInALoop() {
        Message[] messages = {message1, message2};
        String[] expected = {"00:0:HITONIGHT", "01:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    @Test
    public void messageIDIsCreated() {
        String id = Message.generateMessageID();
        assertEquals(10, id.length());
        Message m = new Message(id, 0, "+27718693002", TEXT_1);
        assertEquals("Message ID generated: " + id, "Message ID generated: " + m.getMessageID());
    }

    @Test
    public void sentMessageOptions() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
        assertEquals("Press 0 to delete the message.", message1.sentMessage(2));
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // ---------- assertTrue / assertFalse and totals ----------

    @Test
    public void messageIDLengthCheck() {
        assertTrue(message1.checkMessageID());
        assertFalse(new Message("12345678901", 0, "+27718693002", TEXT_1).checkMessageID());
    }

    @Test
    public void totalNumberSentMatchesTestData() {
        message1.sentMessage(1); // Send
        message2.sentMessage(2); // Discard
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void printMessagesShowsDetailsInOrder() {
        message1.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.indexOf("Message ID") < printed.indexOf("Message Hash"));
        assertTrue(printed.indexOf("Message Hash") < printed.indexOf("Recipient"));
        assertTrue(printed.indexOf("Recipient") < printed.indexOf("Message: Hi Mike"));
    }

    @Test
    public void storedMessageIsWrittenToJson() throws Exception {
        message1.sentMessage(3);
        String json = Files.readString(tempDir.resolve("messages.json"));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(json.contains("+27718693002"));
    }
}
 
