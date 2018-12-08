package lejos.ev3.menu.presenter;

public class DynamicDetail extends SettingDetail{

  public DynamicDetail(String key, String label, String format, String defaultValue) {
    super(key, label, format, defaultValue);
    autoRefresh = true;
  }

}
