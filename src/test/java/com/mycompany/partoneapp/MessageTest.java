/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.partoneapp;

import com.mycompany.partoneapp.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test public void testCheckMessageID() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(m.checkMessageID());
    }
    @Test public void testRecipientValid() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }
    @Test public void testRecipientInvalid() {
        Message m = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertTrue(m.checkRecipientCell().contains("incorrectly"));
    }
    @Test public void testMessageHash() {
        Message m = new Message();
        String hash = m.createMessageHash("0012345678", 0, "Hi tonight");
        assertEquals("00:0:HITONIGHT", hash);
    }
    @Test public void testLengthSuccess() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", m.checkMessageLength(m.messageText));
    }
    @Test public void testLengthFailure() {
        String longMsg = "a".repeat(300);
        Message m = new Message(0, "+27718693002", longMsg);
        assertTrue(m.checkMessageLength(longMsg).contains("exceeds"));
    }
    @Test public void testSentMessage() {
        Message m = new Message(0, "+27718693002", "Hi");
        assertEquals("Message successfully sent.", m.SentMessage(1));
        assertEquals("Press 0 to delete the message.", m.SentMessage(2));
        assertEquals("Message successfully stored.", m.SentMessage(3));
    }
    @Test public void testTotalMessages() {
        Message m = new Message(0, "+27718693002", "Hi");
        m.SentMessage(1);
        assertTrue(m.returnTotalMessagess() >= 1);
    }
}