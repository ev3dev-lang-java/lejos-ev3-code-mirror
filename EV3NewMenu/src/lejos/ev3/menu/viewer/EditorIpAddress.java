package lejos.ev3.menu.viewer;

import lejos.ev3.menu.components.Panel;
import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.ev3.menu.presenter.Detail;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

public class EditorIpAddress implements Editor{
  
  public static void main(String[] args) {
    editKey("192.0.0.10","Unit testing");
  }

  
  public static String editKey(String key, String label){
    int selected =0;
    TextPanel p = new TextPanel(new String[] {"Enter IP for:", label,"",""});
    p.setBorders(15);
    p.setShadow(true);
    p.setHeight(70);
    p.setMargin(6);
    p.setWidth(168);
    int xOffset = p.getX() + 3;
    int yOffset= p.getY() + 2 * p.getFont().getHeight() + 12;
    Panel a = new Panel(xOffset, yOffset, 164, 25);
    String oldValue = key;
    String[] parts = oldValue.split("\\.");
    int len = parts.length;
    int[] pin =new int[len];
    TextPanel[] n = new TextPanel[len];
    TextPanel[] m = new TextPanel[len];
    for (int i = 0; i < len; i++) {
        pin[i] = Integer.parseInt(parts[i]);
        n[i] = new TextPanel(String.format("%03d", pin[i]), xOffset, yOffset);
        n[i].setMargin(1);
        xOffset += n[i].getWidth()+3;
        m[i] = new TextPanel(".", xOffset, yOffset);
        xOffset += m[i].getWidth()+2;
    }
    p.saveScreen();
    p.paint();
    int last =0;
    int x=0;
    GraphicsLCD    canvas          = LocalEV3.get().getGraphicsLCD();
    while (true){
      a.clear();
      for (int i=0;i<len;i++) {
        n[i].setBorders(i==selected ? 15 : 0);
        n[i].setMessage(String.format("%03d", pin[i]));
        n[i].paint();
        if (i < len -1)  m[i].paint();
      }
      switch (UI.getUI()) {
      case Button.ID_ESCAPE: {p.restoreScreen(); return "";}
      case Button.ID_ENTER: {
        StringBuilder sb =new StringBuilder(15);
        for (int i=0;i<len;i++) {
          sb.append(String.format("%d", pin[i]));
          if (i < len -1) sb.append('.');
        }
        p.restoreScreen();
        return sb.toString();
      }
      case Button.ID_LEFT: { if (selected > 0) selected--; else selected =len; break;}
      case Button.ID_RIGHT: { if (selected < len) selected++; else selected =0 ; break;}
      case Button.ID_UP: { pin[selected]++; if (pin[selected] > 255) pin[selected]=0 ; break;}
      case Button.ID_DOWN: { pin[selected]--; if (pin[selected] < 0) pin[selected]=255 ; break;}
      }
      
  }

}

  @Override
  public void edit(Detail detail) {
    String value = detail.getValue();
    String newValue = editKey(detail.getValue(),detail.getLabel());
    if (newValue.isEmpty()) return;
    detail.setValue(newValue);
    return;
  }
    
}
