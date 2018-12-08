package lejos.ev3.menu.model;

/** Interface for objects that want to register with a model as a listener to listen to changes in the model
 * @author Aswin Bouwmeester
 *
 */
public interface ModelListener {
  
  /** Model calls this method on all listeners of the model whenever a setting is changed
   * @param key The ID of the setting
   * @param value The new value of the setting
   */
  public void keyChanged(String key);
  
  /** Model calls this method on all listeners of the model whenever a list is changed
   * @param list The ID indicating the type of the list
   * @param parameter The parameter indicating the contents of the list
   */
  public void listChanged(String list, String parameter);

}
