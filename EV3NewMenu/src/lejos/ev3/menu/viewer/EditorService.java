package lejos.ev3.menu.viewer;

import java.util.ArrayList;

public class EditorService extends EditorSelect{
  
  public EditorService() {
    choices = new ArrayList<String>();
    choices.add("NAP");
    choices.add("PANU");
    choices.add("GN");
  }

}
