package application;
	
// importing all the necessary required libraries
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

// the entry point of the application which is the log in page
public class Main extends Application {
	
	// overriding the start function of the Application class
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// accessing the login page from the views folder
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
			Parent pane = loader.load();
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Log In - The Reading Room");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// launching the application
	public static void main(String[] args) {
		launch(args);
	}
}
