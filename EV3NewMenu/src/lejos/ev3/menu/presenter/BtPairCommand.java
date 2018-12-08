package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.viewer.EditorBtKey;


public class BtPairCommand extends BaseDetail {
  String remoteKey="0000";


  public BtPairCommand(String entry) {
    super("PAIR", "Pair", "%3$s", "", true);
    this.value = entry;
    isFresh = true;
  }
  
  
  
@Override
  protected boolean preExecute() {
  String newValue = EditorBtKey.editKey(remoteKey,value);
  if (newValue.isEmpty()) return false;
  remoteKey = newValue;
  return true;
  }



@Override
  protected List<String> execute() {
    List<String> r = model.execute(key, value, remoteKey);
    if (r == null) {
      menu.dialog("Pairing succeeded",1);
    }
    else {
      menu.dialog("Pairing failed",2);
    }
    return null;
  }

}
 