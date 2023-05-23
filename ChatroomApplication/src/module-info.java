module ChatroomApplication {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	
	opens application.controllers to javafx.fxml;
	opens application.view to javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
