module Yu.Gi.Oh {
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;
    requires cloning;
    requires commons.math3;
    requires javafx.controls;
    exports view;
    opens model.card to cloning;
    opens model.card.effects to cloning;
}