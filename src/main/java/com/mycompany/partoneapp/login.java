/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.partoneapp;

/**
 *
 * @author anann
 */

//this class handles user registration and login 
public class login {
    public String firstName;
    public String lastName;
    public String cellPhoneNumber;
    public String userName;
    public String password;
    public boolean loginStatus;

    //constructor
    public login(String userName, String password, String cellPhoneNumber, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhoneNumber = cellPhoneNumber;
        this.userName = userName;
        this.password = password;
        this.loginStatus = false;
    }

    //check if username contains an underscore and <= characters
    public boolean checkUserName() {
        return userName.contains("_") && userName.length() <= 5;
    }

    //check password complexity, must have 8 characters, capital letter, number and special case
    public boolean checkPasswordComplexity() {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return password.matches(passwordRegex);
    }

    //check the cell number format
    public boolean checkCellPhoneNumber() {
        String cellPhoneRegex = "\\+27\\d{9}";
        return cellPhoneNumber.matches(cellPhoneRegex);
    }

    //register user step by step
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        System.out.println("Username successfully captured.");
        //check password
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        System.out.println("Password successfully captured.");
        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
        System.out.println("Cell phone number successfully captured.");
        return "User successfully registered.";
    }

    //login method- compares entered details with stored details
    public boolean loginUser(String enteredUserName, String enteredPassword) {
        if (enteredUserName.equals(userName) && enteredPassword.equals(password)) {
            loginStatus = true;
        } else {
            loginStatus = false;
        }
        return loginStatus;
    }

    //returns login in status if logged in
    public String returnLoginStatus() {
        if (loginStatus) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}