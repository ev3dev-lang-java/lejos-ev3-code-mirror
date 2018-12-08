package lejos.ev3.menu.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lejos.ev3.menu.model.Model;
import lejos.ev3.menu.viewer.Menu;
import lejos.ev3.menu.viewer.Viewer;

/** Basic implementation of a menu Detail. <br>
 * @author Aswin Bouwmeester
 *
 */
public class BaseDetail implements Detail{
  protected String key;
  protected String label;
  protected String format;
  protected String value;
  protected boolean selectable = false;
  protected boolean isFresh = false;
  protected Map<String, String> specials = new HashMap<String, String>();
  protected String defaultValue;
  protected static Menu menu;
  protected static Model model;
  protected boolean autoRefresh =false;
  protected Node parent;

  
  public static void setEnvironment( Model m,  Menu m3) {
    model =m;
    menu =m3;
  }
  
  
  public BaseDetail(String key, String label, String format, String defaultValue) {
    this(key, label, format, defaultValue, true);
  }

  
  public BaseDetail(String key, String label, String format, String defaultValue, boolean selectable) {
    this.key = key;
    this.label = label;
    this.format = format;
    this.selectable = selectable;
    this.defaultValue = defaultValue;
  }
  
   @Override
  public boolean isSelectable() {
    return selectable;
  }

  @Override
  public void select() {
    if (!selectable) throw new RuntimeException("Detail is not selectable") ;
    if (preExecute()) postExecute(execute());
  }

  protected void postExecute(List<String> feedBack) {
    if (feedBack == null) return;
    if(feedBack.size() == 0 ) return;
    Viewer.view(feedBack);
  }

  protected List<String> execute() {
    return null;
  }

  protected boolean preExecute() {
    return true;
  }

  @Override
  public void setValue(String value) {
    this.value = value;
    
  }

  @Override
  public String getValue() {
    if (!isFresh) refresh();
    return value;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
    
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
    
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setFormat(String format) {
    this.format = format;
  }

  @Override
  public String getFormat() {
    return format;
  }
  
  @Override 
  public String toString() {
    if (!isFresh || autoRefresh) refresh();
    if (specials.containsKey(value)) 
      return String.format(format, key, label,specials.get(value).toString());
    return String.format(format, key, label, value == null ? "" : value);
  }

  protected void refresh() {
    isFresh = true;
  }

  public boolean isFresh() {
    return isFresh;
  }
  
  @Override
  public Detail addSpecialValue(String value, String label) {
    specials.put(value, label);
    return this;
  }

  @Override
  public Map<String, String> getSpecials() {
    return specials;
  }


  @Override
  public void keyChanged(String key) {
    if (key.equals(this.key)) {
      isFresh = false;
      parent.needRefresh();
    }
  }


  @Override
  public void listChanged(String list, String parameter) {
    // Empty method, details never contain lists
  }


  @Override
  public boolean isAutoFefresh() {
    return autoRefresh;
  }


  @Override
  public void setParent(Node menuItem) {
    parent= menuItem;
    
  }


  @Override
  public void needRefresh() {
    isFresh = false;
    parent.needRefresh();
  }
  
  

  
 



}
