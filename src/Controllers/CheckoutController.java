package Controllers;

// importing all the necessary required libraries
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class CheckoutController {

	@FXML
	private Button btnlogout;

	@FXML
	private Button btndashboard;

	@FXML
	private Button btnconfirm;

	@FXML
	private TextField txtCreditCardNumber;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtDate;

	@FXML
	private TextFlow errcreditcard;

	@FXML
	private TextFlow errcvv;

	@FXML
	private TextFlow errdate;

	@FXML
	public void initialize() {
		System.out.println("Initializing DashboardController");

		// Check if buttons are properly initialized
		System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
		System.out.println("btnexport: " + (btndashboard == null ? "null" : "initialized"));
		// System.out.println("btncart: " + (btncart == null ? "null" : "initialized"));
	}

	public void goToLogOut() {
		System.out.println("Logout button clicked!");
		try {
			Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
			Stage stage = (Stage) btnlogout.getScene().getWindow();
			stage.setScene(new Scene(loginPage));
			stage.show();
		} catch (Exception e) {
			System.err.println("Failed to load Login page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void goToDashboard() {
		System.out.println("Export button clicked!");
		try {
			Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
			Stage stage = (Stage) btndashboard.getScene().getWindow();
			stage.setScene(new Scene(exportPage));
			stage.show();
		} catch (Exception e) {
			System.err.println("Failed to load export page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void goToConfirm() {
		System.out.println("Export button clicked!");
		System.out.println("Date entered: " + txtDate.getText());

		clearErrorMessages();

		boolean isValid = true;

		if (!isValidCreditCard(txtCreditCardNumber.getText())) {
			showError(errcreditcard, "Invalid credit card number.");
			isValid = false;
		}
		if (!isValidCVV(txtCVV.getText())) {
			showError(errcvv, "Invalid CVV.");
			isValid = false;
		}
		if (!isValidExpiryDate(txtDate.getText().trim())) {
			showError(errdate, "Invalid expiry date.");
			isValid = false;
		}

		if (isValid) {
			try {

				Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/CheckoutConfirm.fxml"));
				Stage stage = (Stage) btnconfirm.getScene().getWindow();
				stage.setScene(new Scene(exportPage));
				stage.show();
			} catch (Exception e) {
				System.err.println("Failed to load export page: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void clearErrorMessages() {
		errcreditcard.getChildren().clear();
		errcvv.getChildren().clear();
		errdate.getChildren().clear();
	}

	private void showError(TextFlow errorFlow, String message) {
		Text errorText = new Text(message);
		errorText.setFill(Color.RED);
		errorFlow.getChildren().add(errorText);
	}

	public boolean isValidCreditCard(String creditCardNumber) {
		return creditCardNumber.length() == 16 && creditCardNumber.matches("\\d+");
	}

	private boolean isValidCVV(String cvv) {
		return cvv.length() == 3 && cvv.matches("\\d+");
	}

	private boolean isValidExpiryDate(String expiryDate) {
		System.out.println("Validating date: " + expiryDate);
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
			YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);
			YearMonth currentYearMonth = YearMonth.now();

			System.out.println("Parsed expiry year/month: " + expiryYearMonth);
			System.out.println("Current year/month: " + currentYearMonth);
			System.out.println("Is expiry after or equal to current: " + !expiryYearMonth.isBefore(currentYearMonth));

			return !expiryYearMonth.isBefore(currentYearMonth);
		} catch (DateTimeParseException e) {
			System.out.println("Date parsing failed: " + e.getMessage());
			return false;
		}
	}

}