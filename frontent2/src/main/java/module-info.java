module frontend2 {
    requires javafx.controls;
    requires javafx.fxml;
    exports gauncher.frontend2;
    exports gauncher.frontend2.controller;
    exports gauncher.frontend2.exception;
    exports gauncher.frontend2.view;
    exports gauncher.frontend2.client;
    exports gauncher.frontend2.logging;
    opens gauncher.frontend2.controller;
}
