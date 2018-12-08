package lejos.ev3.menu.presenter;

 import java.util.Map;

import lejos.ev3.menu.components.Icons;

public class PanMode extends SettingDetail {

  public PanMode() {
    super("Pan.mode", "Mode", "%2$6s: %3$s", "NONE");
    this.addSpecialValue("NONE", "Disabled");
    this.addSpecialValue("AP", "Access Pt");
    this.addSpecialValue("AP+", "Access Pt+");
    this.addSpecialValue("BT", "BT Client");
    this.addSpecialValue("USB", "USB Client");
    this.selectable= true;
  }

  @Override
  public void select() {
    BaseNode x=new BaseNode(label, Icons.YES); 
    int  i=0;
    for (Map.Entry<String, String> item : specials.entrySet()) {
      x.addDetail(new BaseDetail(item.getKey(), item.getValue(), "%2$s", "" , true));
      if (item.getKey().equals(key)) 
        x.setSelected(i);
      i++;
    }
    x.needRefresh();
    Detail selected = menu.selectFromList(x);
    if (selected == null ) return;
    setValue(selected.getKey());
    parent.needRefresh();
  }


}
