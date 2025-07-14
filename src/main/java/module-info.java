module Beautygest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    
    opens Application to javafx.fxml;
    exports Application;
    
    opens Controller to javafx.fxml;
    exports Controller;
    
    opens dao to javafx.fxml;
    exports dao;
    
    opens model to javafx.fxml;
    exports model;
    
    opens util to javafx.fxml;
    exports util;
}
