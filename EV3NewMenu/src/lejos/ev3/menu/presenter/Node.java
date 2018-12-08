package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.hardware.lcd.Image;

/**
 * The Node is the fundamental building block of the visible part of the lejos
 * EV3 menu structure. It represents one screen in the menu. A node consists of an
 * Icon, a label and a number of details.
 * 
 * @author Aswin Bouwmeester
 *
 */
public interface Node {

  /**
   * Returns the icon for this node
   * 
   * @return
   */
  public Image getIcon();

  /**
   * Returns the label for this nod
   * 
   * @return
   */
  public String getLabel();

  /**
   * Adds a Detail to this node
   * 
   * @param detail
   * @return this
   */
  public Node addDetail(Detail detail);
  
  /**
   * Removes all Details
   */
  public void removeDetails();

  /**
   * Returns a list of all the details of this node
   * 
   * @return
   */
  public List<Detail> getDetails();

  
  /** Returns the id of the selected detail, or null if no detail is selected
   * @return
   */
  public Detail getSelected();
  
  /** Marks a detail as selected
   * @param index
   */
  public void setSelected(int index);

  /** Marks the first selectable detail after the currently selected detail as selected. 
   * 
   */
  void selectNextDetail();

  /** Marks the first selectable detail before the currently selected detail as selected. 
   * 
   */
  void selectPreviousDetail();

  /** Returns true if the node has details
   * @return
   */
  public boolean hasDetails();

  /** Retuens the index number of the selected detail
   * @return
   */
  public int getSelectedIndex();
  
  /** Marks this node for refresh. It will refresh its details as soon as the node is queried for its details.
   * 
   */
  public void needRefresh();

  /** Returns false if the Node is marked for a refresh
   * @return
   */
  public boolean isFresh();
  



}
