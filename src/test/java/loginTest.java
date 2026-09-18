/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.partoneapp.login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author anann
 */
public class loginTest {
    // ---------- checkUserName() ----------
    @Test
    public void testCheckUserNameValid() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testCheckUserNameInvalid() {
        login login = new login("kyle!!!!!!", "Ch&cK@ke44!", "+27768978651", "Mutondwa", "Munyai");
        assertFalse(login.checkUserName());
    }

    // ---------- checkPasswordComplexity() ----------
    @Test
    public void testCheckPasswordComplexityValid() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27670986756", "Mutondwa", "Munyai");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        login login = new login("kyl_1", "samuke", "+27768978651", "Mutondwa", "Munyai");
        assertFalse(login.checkPasswordComplexity());
    }

    // ---------- checkCellPhoneNumber() ----------
    @Test
    public void testCheckCellPhoneNumberValid() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumberInvalid() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "0768978651", "Mutondwa", "Munyai");
        assertFalse(login.checkCellPhoneNumber());
    }

    // ---------- registerUser() ----------
    @Test
    public void testRegisterUserSuccessful() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        assertEquals("User successfully registered.", login.registerUser());
    }

    @Test
    public void testRegisterUserUsernameInvalid() {
        login login = new login("kyle!!!!!!", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.",
                login.registerUser());
    }

    @Test
    public void testRegisterUserPasswordInvalid() {
        login login = new login("kyl_1", "password", "+27768978651", "Mutondwa", "Munyai");
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser());
    }

    @Test
    public void testRegisterUserCellPhoneInvalid() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "08966553", "Mutondwa", "Munyai");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code; please correct the number and try again.",
                login.registerUser());
    }

    // ---------- loginUser() ----------
    @Test
    public void testLoginSuccessful() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        assertTrue(login.loginUser("kyl_1", "Ch&cK@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        assertFalse(login.loginUser("wrong_username", "wrong_password"));
    }

    // ---------- returnLoginStatus() ----------
    @Test
    public void testReturnLoginStatusSuccessful() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        login.loginUser("kyl_1", "Ch&cK@ke99!");
        assertEquals("Welcome Mutondwa, Munyai it is great to see you again.", login.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatusFailed() {
        login login = new login("kyl_1", "Ch&cK@ke99!", "+27768978651", "Mutondwa", "Munyai");
        login.loginUser("wrong_username", "wrong_password");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }
}
    
    

