module com.example.studentapp.apprapphim {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.net.http;


    opens com.example.studentapp.apprapphim to javafx.fxml;
    exports com.example.studentapp.apprapphim;
}