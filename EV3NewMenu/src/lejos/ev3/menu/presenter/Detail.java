package lejos.ev3.menu.presenter;

import java.util.Map;

import lejos.ev3.menu.model.ModelListener;


/** The Detail is a menu component. A dDetail normally is displayed as a single line in a menu Node. A detail corresponds to a system setting. 
 * The setting is identified by the key field of a Detail.
 * @author Aswin Bouwmeester
 *
 */
public interface Detail extends  ModelListener{
  /**
   * Returns true it the detail can be selected by the user
   * 
   * @return
   */
  public boolean isSelectable();

  /**
   * Called after the user has selected the Detail. Functionality depends on
   * the type of detail.
   * 
   * @param menu
   */
  public void select();

  /**
   * To be called if the Detail should be refreshed as the data it displays has
   * changed
   * 
   */
  //public void needRefresh();

  /**
   * Returns the text to display in the menu
   * 
   * @return
   */
  public String toString();
  
  /** Sets the value of a detail. The detail will call the model to update the value
   * @param value
   */
  public void setValue(String value);
  
  /** Returns the value of the detail
   * @return
   */
  public String getValue();
  
  /** Sets the label of a detail. The label is used for display purposes.
   * @param label
   */
  public void setLabel(String label);
  
  public String getLabel();
  
  public void setKey(String key);
  
  public String getKey();
  
  /** Sets the format string of the detail. The format string is used by the detail to format the value for display purposes. <br>
   * The format string can make use of three positional parameters. The first parameter is the key of the detail, the second is the label and the third is the value.
   * For example, the format string "%2$s: %3$s" will generate a display string of "label: value".
   * @param format
   */
  public void setFormat(String format);
  
  public String getFormat();
  
  public boolean isFresh();
  
  /**
   * This method marks the refresh flag of a detail. A detail iwill actually refresh when its value is queried. 
   */
  public void needRefresh();
  
  /** This method instructs the detail to use a special description to display certain values.<br>
   * For example a value of "" can be displayed as "not connected" by calling addSpecialValue("", "not connected"). 
   * @param value
   * @param label
   * @return
   */
  public Detail addSpecialValue(String value, String label);

  public Map<String, String> getSpecials();
  
  public boolean isAutoFefresh();

  public void setParent(Node menuItem);


}
