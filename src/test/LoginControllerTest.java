package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controllers.LoginController;

public class LoginControllerTest {

    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        loginController = new LoginController();
    }

    @Test
    public void testAdminAuthentication() {
        String username1 = "user"; // Example valid card number
        String password1 = "reading_admin"; // Example invalid card number

        String username2 = "admin"; // Example valid card number
        String password2 = "reading_admin";
        
        assertTrue(loginController.adminAuthentication(username2, password2));
        assertFalse(loginController.adminAuthentication(username1, password1));
        
    }

}
