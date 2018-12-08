package lejos.ev3.menu.components;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.CommonLCD;
import lejos.hardware.lcd.GraphicsLCD;

public class Panel {
  public static int TOPBORDER = 1;
  public static int RIGHTBORDER = 2;
  public static int BOTTOMBORDER = 4;
  public static int LEFTBORDER = 8;
  public static int AUTOMATIC = Integer.MAX_VALUE; 
  
  static GraphicsLCD canvas = LocalEV3.get().getGraphicsLCD();
  protected int x=AUTOMATIC;
  protected int y=AUTOMATIC;
  protected int width = AUTOMATIC;
  protected int height =AUTOMATIC;
  protected int borders = 0;
  protected boolean reverse = false;
  protected int borderStyle =GraphicsLCD.SOLID;
  protected boolean shadow = false;
  protected int shadowOffset = 3;
  protected int margin = 0;
  private byte[] saveScreen ;
  
  

  /** main method is for unit testing
   * @param arg
   */
  public static void main(String[] arg) {
    Panel a = new Panel(1,1,canvas.getWidth()-2, canvas.getHeight()-2).setReverse(true);
    a.paint();
    Panel x = new Panel(2,2,canvas.getWidth()-4, canvas.getHeight()-4).setReverse(false);
    x.paint();
    Panel y = new Panel(10,10,25,25).setReverse(false).setBorders(15).setShadow(true);
    y.paint();
    Panel z = new Panel().setReverse(false).setBorders(15).setShadow(true).setWidth(60).setHeight(20);
    z.paint();
    Button.waitForAnyPress();
  }
  
  public Panel(){
  }
  
  public Panel(int x, int y, int w, int h){
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
  }
  
  public int getMargin() {
    return margin;
  }

  public Panel setMargin(int margin) {
    this.margin = margin;
    return this;
  }


  
  public void setBorderStyle(int borderStyle) {
    this.borderStyle = borderStyle;
  }

  public boolean hasShadow() {
    return shadow;
  }

  public Panel setShadow(boolean hasShadow) {
    this.shadow = hasShadow;
    return this;
  }

  public int getShadowOffset() {
    return shadowOffset;
  }

  public Panel setShadowOffset(int shadowOffset) {
    this.shadowOffset = shadowOffset;
    return this;
  }

  public int getX() {
    if (x == AUTOMATIC) 
       return calcX();
    return x;
  }

  public Panel setX(int x) {
    this.x = x;
    return this;
  }

  public int getY() {
    if (y == AUTOMATIC) 
      return calcY();
    return y;
  }

  public Panel setY(int y) {
    this.y = y;
    return this;
  }

  public int getWidth() {
    if (width == AUTOMATIC) 
      return calcWidth();
    return width;
  }

  public Panel setWidth(int width) {
    this.width = width;
    return this;
  }

  public int getHeight() {
    if (height == AUTOMATIC) 
      return calcHeight();
    return height;
  }

  public Panel setHeight(int height) {
    this.height = height;
    return this;
  }
  
  protected int getInnerX() {
    return getX() + margin + ((borders & Panel.LEFTBORDER) != 0 ? 1 : 0);
  }
  
  protected int getInnerY(){
    return getY() + margin + ((borders & Panel.TOPBORDER) != 0 ? 1 : 0);
  }
  
  protected int getInnerBottom() {
    return getBottom() -  margin -  ((borders & Panel.BOTTOMBORDER) != 0 ? 1 : 0);
  }
  
  protected int getInnerRight(){
    return getRight() - margin - ((borders & Panel.RIGHTBORDER) != 0 ? 1 : 0);
  }
  

  public int getBorders() {
    return borders;
  }

  public Panel setBorders(int borders) {
    this.borders = borders;
    return this;
  }

  public boolean isReverse() {
    return reverse;
  }

  public Panel setReverse(boolean reverse) {
    this.reverse = reverse;
    return this;
  }

  public int getBorderStyle() {
    return borderStyle;
  }
  
  public int getBottom() {
    return getY() + getHeight(); 
  }
  
  public int getRight() {
    return getX() + getWidth();
  }
  
  public int getHCenter() {
    return getX()  + getWidth()/2;
  }

  public int getVCenter() {
    return getY()  + getHeight()/2;
  }
  
  protected int calcX() {
    return  between((canvas.getWidth() - getWidth())/2, 0, canvas.getWidth());
  }

  protected int calcY() {
    return  between((canvas.getHeight() - getHeight())/2, 0, canvas.getHeight());
  }

  protected int calcWidth() {
    return between(2 * margin - ((margin !=0) ? 1 :0)  + ((borders & Panel.LEFTBORDER) != 0 ? 1 : 0) + ((borders & Panel.RIGHTBORDER) != 0 ? 1 : 0), 0, canvas.getWidth());
  }

  protected int calcHeight() {
    return between (2 * margin - ((margin !=0) ? 1 :0) + ((borders & Panel.TOPBORDER) != 0 ? 1 : 0) + ((borders & Panel.BOTTOMBORDER) != 0 ? 1 : 0), 0, canvas.getHeight());
  }
  
  public void paint() {
    canvas.setStrokeStyle(borderStyle);
    if (shadow) {
      canvas.setColor(GraphicsLCD.BLACK);
      canvas.fillRect(getX() + shadowOffset , getY() + shadowOffset , getWidth()+1, getHeight()+1);
    }

    if (reverse)
      canvas.setColor(GraphicsLCD.BLACK);
    else
      canvas.setColor(GraphicsLCD.WHITE);
    canvas.fillRect(getX() , getY() , getWidth(), getHeight());
    if (reverse)
      canvas.setColor(GraphicsLCD.WHITE);
    else
      canvas.setColor(GraphicsLCD.BLACK);
    if ((borders & 1) != 0)
      canvas.drawLine(getX() , getY() , getRight() , getY() );
    if ((borders & 2) != 0)
      canvas.drawLine(getRight(), getY() , getRight(), getBottom());
    if ((borders & 4) != 0)
      canvas.drawLine(getX() , getBottom(), getRight(),getBottom());
    if ((borders & 8) != 0)
      canvas.drawLine(getX() , getY() , getX() , getBottom());
    canvas.setColor(GraphicsLCD.BLACK);   
    canvas.setStrokeStyle(GraphicsLCD.SOLID);
  }
  


  public void clear() {
    canvas.setColor(GraphicsLCD.WHITE);
    if (shadow) {
      canvas.fillRect(getX() + shadowOffset , getY() + shadowOffset , getWidth()+1, getHeight()+1);
    }
    canvas.fillRect(getX() , getY() , getWidth()+1, getHeight()+1);
    canvas.setColor(GraphicsLCD.BLACK);
  }
  
  protected int between(int value, int lowerBound, int upperBound) {
    if (value <lowerBound) return lowerBound;
    if (value>upperBound) return upperBound;
    return value;
  }
  
  public void saveScreen() {
    byte[] disp = canvas.getDisplay();
    saveScreen = new byte[canvas.getDisplay().length];
    System.arraycopy(disp, 0, saveScreen, 0, disp.length);
  }
  
  public void restoreScreen() {
    if (saveScreen != null)
      canvas.bitBlt(saveScreen, canvas.getWidth(), canvas.getHeight(), 0, 0, 0, 0, canvas.getWidth(), canvas.getHeight(), CommonLCD.ROP_COPY);
    saveScreen = null;
  }

  



}
