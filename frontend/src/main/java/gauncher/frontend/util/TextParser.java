package gauncher.frontend.util;

import gauncher.frontend.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextParser {

  private Logger log = new Logger("TextParser");

  private final SimpleStringProperty simpleStringProperty;
  private final TextInputControl textInputControl;
  private final List<Predicate<KeyEvent>> filters;
  private final List<BiConsumer<KeyEvent, SimpleStringProperty>> actions;
  private final List<BiConsumer<TextInputControl, SimpleStringProperty>> endActions;

  public TextParser(SimpleStringProperty simpleStringProperty, TextInputControl textInputControl) {
    this.simpleStringProperty = simpleStringProperty;
    this.textInputControl = textInputControl;
    this.actions = new ArrayList<>();
    this.endActions = new ArrayList<>();
    this.filters = new ArrayList<>();
  }

  public TextParser addAction(BiConsumer<KeyEvent, SimpleStringProperty> propertyBiConsumer) {
    this.actions.add(propertyBiConsumer);
    return this;
  }

  public TextParser addFilter(Predicate<KeyEvent> filter) {
    filters.add(filter);
    return this;
  }

  public TextParser addEndAction(
      BiConsumer<TextInputControl, SimpleStringProperty> propertyBiConsumer) {
    endActions.add(propertyBiConsumer);
    return this;
  }

  public void inputValue(KeyEvent event) {
    AtomicBoolean filterResult = new AtomicBoolean(true);
    this.filters.forEach(filter -> filterResult.set(filter.test(event) && filterResult.get()));
    var value = event.isShiftDown() ? event.getText().toUpperCase(Locale.ROOT) : event.getText();
    if (filterResult.get()) simpleStringProperty.set(simpleStringProperty.get() + value);

    this.actions.forEach((biConsumer) -> biConsumer.accept(event, simpleStringProperty));

    this.endActions.forEach(
        (biConsumer) -> biConsumer.accept(textInputControl, simpleStringProperty));
  }

  public static TextParser getDefaultInstance(
      SimpleStringProperty simpleStringProperty, TextInputControl textInputControl) {
    return new TextParser(simpleStringProperty, textInputControl)
        .addFilter((event -> event.getText() != null)) // Accepts all characters
        .addAction( // Remove character on backspace
            (event, property) -> {
              if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                if (event.isControlDown()) property.set("");
                else {
                  System.out.println("event = " + event);
                  var value = property.get();
                  if (value.length() > 0) property.set(value.substring(0, value.length() - 1));
                }
              }
            })
        // Set position carret to max value
        .addEndAction((input, property) -> input.positionCaret(Integer.MAX_VALUE));
  }
}
