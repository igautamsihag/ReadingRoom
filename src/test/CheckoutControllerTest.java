package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controllers.CheckoutController;

public class CheckoutControllerTest {

    private CheckoutController checkoutController;

    @BeforeEach
    public void setUp() {
        checkoutController = new CheckoutController();
    }

    @Test
    public void testValidCreditCard() {
        String validCreditCard = "1234567812345678"; 
        String invalidCreditCard = "1234abcd56789012"; 
        
        assertTrue(checkoutController.validateCreditCard(validCreditCard));
        assertFalse(checkoutController.validateCreditCard(invalidCreditCard));
        
    }

    @Test
    public void testValidCVV() {
        String validCVV = "999"; // Example valid CVV
        String invalidCVV = "89z"; // Example invalid CVV
        String invalidCVV2 = "9024"; // CVV too long

        assertTrue(checkoutController.validateCVV(validCVV));
        assertFalse(checkoutController.validateCVV(invalidCVV));
        assertFalse(checkoutController.validateCVV(invalidCVV2));
    }

    @Test
    public void testValidExpiryDate() {
        String firsttest = "12/2025"; // Future date
        String secondtest = "01/2020"; // Past date
        String thirdtest = "2025/12"; // Incorrect format
        String fourthtest = "13/2025"; // Invalid month

        assertTrue(checkoutController.validateExpiryDate(firsttest));
        assertFalse(checkoutController.validateExpiryDate(secondtest));
        assertFalse(checkoutController.validateExpiryDate(thirdtest));
        assertFalse(checkoutController.validateExpiryDate(fourthtest));
    }
}
