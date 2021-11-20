package gauncher.frontend2.view;

import gauncher.frontend2.exception.UnprocessableViewException;

public class ConnectionView extends View {
  public ConnectionView() throws UnprocessableViewException {
    super("/fxml/connection_view.fxml");
  }
}
