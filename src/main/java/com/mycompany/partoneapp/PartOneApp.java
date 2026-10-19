/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.partoneapp;

import java.util.Scanner;

/**
 *
 * @author ananny
 */

public class PartOneApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("-----------Registration-----------");
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

        login login = new login(userName, password, cellPhoneNumber, firstName, lastName);
        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        if (registrationResult.equals("User successfully registered.")) {
            System.out.println("----------Login----------");
            System.out.println("Enter your username");
            String enteredUserName = input.nextLine();
            System.out.println("Enter your password");
            String enteredPassword = input.nextLine();
            login.loginUser(enteredUserName, enteredPassword);
            System.out.println(login.returnLoginStatus());
            if (login.loginStatus) {
                runQuickChat(input);
            }
        }
        input.close();
    }

    private static void runQuickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");
        int maxMessages = readNumber(input, "How many messages would you like to enter?", 1, 100);
        int entered = 0;
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int option = readNumber(input, "", 1, 3);
            if (option == 1) {
                if (entered >= maxMessages) {
                    System.out.println("You have already entered all " + maxMessages + " messages.");
                } else {
                    while (entered < maxMessages) {
                        captureMessage(input, entered);
                        entered++;
                    }
                    System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
                }
            } else if (option == 2) {
                System.out.println("Coming Soon.");
            } else {
                System.out.println("Goodbye.");
                running = false;
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