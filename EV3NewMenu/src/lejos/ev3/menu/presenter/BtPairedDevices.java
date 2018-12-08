package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.hardware.lcd.Image;

public class BtPairedDevices extends BaseNode {
 
  public BtPairedDevices(String label, Image icon ) {
    super( label , icon);
    this.key = "PAIRED_DEVICES";
    model.attach(key, this);
  }
  
 
  @Override
  protected void refresh() {
    super.refresh();
    List<String> entries = model.getList("PAIRED_DEVICES", null);
    clearDetails();
    if (entries == null || entries.isEmpty()) {
      addDetail(new BaseDetail("", "<No paired devices>", "%2$s", "", false));
    }
    else
      for (String entry: entries) {
        addDetail(new BtForgetCommand( entry));
      }
    isFresh = true;
  }
  

}
