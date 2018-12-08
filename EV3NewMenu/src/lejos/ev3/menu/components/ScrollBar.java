package lejos.ev3.menu.components;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * A simple scrollbar used to aid in navigation
 * 
 * @author Aswin Bouwmeester
 *
 */
public class ScrollBar {
  GraphicsLCD              canvas;
  private final int        x;
  private final int        y;
  private final int        height;
  private final int        rows;
  private final int        visibleRows;
  private int              toprow;
  private final static int W = 6;
  private final int        barHeight;
  private final float      step;
  private final int        thumpHeight;

  public static void main(String[] args) {
    int t = 0;
    ScrollBar test = new ScrollBar(LocalEV3.get().getGraphicsLCD(), 10, 10, 52, 27, 5, t);
    while (true) {
      test.setToprow(t);
      test.canvas.clear();
      test.canvas.drawString(String.format("%s", t), 50, 50, 0);
      test.paint();
      switch (Button.waitForAnyPress()) {
      case Button.ID_DOWN:
        t++;
        break;
      case Button.ID_UP:
        t--;
        break;
      case Button.ID_ESCAPE:
        return;
      }
    }

  }

  public ScrollBar(GraphicsLCD canvas, int x, int y, int height, int rows, int visibleRows, int toprow) {
    this.canvas = canvas;
    this.x = x;
    this.y = y;
    this.height = height;
    this.rows = rows;
    this.visibleRows = visibleRows;
    this.toprow = toprow;
    this.barHeight = height - 2 * W - 4;
    this.step = (float) (barHeight) / (float) (rows);
    this.thumpHeight = (int) Math.round(visibleRows * step);
  }

  public void setToprow(int toprow) {
    if (toprow < 0 | toprow > rows - visibleRows)
      throw new RuntimeException("Invalid toprow for ScrollBar");
    this.toprow = toprow;
  }

  public int getToprow() {
    return toprow;
  }

  public void paint() {
    canvas.setColor(GraphicsLCD.WHITE);
    canvas.fillRect(x, y, W * 2 * 1, height + 1);
    canvas.setColor(GraphicsLCD.BLACK);

    canvas.drawLine(x + W, y, x, y + W);
    canvas.drawLine(x + W, y, x + W * 2, y + W);
    canvas.drawLine(x, y + W, x + W * 2, y + W);

    canvas.drawRect(x, y + W + 2, W * 2, barHeight);

    canvas.drawLine(x + W, y + height, x, y - W + height);
    canvas.drawLine(x + W, y + height, x + W * 2, y - W + height);
    canvas.drawLine(x, y - W + height, x + W * 2, y - W + height);

    int top = y + W + 2 + (int) Math.round(toprow * step);

    if (step >= 2)
      canvas.fillRect(x, top, W * 2, thumpHeight);
    else
      canvas.drawLine(x, top, x + W * 2, top);

  }

}
