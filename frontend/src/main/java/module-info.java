module frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    exports gauncher.frontend;
    exports gauncher.frontend.controller;
    exports gauncher.frontend.exception;
    exports gauncher.frontend.view;
    exports gauncher.frontend.client;
    exports gauncher.frontend.logging;
    opens gauncher.frontend.controller;
}
