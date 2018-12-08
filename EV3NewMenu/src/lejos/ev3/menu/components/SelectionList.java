package lejos.ev3.menu.components;

import java.util.List;
import java.util.Map;

public class SelectionList {
  private static TextPanel itemPanel = new TextPanel("");
  private static Panel listPanel = new Panel();
  static {
    listPanel.setMargin(2);
    listPanel.setBorders(15);
    listPanel.setShadow(true);
  }
  
  
  public static void setList(Map<String, String> list){
    
  }
  
  public static int select(Map<String, String> list) {
    return 0;
  }

  
  public static int select(List<String> list) {
    int height = 2 *listPanel.getMargin();
    for (String item : list) {
      
    }
    return 0;
  }

}
