package lejos.ev3.menu.components;

import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

public class TextPanel extends Panel {
  private String[] message;
  private Font     font      = Fonts.Courier13;
  private int      spacing   = 2;
  private int      textAlign = GraphicsLCD.LEFT + GraphicsLCD.TOP;

  /**
   * main is for unit test
   * 
   * @param arg
   */
  public static void main(String[] arg) {
    TextPanel a = new TextPanel("WWqpqpqWW\nBBBpqpXXX");
    a.setX(5);
    a.setY(45);
    a.setWidth(110);
    a.setHeight(32);
    a.setReverse(true);
    a.setMargin(0);
    a.setBorders(0);
    a.setSpacing(1);
    a.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.VCENTER);
    a.paint();
    
    TextPanel b = new TextPanel("WWqpqpqWW\nBBBpqpXXX");
    b.setX(5);
    b.setY(5);
    b.setReverse(false);
    b.setMargin(1);
    b.setBorders(15);
    b.setSpacing(1);
    b.setTextAlign(GraphicsLCD.LEFT + GraphicsLCD.TOP);
    b.paint();
    
    
    Button.waitForAnyPress();
//    a.clear();
//    a = new TextPanel("WWqpqpqWW", 10, 10);
//    a.setWidth(60);
//    a.setHeight(20);
//    a.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.VCENTER);
//    a.setShadow(true);
//    a.setShadowOffset(7);
//    a.setBorders(15);
//    a.paint();
//    Button.waitForAnyPress();
  }

  public TextPanel(String text) {
    this(text.split("\n"));
  }

  public TextPanel(String lines, int x, int y) {
    this(lines.split("\n"), x, y);
  }



  public TextPanel(String[] lines, int x, int y) {
    this(lines);
    this.x = x; 
    this.y = y;
  }

  public TextPanel(String[] text) {
    this.message = text;
  }

  public String[] getMessage() {
    return message;
  }

  public TextPanel setMessage(String[] message) {
    this.message = message;
    return this;
  }

  public Font getFont() {
    return font;
  }

  public TextPanel setFont(Font font) {
    this.font = font;
    return this;
  }



  public int getSpacing() {
    return spacing;
  }

  public TextPanel setSpacing(int spacing) {
    this.spacing = spacing;
    return this;
  }

  @Override 
  protected int calcHeight() {
    return  between( super.calcHeight() + calctextHeight(), 0, canvas.getHeight()) ;
  }
  
  @Override
  protected int calcWidth() {
    if (message == null)
      return super.calcWidth();
    else {
      int w = 0;
      for (String line : message)
        w = Math.max(w, font.stringWidth(line));
      return between(super.calcWidth() + w, 0, canvas.getWidth()) ;
    }
    
  }

  int calctextHeight() {
    if (message == null)
      return 0;
    int height = message.length * font.height + Math.max(message.length-1, 0) * spacing;
    return height;
  }


  public void paint() {
    int xx = 0;
    int yy = 0;
    int align = 0;
    super.paint();
    if (message == null)
      return;
    canvas.setFont(font);
    if (reverse)
      canvas.setColor(GraphicsLCD.WHITE);
    else
      canvas.setColor(GraphicsLCD.BLACK);
    if ((textAlign & GraphicsLCD.LEFT) != 0) {
      xx = getInnerX();
      align = GraphicsLCD.LEFT;
    }
    if ((textAlign & GraphicsLCD.RIGHT) != 0) {
      xx = getInnerRight();
      align = GraphicsLCD.RIGHT;
    }
    if ((textAlign & GraphicsLCD.HCENTER) != 0) {
      xx = getHCenter();
      align = GraphicsLCD.HCENTER;
    }
    if ((textAlign & GraphicsLCD.TOP) != 0) {
      yy = getInnerY();
    }
    if ((textAlign & GraphicsLCD.BOTTOM) != 0) {
      yy = getInnerBottom() - calctextHeight() ;
    }
    if ((textAlign & GraphicsLCD.VCENTER) != 0) {
      yy = this.getVCenter() - calctextHeight() / 2 ;
    }
    align += GraphicsLCD.TOP;
    // TODO: take margins and borders into account in max width
    int maxWidth = this.getWidth()/font.width + 1;
    for (String line : message) {
      if (line.length() > maxWidth ) line = line.substring(0, maxWidth);
      canvas.drawString(line, xx, yy, align);
      yy += font.getHeight() + spacing;
    }
  }

  public int getTextAlign() {
    return textAlign;
  }

  public void setTextAlign(int textAlign) {
    if ((textAlign & GraphicsLCD.BASELINE) != 0) {
      throw new RuntimeException("TextPanel does not support BASELINE alignment");
    } else {
      this.textAlign = textAlign;
    }
  }

  public TextPanel setMessage(String message) {
    return setMessage(message.split("/n"));
    
  }

}
