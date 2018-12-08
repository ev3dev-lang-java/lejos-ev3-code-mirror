package lejos.ev3.menu.viewer;

import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.ev3.menu.presenter.Detail;
import lejos.hardware.Button;

public class EditorBoolean implements Editor{
  
  public EditorBoolean(){}

  @Override
  public void edit(Detail presenter) {
    String key = presenter.getKey();
    String format = presenter.getFormat();
    String label = presenter.getLabel();
    String v = presenter.getValue();
    boolean value = Boolean.parseBoolean(v);

    TextPanel p = new TextPanel(String.format(format, key, label, value));
    p.setBorders(15);
    p.setShadow(true);
    p.setMargin(5);

    boolean old = value;
    p.setMessage(String.format(format, key, label, value));
    p.saveScreen();
    p.paint();

    
    
    while (true) {
      switch (UI.getUI()) {
      case (Button.ID_DOWN): {} 
      case (Button.ID_UP): {
        value = !value;
        p.setMessage(String.format(format, key, label, value));
        p.paint();
        break;
      }

      case (Button.ID_ENTER): {
          v = Boolean.toString(value);
          presenter.setValue(v);
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
