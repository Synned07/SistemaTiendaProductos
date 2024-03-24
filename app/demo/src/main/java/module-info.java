module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires com.google.gson;
    requires Random;
    requires bigdecimal;
    requires org.joda.time;
    requires commons.lang;

    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.models;
    exports com.example.repository;
}
