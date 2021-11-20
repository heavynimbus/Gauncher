package gauncher.frontend2.exception;

public class UnprocessableViewException extends Exception{

  public UnprocessableViewException(String message) {
    super(message);
  }

  public UnprocessableViewException(){
    this("Something has been broken while processing view");
  }
}
