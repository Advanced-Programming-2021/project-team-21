module Yu.Gi.Oh {
    requires com.google.gson;
    requires cloning;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires opencsv;
    requires commons.math3;
    requires javafx.media;
    opens model.card to cloning;
    opens model.card.effects to cloning;
    opens model to cloning;
    opens view to javafx.fxml;
    exports controller;
    exports model;
    exports view;
    exports model.card;
}