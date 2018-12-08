package lejos.ev3.menu.presenter;

import java.util.List;

public class Command extends BaseDetail {

  public Command(String command, String label, String value) {
    super(command, label, "%2$s", value, true);
    this.value = value;
    isFresh = true;
  }
  
@Override
  protected List<String> execute() {
    return model.execute(key, value);
  }
}
