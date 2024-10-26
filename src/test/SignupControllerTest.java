package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controllers.SignupController;

public class SignupControllerTest {

    private SignupController signupController;

    @BeforeEach
    public void setUp() {
        signupController = new SignupController();
    }

    @Test
    public void testCustomerName() {
        String validFName = "mike"; // Example valid card number
        String validLName = "ponting"; // Example invalid card number

        String invalidFName = "12tod"; // Example valid card number
        String invalidLName = "murphy09";
        
        assertFalse(signupController.containsNumbers(validFName));
        assertTrue(signupController.containsNumbers(invalidFName));
        assertFalse(signupController.containsNumbers(validLName));
        assertTrue(signupController.containsNumbers(invalidLName));
        
    }

}
