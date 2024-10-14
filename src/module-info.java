module ReadRoomDesktop {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
	opens Controllers to javafx.fxml;
}
