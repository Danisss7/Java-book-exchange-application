module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires jdk.jfr;
    requires java.sql;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires jdk.compiler;
    requires annotations;
    exports org.example.demo to javafx.graphics;

    opens org.example.demo.controllers to javafx.fxml;
    exports org.example.demo.controllers;

    exports org.example.demo.model;
    opens org.example.demo.model to org.hibernate.orm.core;
}