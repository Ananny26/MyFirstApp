/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.partoneapp;

import java.util.Scanner;

/**
 *
 * @author ananny
 */

// runs registration, login and menu
public class PartOneApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("----------Registration----------");
        System.out.println("Enter your first name");
        String firstName = input.nextLine();
        System.out.println("Enter your last name");
        String lastName = input.nextLine();
        System.out.println("Enter your cell phone number");
        String cellPhoneNumber = input.nextLine();
        System.out.println("Enter your username");
        String userName = input.nextLine();
        System.out.println("Enter your password");
        String password = input.nextLine();

        // create login object with entered details
        login login = new login(userName, password, cellPhoneNumber, firstName, lastName);
        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        // only allow login if registration is successful
        if (registrationResult.equals("User successfully registered.")) {
            System.out.println("----------Login----------");
            System.out.println("Enter your username");
            String enteredUserName = input.nextLine();
            System.out.println("Enter your password");
            String enteredPassword = input.nextLine();
            login.loginUser(enteredUserName, enteredPassword);
            System.out.println(login.returnLoginStatus());
            if (login.loginStatus) {
                MessageManager.setSender(firstName);
                runQuickChat(input);
            }
        }
        input.close();
    }

    private static void runQuickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");
        MessageManager.loadStoredMessages();
        int maxMessages = readNumber(input, "How many messages would you like to enter?", 1, 100);
        int entered = 0;
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int option = readNumber(input, "", 1, 4);
            if (option == 1) {
                if (entered >= maxMessages) {
                    System.out.println("You have already entered all " + maxMessages + " messages.");
                } else {
                    // for loop enters the remaining messages
                    int start = entered;
                    for (int i = start; i < maxMessages; i++) {
                        captureMessage(input, i);
                        entered++;
                    }
                    System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
                }
            } else if (option == 2) {
                System.out.println(MessageManager.report(MessageManager.SENT));
            } else if (option == 4) {
                runStoredMessages(input);
            } else {
                System.out.println("Goodbye.");
                running = false;
            }
        }
    }

    // fourth menu option: works with the stored messages read from the JSON file
    private static void runStoredMessages(Scanner input) {
        int loaded = MessageManager.loadStoredMessages();
        System.out.println("Stored messages read from " + MessageManager.JSON_FILE
                + " (" + loaded + " new).");
        boolean viewing = true;
        while (viewing) {
            System.out.println("\nStored Messages:");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages stored for a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to main menu");
            int option = readNumber(input, "", 1, 7);
            if (option == 1) {
                System.out.println(MessageManager.displaySendersAndRecipients());
            } else if (option == 2) {
                System.out.println(MessageManager.longestStoredMessage());
            } else if (option == 3) {
                System.out.println("Enter the message ID");
                String id = input.nextLine().trim();
                String found = MessageManager.searchByMessageID(id);
                if (found.equals(MessageManager.ID_NOT_FOUND)) {
                    System.out.println(found);
                } else {
                    System.out.println("Recipient: " + MessageManager.recipientByMessageID(id));
                    System.out.println("Message: " + found);
                }
            } else if (option == 4) {
                System.out.println("Enter the recipient's cell phone number");
                java.util.ArrayList<String> found = MessageManager.searchByRecipient(input.nextLine().trim());
                if (found.isEmpty()) {
                    System.out.println("No messages found for that recipient.");
                }
                for (String text : found) {
                    System.out.println(text);
                }
            } else if (option == 5) {
                System.out.println("Enter the message hash");
                System.out.println(MessageManager.deleteByHash(input.nextLine().trim()));
            } else if (option == 6) {
                System.out.println(MessageManager.report(MessageManager.STORED));
            } else {
                viewing = false;
            }
        }
    }

    private static void captureMessage(Scanner input, int messageNumber) {
        System.out.println("\nMessage " + (messageNumber + 1));
        String recipient;
        while (true) {
            System.out.println("Enter the recipient's cell phone number (with international code)");
            recipient = input.nextLine().trim();
            Message temp = new Message("0000000000", 0, recipient, "x");
            String check = temp.checkRecipientCell();
            System.out.println(check);
            if (check.equals(Message.CELL_OK)) break;
        }
        Message message = null;
        while (true) {
            System.out.println("Enter your message (maximum 250 characters)");
            String text = input.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("A message cannot be empty.");
                continue;
            }
            message = new Message(Message.generateMessageID(), messageNumber, recipient, text);
            String lengthCheck = message.checkMessageLength();
            System.out.println(lengthCheck);
            if (lengthCheck.equals(Message.READY)) break;
        }
        System.out.println("Message ID generated: " + message.getMessageID());
        System.out.println("\n1) Send Message\n2) Disregard Message\n3) Store Message to send later");
        int choice = readNumber(input, "", 1, 3);
        System.out.println(message.sentMessage(choice));
        if (choice == 1) {
            System.out.println("\n" + message.details());
        } else if (choice == 2) {
            readNumber(input, "", 0, 0);
            System.out.println("Message deleted.");
        }
    }

    private static int readNumber(Scanner input, String prompt, int min, int max) {
        while (true) {
            if (!prompt.isEmpty()) System.out.println(prompt);
            String line = input.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException e) {}
            System.out.println("Please enter a valid number.");
        }
    }
}