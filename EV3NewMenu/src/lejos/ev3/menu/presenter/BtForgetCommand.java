package lejos.ev3.menu.presenter;

import java.util.List;


public class BtForgetCommand extends BaseDetail {

  public BtForgetCommand( String entry) {
    super("FORGET", "Forget", "%3$s", "", true);
    this.value = entry;
    isFresh = true;
  }
  
@Override
  protected List<String> execute() {
    return model.execute(key, value);
  }

  @Override
  protected boolean preExecute() {
    return menu.dialog("Do you really/nwant to forget/n" + value, 3);
  }

}
 