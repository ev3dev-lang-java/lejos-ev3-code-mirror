package lejos.ev3.menu.presenter;

 import java.util.List;
import java.util.Map;

import lejos.ev3.menu.components.Icons;

public class PanServer extends SettingDetail {

  public PanServer() {
    super("Pan.apname", "Server", "%2$6s: %3$s", "*");
    this.addSpecialValue("*", "Any");
    this.selectable= true;
  }

  @Override
  public void select() {
    BaseNode x=new BaseNode("Pan Server", Icons.YES); 
    x.addDetail(new BaseDetail("*","Any" , "%2$s", "" , true));
    x.addDetail(new BaseDetail("select","Select" , "%2$s", "" , true));
    x.needRefresh();
    Detail selected = menu.selectFromList(x);
    if (selected == null ) return;
    if (selected.getKey().equals("*")) {
        setValue(selected.getKey());
    }
    else {
      x = new BaseNode("Devices", Icons.BLUETOOTH);
      List<String> entries = model.getList("PAIRED_DEVICES", null);
      if (entries == null || entries.isEmpty()) {
        menu.dialog("No Paired devices", 1);
        return;
      }
      for (String entry: entries) {
        x.addDetail(new BaseDetail( entry, entry, "%2$s", entry, true));
      }
      x.selectNextDetail();
      selected = menu.selectFromList(x);
      if (selected == null) return;
      setValue(selected.getKey());
    }
    parent.needRefresh();
  }


}
