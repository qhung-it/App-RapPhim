module com.example.studentapp.apprapphim {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.example.studentapp.apprapphim to javafx.fxml;
    exports com.example.studentapp.apprapphim;
}