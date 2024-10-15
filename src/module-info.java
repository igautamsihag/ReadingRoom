module ReadRoomDesktop {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
//	requires org.slf4j;
//    requires org.xerial.sqlitejdbc;
	
	opens application to javafx.graphics, javafx.fxml;
	opens Controllers to javafx.fxml;
	opens Views to javafx.fxml;
	opens Model to javafx.base;
}
