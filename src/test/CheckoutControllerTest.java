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
        String validCreditCard = "1234567812345678"; // Example valid card number
        String invalidCreditCard = "1234abcd56789012"; // Example invalid card number

        assertTrue(checkoutController.isValidCreditCard(validCreditCard));
        assertFalse(checkoutController.isValidCreditCard(invalidCreditCard));
        
    }

    @Test
    public void testValidCVV() {
        String validCVV = "123"; // Example valid CVV
        String invalidCVV = "12a"; // Example invalid CVV
        String tooLongCVV = "1234"; // CVV too long

        assertTrue(checkoutController.isValidCVV(validCVV));
        assertFalse(checkoutController.isValidCVV(invalidCVV));
        assertFalse(checkoutController.isValidCVV(tooLongCVV));
    }

    @Test
    public void testValidExpiryDate() {
        String validExpiryDate = "12/2025"; // Future date
        String pastExpiryDate = "01/2020"; // Past date
        String invalidFormatDate = "2025/12"; // Incorrect format
        String invalidMonthDate = "13/2025"; // Invalid month

        assertTrue(checkoutController.isValidExpiryDate(validExpiryDate));
        assertFalse(checkoutController.isValidExpiryDate(pastExpiryDate));
        assertFalse(checkoutController.isValidExpiryDate(invalidFormatDate));
        assertFalse(checkoutController.isValidExpiryDate(invalidMonthDate));
    }
}
