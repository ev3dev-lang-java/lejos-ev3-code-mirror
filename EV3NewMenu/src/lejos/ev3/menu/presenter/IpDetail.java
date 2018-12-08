package lejos.ev3.menu.presenter;

import lejos.ev3.menu.viewer.EditorIpAddress;

public class IpDetail extends SettingDetail{

  public IpDetail(String key, String label) {
    super(key, label, "%2$7s: %3$s", "0.0.0.0", EditorIpAddress.class);
    this.addSpecialValue("0.0.0.0", "<Auto>");
  }
  
  @Override 
  public String toString() {
    if (!isFresh || autoRefresh) refresh();
    if (specials.containsKey(value)) 
      return String.format(format, key, label, specials.get(value).toString());
    return String.format(format, key, label, value == null ? "" : value);
  }
  
}
