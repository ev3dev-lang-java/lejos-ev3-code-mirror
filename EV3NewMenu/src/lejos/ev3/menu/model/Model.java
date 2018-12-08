package lejos.ev3.menu.model;

import java.util.List;

import lejos.ev3.menu.viewer.WaitScreen;

/** A Model manages a related group of system settings and related system functions.
 *<br>
 *Models have three basic functions:
 *<li> managing system settings with the getSetting and setSetting methods.</li>
 *<li> executing system related functions with the execute function.</li>
 *<li> listing system items (like files, BT devices etc) with the list function.</li>
 * @author Aswin Bouwmeester
 *
 */
public interface Model {
  
  /** Subscribes an object as a listener to the model
   * @param listener The ID of the object to register
   */
  public void attach(String key, ModelListener listener);
  
  /** Unsubscribes an object as a listener
   * @param listener
   */
  public void detach(String key, ModelListener listener);
  
  /** Test if the model controls a setting identified by the key
   * @param key
   * @return
   */
  public boolean hasSetting(String key);
  
  /** Test if the model is able to execute the command identified by the parameter
   * @param command
   * @return
   */
  public boolean canExecute(String command);
  
  /** Test if the model is able to generate a list identified by the parameter
   * @param list
   * @return
   */
  public boolean canList(String list);
  
  /** Gets a setting from the model
   * @param key
   * @param defaultValue
   * @return
   */
  public String getSetting(String key, String defaultValue);
  
  /** Updates a setting to the model. The model will permanently store the new value
   * @param key
   * @param value
   */
  public void setSetting(String key, String value);
  
  /** Executes a command on the target. 
   * @param command
   * @param target
   * @return The stack trace if the command fails. null if the command succeeds.
   */
  public List<String> execute(String command, String target, String... arguments);
  
  /** Returns a list of values 
   * @param list
   * @param parameter
   * @return
   */
  public List<String> getList(String list, String parameter);

  void attach(WaitScreen listener);

  void detach(WaitScreen listener);
  
  void initialize();
  
  void terminate();

}
