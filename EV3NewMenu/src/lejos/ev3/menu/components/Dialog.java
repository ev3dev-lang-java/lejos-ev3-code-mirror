package lejos.ev3.menu.components;

import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.lcd.GraphicsLCD;

public class Dialog {
  public static int         BUTTON_YES = 1;
  public static int         BUTTON_NO  = 2;

  private static Panel      window     = new Panel();               ;
  private static TextPanel  message    = new TextPanel("");
  private static ImagePanel ok         = new ImagePanel(Icons.YES);
  private static ImagePanel cancel     = new ImagePanel(Icons.NO);
  static {
    window.setBorders(15);
    window.setShadow(true);
    window.setMargin(4);
    message.setFont(Fonts.Courier12);
    message.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.TOP);
    ok.setBorders(15);
    cancel.setBorders(15);
  }

  private Dialog() {
  };

  public static void main(String[] args) {
    display("Hello world/nAre you listening?", 3);
  }

  public static boolean display(String text, int buttons) {
    window.saveScreen();
    message.setMessage(text);
    window.setWidth(Math.max(60, message.getWidth()) + 2 * window.getMargin());
    window.setHeight(message.getHeight() + Math.max(ok.getHeight(), cancel.getHeight()) + 4 * window.getMargin());
    message.setX(window.getInnerX());
    message.setY(window.getInnerY());

    ok.setY(window.getInnerBottom() - ok.getHeight() - window.getMargin());
    if (buttons == BUTTON_YES)
      ok.setX(window.getHCenter() - ok.getWidth() / 2);
    else
      ok.setX(window.getHCenter() - ok.getWidth() - window.getMargin());

    cancel.setY(window.getInnerBottom() - cancel.getHeight() - window.getMargin());
    if (buttons == BUTTON_NO)
      cancel.setX(window.getHCenter() - cancel.getWidth() / 2);
    else
      cancel.setX(window.getHCenter() + window.getMargin());

    
    window.paint();
    message.paint();
    if ((buttons & BUTTON_YES) != 0)
      ok.paint();
    if ((buttons & BUTTON_NO) != 0)
      cancel.paint();

      int id = UI.waitForPressAndRelease(Keys.ID_ENTER + Keys.ID_ESCAPE);
      window.restoreScreen();
      switch (id) {
      case Button.ID_ENTER:
        return true;
      default:
        return false;
    }
  }

}
