package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class LoginView extends View {

  public LoginView() throws UnprocessableViewException {
    super("/fxml/login.fxml");
  }
}
