/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.partoneapp;

import java.util.Scanner;

/**
 
 * @author ananny
 */
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

        login login = new login(userName, password, cellPhoneNumber, firstName, lastName);

        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        if (registrationResult.startsWith("User successfully registered")) {
            System.out.println("----------Login----------");
            System.out.println("Enter your username");
            String enteredUserName = input.nextLine();
            System.out.println("Enter your password");
            String enteredPassword = input.nextLine();

            login.loginUser(enteredUserName, enteredPassword);
            System.out.println(login.returnLoginStatus());
        }

        input.close();
    }
}
    

