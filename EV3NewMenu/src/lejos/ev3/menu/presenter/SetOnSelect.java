package lejos.ev3.menu.presenter;


public class SetOnSelect extends SettingDetail {

  private String newValue;

  public SetOnSelect(String key, String label , String newValue) {
    super(key, label, "%2$s", newValue);
    this.newValue = newValue;
    isFresh = true;
    selectable = true;
  }
  
  @Override
  public void select() {
    setValue(newValue);
  }

}
