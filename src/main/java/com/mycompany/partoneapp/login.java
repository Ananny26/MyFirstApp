/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.partoneapp;

/**
 *
 * @author ananny
 */
public class login {
     // Variables
    public String firstName;
    public String lastName;
    public String cellPhoneNumber;
    public String userName;
    public String password;
    public boolean loginStatus;

    // Constructor
    public login(String userName, String password, String cellPhoneNumber, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhoneNumber = cellPhoneNumber;
        this.userName = userName;
        this.password = password;
        this.loginStatus = false;
    }

    public boolean checkUserName() {
        return userName.contains("_") && userName.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return password.matches(passwordRegex);
    }

    public boolean checkCellPhoneNumber() {
        String cellPhoneRegex = "\\+27\\d{9}";
        return cellPhoneNumber.matches(cellPhoneRegex);
    }

    public String registerUser() {
        if (checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully captured.");
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an "
                    + "international code; please correct the number and try again.";
        }

        return "User successfully registered.";
    }

    public boolean loginUser(String enteredUserName, String enteredPassword) {
        if (enteredUserName.equals(userName) && enteredPassword.equals(password)) {
            loginStatus = true;
        } else {
            loginStatus = false;
        }
        return loginStatus;
    }

    public String returnLoginStatus() {
        if (loginStatus) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again!a";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
    

