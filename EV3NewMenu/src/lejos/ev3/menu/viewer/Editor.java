package lejos.ev3.menu.viewer;

import lejos.ev3.menu.presenter.Detail;

/**
 * Defines editors to modify properties or settings of the leJOS VM. Editors
 * work in conjunction with DetailValue's
 * 
 * @author Aswin Bouwmeester
 * @param <T>
 *
 */
public interface Editor {

  public void  edit(Detail detail);


}
