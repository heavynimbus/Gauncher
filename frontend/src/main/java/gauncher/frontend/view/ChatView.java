package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class ChatView extends View{

    public ChatView() throws UnprocessableViewException {
        super("/fxml/chat.fxml");
    }
}
