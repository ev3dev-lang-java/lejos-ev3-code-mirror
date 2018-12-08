package lejos.ev3.menu.presenter;



public class SubmenuDetail extends BaseDetail {

  private Node subMenu;
  
  public SubmenuDetail(String label, Node subMenu) {
    super("", label, "%2$s", "");
    this.subMenu = subMenu;
    selectable = true;
    isFresh = true;
  }

  @Override
  public void select() {
    menu.selectFromList(subMenu);
  }

}
