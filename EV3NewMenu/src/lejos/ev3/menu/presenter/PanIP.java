package lejos.ev3.menu.presenter;


public class PanIP extends IpDetail{

  
  public PanIP() {
    super ("Pan.address", "Address");
    addSpecialValue("0.0.0.0", "<auto>");
    addSpecialValue("*", "<any>");
    addSpecialValue("", "");
    selectable = true;
  }
  
  @Override
  public void select() {
    String mode = model.getSetting("Pan.mode", "NONE");
    if (mode.equals("NONE")) return;
  }

  @Override
  public void keyChanged(String key) {
    super.keyChanged(key);
    if (key.equals("Pan.mode")) {
      if (value.equals("NONE")) selectable = false;
      else selectable = true;
    }
  }
  
  
  

 

}
