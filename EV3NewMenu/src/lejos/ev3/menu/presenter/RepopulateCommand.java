package lejos.ev3.menu.presenter;

import java.util.List;

public class RepopulateCommand extends BaseDetail {

  

  public RepopulateCommand() {
    super("" , "<Scan>", "%2$s", " ", true);
  }

@Override
  public List<String> execute() {
    parent.needRefresh();
    return super.execute();
  }
  
  

}
