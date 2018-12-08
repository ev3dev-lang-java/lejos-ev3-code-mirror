package lejos.ev3.menu.viewer;

import java.util.List;

import lejos.ev3.menu.model.Model;
import lejos.ev3.menu.presenter.Detail;
import lejos.ev3.menu.presenter.Node;

/**
 * Represents a menu for leJOS
 * 
 * @author Aswin Bouwmeester
 *
 */
public interface Menu {
  
  public void runMenu(Node menu);
  
  public void runMenu(List<Node> menu);


  /**
   * Returns true is the select method on a detail is being executed and ths
   * detail has instructed the menu to suspend;
   * 
   * @return
   */
  public boolean isSuspended();

  /**
   * Instructs the menu to suspend. While suspended the menu does not write to
   * the display. It waits for the resume method or the "Kill program" key
   * combination to continue running.
   */
  public void suspendMenu();

  /**
   * Instructs the menu to continue running
   */
  public void resumeMenu();


  public boolean dialog(String text, int buttons);

  public Detail selectFromList(Node list );
  
  public void progress(String text);

  public void setEnvironment(Model model);

}
