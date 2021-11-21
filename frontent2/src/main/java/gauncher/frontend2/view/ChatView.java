package gauncher.frontend2.view;

import gauncher.frontend2.exception.UnprocessableViewException;

public class ChatView extends View{

    public ChatView() throws UnprocessableViewException {
        super("/fxml/chat.fxml");
    }
}
