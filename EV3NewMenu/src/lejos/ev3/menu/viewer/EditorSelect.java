package lejos.ev3.menu.viewer;

import java.util.ArrayList;
import java.util.List;

import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.ev3.menu.presenter.Detail;
import lejos.hardware.Button;

public class EditorSelect implements Editor {
  List<String> choices = new ArrayList<String>();

  public EditorSelect() {
    choices.add("Y");
    choices.add("N");
  }

  @Override
  public void edit(Detail presenter) {
    String key = presenter.getKey();
    String format = presenter.getFormat();
    String label = presenter.getLabel();
    String value = presenter.getValue();
    int current = choices.indexOf(value);
    int max = choices.size() - 1;
    int initial = current;

    TextPanel p = new TextPanel(String.format(format, key, label, value));
    p.setBorders(15);
    p.setShadow(true);
    p.setMargin(5);

    p.saveScreen();
    while (true) {
      p.setMessage(String.format(format, key, label, choices.get(current)));
      p.paint();
      switch (UI.getUI()) {
      case (Button.ID_UP): {
        if (current > 0)
          current--;
        else
          current = max;
        break;
      }
      case (Button.ID_DOWN): {
        if (current < max)
          current++;
        else
          current = 0;
        break;
      }
      case (Button.ID_ENTER): {
        if (current != initial) {
          presenter.setValue(choices.get(current));
          p.restoreScreen();
        }
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
