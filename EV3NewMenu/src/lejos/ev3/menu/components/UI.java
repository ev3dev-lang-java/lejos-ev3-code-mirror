package lejos.ev3.menu.components;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.utility.Delay;

/** Class to capture the hardware buttons using the getUI method. The class treats buttons in two different ways depending on the button. 
 * The getUI method returns the button ID after a press and release of a button. 
 * In addition to this behavior the getUI method also returns the button ID after a "navigation" button has been pressed for a certain amount of time. 
 * This behavior is limited to navigation buttons, by default these are the Up, DOWN, LEFT and RIGHT buttons. The default can be overruled by a parameter to the getUI method. 
 * This behavior makes it possible to react to a prolonged key press. 
 * @author Aswin Bouwmeester
 *
 */
public class UI {
  private static Keys keys = BrickFinder.getLocal().getKeys();
  private static int firstTimeOut = 500;
  private static int subsequentTimeOuts = 200;
  private static int lastButton =0;
  private static long startTime =0;
  private static long lastTime = 0; 
  private static long delay = 10; 
  private static int navButtons = Keys.ID_DOWN + Keys.ID_UP + Keys.ID_LEFT + Keys.ID_RIGHT;
  
  /** Gets user input
   * Enter and Escape are returned upon button release
   * navigation keys are returned immediately and after time outs
   * @return
   */
  public static int getUI() {
    return getUI(navButtons, firstTimeOut,subsequentTimeOuts );
  }
  
  
  public static int getUI(int navButtons, int firstTimeOut, int subsequentTimeOuts) {
    while (true) {
      int button = keys.getButtons();
      long now = System.currentTimeMillis();
      
      // if an enter or escape key is released return the most recently pressed key
      if (button == 0 && isIn(lastButton, Keys.ID_ALL -navButtons) /*  (lastButton == Keys.ID_ENTER || lastButton == Keys.ID_ESCAPE)*/) {
        int previous = lastButton;
        lastButton = 0;
        return previous;
      }
      
      // if a navigation key has just been pressed start the timers used for repeat function and return the pressed key immediately
      if (lastButton ==0 && isIn(button, navButtons)) {
        startTime = now;
        lastButton = button;
        return button;
      }
      
      // if a navigation key is pressed for the duration of the initial timeout return the pressed key;
      if (isIn(button, navButtons) && button ==lastButton && lastTime ==0 && now > startTime + firstTimeOut) {
        lastTime = now;
        return button;
      }
      
      // if a navigation key is pressed, the initial time out has expied an a subsequent timeout has expired return the pressed key;
      if (isIn(button, navButtons) && button ==lastButton && lastTime !=0 && now > startTime + subsequentTimeOuts) {
        lastTime = now;
        return button;
      }
      
      // if no navigation button is pressed clear all the timers;
      if (!isIn(button, navButtons)) {
        startTime =0;
        lastTime = 0;
      }
      
      // remember last button and wait a bit;
      lastButton = button;
      Delay.msDelay(delay);
      }
    
  }
  
  /** Gets user input, but only for selected buttons after release;
   * @param buttons
   * @return
   */
  static int waitForPressAndRelease(int buttons) {
    int last = 0;
    while (true) {
      int button = keys.getButtons();
      if (button ==0 && last != 0) return last;
      if ((button & buttons) != 0 ) last = button;
      Delay.msDelay(delay);
    }
  }
    

  private static boolean isIn(int button, int buttons) {
      if ((button & buttons) !=0) return true;
      return false;
    }

}
