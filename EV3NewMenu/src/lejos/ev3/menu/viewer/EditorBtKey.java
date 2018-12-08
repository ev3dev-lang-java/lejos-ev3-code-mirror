package lejos.ev3.menu.viewer;

import lejos.ev3.menu.components.Panel;
import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.ev3.menu.presenter.Detail;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

public class EditorBtKey implements Editor{
  
  public static void main(String[] args) {
    editKey("1234","Unit testing");
  }

  
  public static String editKey(String key, String label){
    int selected =0;
    TextPanel p = new TextPanel(new String[] {"Enter key for:", label,"",""});
    p.setBorders(15);
    p.setShadow(true);
    p.setHeight(70);
    p.setMargin(8);
    int xOffset = p.getX() + 15;
    int yOffset= p.getY() + 2 * p.getFont().getHeight() + 12;
    Panel a = new Panel(xOffset, yOffset, 100, 25);
    String oldValue = key;
    int len = oldValue.length();
    char[] pin = new char[len];
    TextPanel[] n = new TextPanel[len];
    for (int i = 0; i < len; i++) {
        pin[i] = oldValue.charAt(i);
        n[i] = new TextPanel(oldValue.substring(i, i), xOffset, yOffset);
        n[i].setMargin(4);
        xOffset += 20;
    }
    p.saveScreen();
    p.paint();
    int last =0;
    int x=0;
    GraphicsLCD    canvas          = LocalEV3.get().getGraphicsLCD();
    while (true){
      x++;
      if (x>170) x=0;
      canvas.setColor(canvas.BLACK);
      canvas.drawLine(0, 0, x, 0);
      a.clear();
      for (int i=0;i<len;i++) {
        n[i].setBorders(i==selected ? 15 : 0);
        n[i].setMessage(String.copyValueOf(pin, i, 1));
        n[i].paint();
      }
      switch (UI.getUI()) {
      case Button.ID_ESCAPE: {p.restoreScreen(); return "";}
      case Button.ID_ENTER: {p.restoreScreen(); return String.copyValueOf(pin);}
      case Button.ID_LEFT: { if (selected > 0) selected--; else selected = len; break;}
      case Button.ID_RIGHT: { if (selected < len) selected++; else selected =0; break;}
      case Button.ID_UP: { pin[selected]++; if (pin[selected] > '9') pin[selected]='0' ; break;}
      case Button.ID_DOWN: { pin[selected]--; if (pin[selected] < '0') pin[selected]='9' ; break;}
      }
      
  }

}

  @Override
  public void edit(Detail detail) {
    String newValue = editKey(detail.getValue(),"this brick");
    if (newValue.isEmpty()) return;
    detail.setValue(newValue);
    return;
  }
    
}
