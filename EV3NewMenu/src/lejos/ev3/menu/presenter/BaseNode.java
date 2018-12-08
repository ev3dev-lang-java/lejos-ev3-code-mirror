package lejos.ev3.menu.presenter;

import java.util.ArrayList;
import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.model.Model;
import lejos.ev3.menu.model.ModelListener;
import lejos.ev3.menu.viewer.Menu;
import lejos.hardware.lcd.Image;

/**
 * A basic implementation of a menu item
 * 
 * @author Aswin Bouwmeester
 *
 */
public class BaseNode implements Node, ModelListener {

  private Image            icon;
  private List<Detail>     details            = new ArrayList<Detail>();
  private String           label;
  protected boolean        selectableDetails  = false;
  protected boolean        markedForExecution = false;
  protected String         script;
  protected boolean        isFresh        = false;
  protected String key;
  protected int selectedDetail =0;


  protected static Menu    menu;
  protected static Model   model;

  public static void setEnvironment(Model m, Menu m3) {
    model =m;
    menu =m3;
  }

  /**
   * @param control
   *          The control that provides access to the leJOS VM from this menu
   *          item
   * @param label
   *          The title of this manu item
   * @param icon
   *          The icon of this menu item
   */
  public BaseNode(String label, Image icon) {
    if (menu == null)
      throw new RuntimeException("Menu nodes can only be instantiated after the environment is set");
    this.label = label;
    this.icon = icon;
  }

  public BaseNode(String label) {
    this(label, Icons.DEFAULT);
  }


  protected void setIcon(Image icon) {
    this.icon = icon;
  }

  @Override
  public Image getIcon() {
    return icon;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public List<Detail> getDetails() {
    if (!isFresh) refresh();
    return details;
  }

  @Override
  public Node addDetail(Detail detail) {
    details.add(detail);
    if (detail.isSelectable()) {
      selectableDetails = true;
      if (selectedDetail ==-1) {
        selectedDetail = details.size()-1;
      }
        
    }
    detail.setParent(this);
    return this;
  }

  @Override
  public String toString() {
    return label;
  }

  @Override
  public void removeDetails() {
    clearDetails();
    isFresh = true;
  }

  protected void refresh() {
    isFresh = true;
    // TODO: Replace line below to a child classes that know when to set it
//    selectedDetail = -1;
  }

protected void clearDetails() {
  for (Detail detail : details)
    model.detach(detail.getKey(), detail);
  details.clear();
  selectedDetail = -1;
}

@Override
public void keyChanged(String key) {
}

@Override
public void listChanged(String list, String parameter) {
  if (list.equals(key)) 
      this.isFresh=false; 
}

@Override
public Detail getSelected() {
  if (!isFresh) refresh();
  if (selectedDetail == -1 ) return null;
  return details.get(selectedDetail);
}

@Override
public void setSelected(int index) {
  selectedDetail = index;
}

@Override
public void selectNextDetail() {
  for (int i =selectedDetail+1; i < details.size();i++) {
    if (details.get(i).isSelectable()) { 
      selectedDetail =i; 
      return;
    }
  }
}

@Override
public void selectPreviousDetail() {
  for (int i =selectedDetail-1; i >= 0 ;i--) {
    if (details.get(i).isSelectable()) { 
      selectedDetail =i; 
      return;
    }
  }
}

@Override
public boolean hasDetails() {
  if (details == null) return false;
  if (details.size()==0) return false;
  return true;
}

@Override
public int getSelectedIndex() {
  return selectedDetail;
}

@Override
public void needRefresh() {
  isFresh = false;
}

@Override
public boolean isFresh() {
  return this.isFresh;
}



}
