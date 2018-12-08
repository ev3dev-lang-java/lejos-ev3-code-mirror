package lejos.ev3.menu.viewer;

import lejos.hardware.lcd.Image;

/** Interface for listeners that present progress messages on the screen 
 * @author Aswin Bouwmeester
 *
 */
public interface WaitScreen {
  
  public void openMsgBox();
  
  public void closeMsgBox();
  
  public void msgBoxSetText(String text);

  public void msgBoxSetIcon(Image icon);

}
