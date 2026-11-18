/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.partoneapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


// unit tests for Message class
public class MessageTest {
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";
    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        Message.resetAll();
        message1 = new Message("0012345678", 0, "+27718693002", TEXT_1);
        message2 = new Message("0198765432", 1, "08575975889", TEXT_2);
    }

    @Test public void messageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test public void messageLengthFailure() {
        Message tooLong = new Message("0012345678", 0, "+27718693002", "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", tooLong.checkMessageLength());
    }

    @Test public void recipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test public void recipientIncorrectlyFormatted() {
        assertEquals(Message.CELL_BAD, message2.checkRecipientCell());
    }

    // both hash test cases are checked in one loop
    @Test public void messageHashTestCases() {
        Message[] messages = {message1, message2};
        String[] expected = {"00:0:HITONIGHT", "01:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash(), "hash test case " + (i + 1));
        }
    }

    @Test public void messageIDIsCreated() {
        for (int i = 0; i < 50; i++) {
            String id = Message.generateMessageID();
            assertEquals(10, id.length());
            assertTrue(id.matches("\\d{10}"));
        }
    }

    @Test public void messageIDLengthCheck() {
        assertTrue(message1.checkMessageID());
        assertFalse(new Message("12345678901", 0, "+27718693002", TEXT_1).checkMessageID());
    }

    @Test public void sentMessageOptions() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
        assertEquals("Press 0 to delete the message.", message1.sentMessage(2));
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    @Test public void invalidChoiceIsNotStored() {
        assertEquals("Invalid choice.", message1.sentMessage(9));
        assertEquals(0, Message.returnStoredCount());
    }

    @Test public void storeMessageWritesJsonFile() throws Exception {
        message1.sentMessage(3);
        String json = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(MessageManager.JSON_FILE)));
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\"recipient\": \"+27718693002\""));
        assertEquals(1, Message.returnStoredCount());
    }

    @Test public void printMessagesListsOnlySentMessages() {
        message1.sentMessage(1);
        message2.sentMessage(2);
        String printed = Message.printMessages();
        assertTrue(printed.contains("0012345678"));
        assertFalse(printed.contains("0198765432"));
    }

    @Test public void totalNumberSentMatchesTestData() {
        message1.sentMessage(1);
        message2.sentMessage(2);
        assertEquals(1, Message.returnTotalMessages());
    }
}