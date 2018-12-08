package lejos.ev3.menu.presenter;

import lejos.hardware.lcd.Image;

public class FileNode extends BaseNode {

  private String file;

  public FileNode(String file, String label, Image icon) {
    super(label, icon);
    this.file = file;
    model.attach("DELETE", this);
  }

}
