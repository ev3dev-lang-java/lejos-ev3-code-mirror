package lejos.ev3.menu.components;

import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

public class CompoundPanel extends Panel {
  // TODO: have text centered when the panel has a specified width
  ImagePanel image   = null;
  TextPanel  text    = null;
  int        spacing = 2;

  /**
   * Main is here for unit test only
   * 
   * @param arrg
   */
  public static void main(String[] arrg) {
//    CompoundPanel a = new CompoundPanel(Icons.HOUR_GLASS, "Wait \na \nsecond...");
//    a.setBorders(15);
//    a.setShadow(true);
//    a.setMargin(1);
//    a.setWidth(170);
//    a.setHeight(90);
//    a.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.VCENTER);
//    a.paint();
    
    CompoundPanel title = new CompoundPanel(Icons.EYE, "Title");
    title.setX(0);
    title.setY(13);
    title.setWidth(canvas.getWidth());
    title.setFont(Fonts.Courier17);
    title.setBorders(15);
    title.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.VCENTER);
    title.paint();
    Button.waitForAnyPress();
  }

  public CompoundPanel(Image i, String[] message) {
    super();
    image = new ImagePanel(i);
    text = new TextPanel(message);
    image.setMargin(0);
    text.setMargin(0);
    image.setBorders(0);
    text.setBorders(0);
    
  }

  public CompoundPanel(Image i, String message) {
    this(i, message.split("\n"));
  }

  public CompoundPanel(Image i, String[] message, int x, int y) {
    this(i, message);
    setX(x);
    setY(y);
  }

  public CompoundPanel(Image i, String message, int x, int y) {
    this(i, message.split("/n"), x, y);
  }
  


  @Override
  protected int calcX() {
    return Math.max((canvas.getWidth() - getWidth()) / 2, 0);
  }

  @Override
  protected int calcY() {
    return Math.max((canvas.getHeight() - getHeight()) / 2, 0);
  }

  @Override
  protected int calcWidth() {
    return image.getWidth() + text.getWidth() + spacing + 2 * margin +4;
  }

  @Override
  protected int calcHeight() {
    return Math.max(text.getHeight(), image.getHeight()) + 2 * margin + 2;
  }

  protected void setSubPanels() {
    image.setMargin(0);
    text.setMargin(0);
    image.setBorders(0);
    text.setBorders(0);
  }

  @Override
  public CompoundPanel setX(int x) {
    super.setX(x);
    return this;
  }

  @Override
  public CompoundPanel setY(int y) {
    super.setY(y);
    return this;
  }
  
  @Override
  public CompoundPanel setWidth(int w) {
    super.setWidth(w);
    if (w==AUTOMATIC) {
      text.setWidth(AUTOMATIC);
    }
    else {
      text.setWidth(getInnerRight() - getInnerX() - image.getWidth() - spacing);
    }
    return this;
  }  

  @Override
  public void paint() {
    image.setX(getInnerX()+2 );
    image.setY(getVCenter() - image.getHeight() / 2);
    text.setX(image.getRight() + spacing);
    text.setY(getInnerY()+1);
    text.setHeight(getInnerBottom() - getInnerY());
    super.paint();
    image.paint();
    text.paint();
  }
  
  @Override 
  public CompoundPanel setReverse(boolean r) {
    super.setReverse(r);
    image.setReverse(r);
    text.setReverse(r);
    return this;
  }

  public CompoundPanel setMessage(String label) {
    text.setMessage(label);
    return this;
  }

  public CompoundPanel setIcon(Image icon) {
    image.setImage(icon);
    return this;
    
  }

  public CompoundPanel setTextAlign(int align) {
    text.setTextAlign(align);
    return this;
  }

  public CompoundPanel setFont(Font font) {
    text.setFont(font);
    return this;
  }

}
