package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class ConnectionView extends View {
  public ConnectionView() throws UnprocessableViewException {
    super("/fxml/connection_view.fxml");
  }
}
