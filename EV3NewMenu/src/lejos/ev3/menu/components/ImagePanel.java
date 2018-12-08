package lejos.ev3.menu.components;

import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

public class ImagePanel extends Panel {
  // TODO: Make size pixel perfect  

  protected Image image;
  protected int   imageAlign = GraphicsLCD.LEFT + GraphicsLCD.TOP;

  public static void main(String[] arg) {
    ImagePanel a = new ImagePanel(Icons.HOUR_GLASS);
    a.setX(10).setY(10).setReverse(true).setBorders(15).setShadow(false);
    a.paint();
    Button.waitForAnyPress();
  }
  

  public ImagePanel() {
    super();
  }
  
  public ImagePanel(Image i) {
    super();
    setImage(i);
  }

  @Override
  protected int calcWidth() {
    return super.calcWidth() + image.getWidth() ;
  }
  
  @Override
  protected int calcHeight() {
    return super.calcHeight() + image.getHeight() ;
  }

  public void paint() {
    super.paint();
    int w = Math.min(image.getWidth(), this.getWidth());
    int h = Math.min(image.getHeight(), this.getHeight());
    int rop = GraphicsLCD.ROP_COPY;
    if (reverse)
      rop = GraphicsLCD.ROP_COPYINVERTED;
    canvas.drawRegionRop(image, 0, 0, w, h, getInnerX() , getInnerY() , imageAlign, rop);
  }

  public ImagePanel setImage(Image i) {
    this.image = i;
    return this;
  }

}
