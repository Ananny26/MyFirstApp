/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.partoneapp.login;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author anann
 */
public class loginTest {

    // Test data from the brief
    private static final String USER = "kyl_1";
    private static final String PASS = "Ch&&sec@ke99!";
    private static final String CELL = "+27838968976";

    private login newLogin(String user, String pass, String cell) {
        return new login(user, pass, cell, "Mutondwa", "Munyai");
    }

    // registerUser() prints the "successfully captured" lines, so capture the console output
    private String[] printedLines(login l) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            l.registerUser();
        } finally {
            System.setOut(original);
        }
        return buffer.toString().split("\\R");
    }

    // ---------- checkUserName() ----------
    @Test
    public void testCheckUserNameValid() {
        assertTrue(newLogin("kyl_1", PASS, CELL).checkUserName());
    }

    @Test
    public void testCheckUserNameInvalid() {
        assertFalse(newLogin("kyle!!!!!!!", PASS, CELL).checkUserName());
    }

    // ---------- checkPasswordComplexity() ----------
    @Test
    public void testCheckPasswordComplexityValid() {
        assertTrue(newLogin(USER, "Ch&&sec@ke99!", CELL).checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        assertFalse(newLogin(USER, "password", CELL).checkPasswordComplexity());
    }

    // ---------- checkCellPhoneNumber() ----------
    @Test
    public void testCheckCellPhoneNumberValid() {
        assertTrue(newLogin(USER, PASS, "+27838968976").checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumberInvalid() {
        assertFalse(newLogin(USER, PASS, "08966553").checkCellPhoneNumber());
    }

    // ---------- registerUser() ----------
    @Test
    public void testRegisterUserSuccessful() {
        assertEquals("User successfully registered.", newLogin(USER, PASS, CELL).registerUser());
    }

    @Test
    public void testUsernameSuccessfullyCaptured() {
        assertEquals("Username successfully captured.", printedLines(newLogin(USER, PASS, CELL))[0]);
    }

    @Test
    public void testPasswordSuccessfullyCaptured() {
        assertEquals("Password successfully captured.", printedLines(newLogin(USER, PASS, CELL))[1]);
    }

    @Test
    public void testCellPhoneSuccessfullyCaptured() {
        assertEquals("Cell phone number successfully captured.", printedLines(newLogin(USER, PASS, CELL))[2]);
    }

    @Test
    public void testRegisterUserUsernameInvalid() {
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.",
                newLogin("kyle!!!!!!!", PASS, CELL).registerUser());
    }

    @Test
    public void testRegisterUserPasswordInvalid() {
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.",
                newLogin(USER, "password", CELL).registerUser());
    }

    @Test
    public void testRegisterUserCellPhoneInvalid() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code; please correct the number and try again.",
                newLogin(USER, PASS, "08966553").registerUser());
    }

    // ---------- loginUser() ----------
    @Test
    public void testLoginSuccessful() {
        assertTrue(newLogin(USER, PASS, CELL).loginUser(USER, PASS));
    }

    @Test
    public void testLoginFailed() {
        assertFalse(newLogin(USER, PASS, CELL).loginUser("wrong_username", "wrong_password"));
    }

    // ---------- returnLoginStatus() ----------
    @Test
    public void testReturnLoginStatusSuccessful() {
        login l = newLogin(USER, PASS, CELL);
        l.loginUser(USER, PASS);
        assertEquals("Welcome Mutondwa, Munyai it is great to see you again.", l.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatusFailed() {
        login l = newLogin(USER, PASS, CELL);
        l.loginUser("wrong_username", "wrong_password");
        assertEquals("Username or password incorrect, please try again.", l.returnLoginStatus());
    }
}