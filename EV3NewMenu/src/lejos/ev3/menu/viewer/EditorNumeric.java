package lejos.ev3.menu.viewer;

import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.ev3.menu.presenter.Detail;
import lejos.hardware.Button;
import lejos.utility.Delay;

/**
 * A simple editor for numeric values that uses the up and down keys to change
 * the value
 * 
 * @author Aswin Bouwmeester
 *
 */
public class EditorNumeric implements Editor {
  int increment  = 1;
  int upperLimit = Integer.MAX_VALUE;
  int lowerLimit = Integer.MIN_VALUE;

  public EditorNumeric() {
  }

  @Override
  public void edit(Detail presenter) {
    String key = presenter.getKey();
    String format = presenter.getFormat();
    String label = presenter.getLabel();
    String v = presenter.getValue();
    int value;
    try {
      value = Integer.parseInt(v);
    }
    catch(Exception e) {
      value=0;
    }
    TextPanel p = new TextPanel(String.format(format, key, label, value));
    p.setBorders(15);
    p.setShadow(true);
    p.setMargin(5);

    int old = value;
    p.setMessage(String.format(format, key, label, value));
    p.saveScreen();
    p.paint();

    
    while (true) {

      switch (UI.getUI()) {
      case (Button.ID_UP): {
        if (value + increment <= upperLimit)
          value += increment;
        p.setMessage(String.format(format, key, label, value));
        p.paint();
        break;
      }
      case (Button.ID_DOWN): {
        if (value - increment >= lowerLimit)
          value -= increment;
        p.setMessage(String.format(format, key, label, value));
        p.paint();
        break;
      }
      case (Button.ID_ENTER): {
        if (value != old) {
          v = Integer.toString(value);
          presenter.setValue(v);
        }
        p.restoreScreen();
        return;
      }
      case (Button.ID_ESCAPE): {
        p.restoreScreen();
        return;
      }
      }
    }
  }

}
