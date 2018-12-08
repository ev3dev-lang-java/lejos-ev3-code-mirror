package lejos.ev3.menu.presenter;

import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.viewer.EditorSelect;
import lejos.ev3.menu.viewer.EditorService;
import lejos.hardware.lcd.Image;

public class PanNode extends BaseNode{

  public PanNode(String label, Image icon) {
    super(label, icon);
  }

  @Override
  protected void refresh() {
    clearDetails();
    super.refresh();
    addDetail(new PanMode());
    String mode = model.getSetting("Pan.mode", "NONE");
    if (mode.equals("NONE")) return;
    if (mode.equals("BT")) {
      addDetail(new PanServer());
    }
    addDetail(new Command("PAN_APPLY","Apply settings","" ));
    Node advanced = new BaseNode("PAN settings", Icons.PAN);
    advanced.addDetail(new IpDetail("Pan.address", "Address"));
    advanced.addDetail(new IpDetail("Pan.netmask", "Netmask"));
    advanced.addDetail(new IpDetail("Pan.broadcast", "Brdcast"));
    advanced.addDetail(new IpDetail("Pan.gateway", "Gateway"));
    advanced.addDetail(new IpDetail("Pan.dns", "DNS"));
    if (mode.equals("BT") || mode.equalsIgnoreCase("USB")) 
      advanced.addDetail(new SettingDetail("Pan.persist", "Persist", "%2$s: %3$4s", "N", EditorSelect.class));
    if (mode.equals("BT"))
      advanced.addDetail(new SettingDetail("Pan.service", "Service", "%2$s: %3$4s","NAP", EditorService.class));
    addDetail(new SubmenuDetail("Advanced settings", advanced));
    isFresh = true;
  }
  
  

}
