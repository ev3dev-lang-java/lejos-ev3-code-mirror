package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.hardware.lcd.Image;

public class BtDevices extends BaseNode {
 
  
  public BtDevices(String label, Image icon ) {
    super( label , icon);
    this.key = "REMOTE_DEVICES";
    model.attach(key, this);
  }
  
 
  @Override
  protected void refresh() {
    super.refresh();
    List<String> entries = model.getList(key, null);
    clearDetails();
    addDetail(new RepopulateCommand());
    if (entries == null || entries.isEmpty() ) {
      menu.dialog("No devices found", 1);
    }
    else
      for (String entry: entries) {
        addDetail(new BtPairCommand( entry));
      }
    isFresh = true;
  }
  

}
