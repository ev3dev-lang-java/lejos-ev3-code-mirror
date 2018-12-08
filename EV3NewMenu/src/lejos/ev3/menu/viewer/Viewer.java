package lejos.ev3.menu.viewer;

import java.util.List;

import lejos.ev3.menu.components.Fonts;
import lejos.ev3.menu.components.Panel;
import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

/** Simple file viewer for text files
 * @author Aswin Bouwmeester
 *
 */
public class Viewer {
  private static GraphicsLCD canvas = LocalEV3.get().getGraphicsLCD();
  private static Panel viewer; 
  private static TextPanel viewLine;
  static {
    viewer = new Panel(0,0,canvas.getWidth(), canvas.getHeight());
    viewLine = new TextPanel("", 0,0);
    viewLine.setWidth(canvas.getWidth());
    viewLine.setFont(Fonts.Courier12);
  }
  
  private Viewer(){};
  
public static void view(List<String> lines) {    
    int top = 0;
    int start = 0;
    int longest = 0; 
    int oldstart =-1;
    int oldtop =-1;
    int n = viewer.getHeight() / viewLine.getHeight();
    int l = viewLine.getWidth() / viewLine.getFont().width;
    viewer.saveScreen();
    while (true) {
      if (oldstart != start | oldtop != top) {
        viewer.clear();
      for (int i =top ; i< n +top && i < lines.size() ; i ++) {
        longest = Math.max(longest, lines.get(i).length());
        String line = lines.get(i);
        if (start < line.length()) {
          viewLine.setMessage(line.substring(start));
        }
        else {
          viewLine.setMessage("");
        }
        int y = (i-top) * viewLine.getHeight() + viewer.getY();
        viewLine.setY(y);
        viewLine.paint();
      }
      oldstart = start ;
      oldtop = top;
      }
      int but = UI.getUI();
      if ((but & Button.ID_LEFT) > 0) {if (start > 0) start --;}
      if ((but & Button.ID_RIGHT) > 0) {if (start < longest-l) start ++;}
      if ((but & Button.ID_UP) > 0) {if (top > 0) top --;}
      if ((but & Button.ID_DOWN) > 0) {if (top < lines.size()-n) top ++;}
      if ((but & Button.ID_ESCAPE) > 0) { 
        viewer.restoreScreen();
        return;
       }
      
    }
  }

}
