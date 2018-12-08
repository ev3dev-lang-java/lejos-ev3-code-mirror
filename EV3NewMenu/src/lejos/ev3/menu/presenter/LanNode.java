package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.hardware.lcd.Image;

public class LanNode extends BaseNode{

  public LanNode(String label, Image icon) {
    super(label, icon);
    this.key = "ACCESSPOINTS";
  }

  @Override
  public void refresh() {
    super.refresh();
    clearDetails();
    List<String> accesspoints = model.getList(key, " ");
    for (String ap : accesspoints) {
      this.addDetail(new LanConnect(ap));
    }
  }
  
  
  

}
