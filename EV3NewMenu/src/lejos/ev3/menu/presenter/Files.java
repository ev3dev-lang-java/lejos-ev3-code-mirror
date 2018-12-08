package lejos.ev3.menu.presenter;

import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.hardware.lcd.Image;

public class Files extends BaseNode {
 
  public static final String PROGRAMS_DIRECTORY = "/home/lejos/programs";
  public static final String LIB_DIRECTORY      = "/home/lejos/lib";
  public static final String SAMPLES_DIRECTORY  = "/home/root/lejos/samples";
  public static final String TOOLS_DIRECTORY    = "/home/root/lejos/tools";
  public static final String NOT_SET            = null;
  protected String           path;
  protected String key;

  public Files( String path) {
    this( path , Icons.FILES);
  }

  public Files( String path, Image icon) {
    super( path , icon);
    this.path = path;
    this.key = "GET_FILES";
    model.attach(key, this);
    isFresh = false;
  }

  
  @Override
  protected void refresh() {
    super.refresh();
    clearDetails();
    List<String> entries = model.getList(key, path);
    for (String entry : entries) {
      addDetail(new File( entry, this));
    }
    if (entries.isEmpty()) addDetail(new BaseDetail("", "<Empty>", "%2$s", "", false));
  }
  
  @Override 
  public String getLabel() {
    int i = super.getLabel().lastIndexOf(java.io.File.separator);
    return super.getLabel().substring(i+1);
  }

  @Override
  public void listChanged(String list, String parameter) {
    if (list.equals(key) && parameter.equals(path)) 
      isFresh = false;
  }

  

}
