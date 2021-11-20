package gauncher.frontend2.view;

import gauncher.frontend2.exception.UnprocessableViewException;

public class LoginView extends View{

  public LoginView() throws UnprocessableViewException {
    super("/fxml/login_view.fxml");
  }
}
