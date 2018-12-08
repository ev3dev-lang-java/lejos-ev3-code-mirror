package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.ev3.menu.viewer.EditorString;

/** Menu detail for the displayand selection of wifi access points. 
 * When selected this detail will present a password entry screen and request the model to connect to the SSID that this object represents.
 * @author Aswin Bouwmeester
 *
 */
public class LanConnect extends BaseDetail{
  String remoteKey=" ";


  public LanConnect(String entry) {
    super("CONNECT", "Connect", "%3$s", "", true);
    this.value = entry;
    isFresh = true;
  }
  
  
  
@Override
  protected boolean preExecute() {
  String newValue = EditorString.editValue();
  if (newValue.isEmpty()) return false;
  remoteKey = newValue;
  return true;
  }



@Override
  protected List<String> execute() {
    List<String> r = model.execute(key, value, remoteKey);
    if (r != null) {
      menu.dialog("Connecting failed",2);
    }
    return null;
  }

  

}
