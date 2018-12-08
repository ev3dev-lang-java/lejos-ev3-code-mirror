package lejos.ev3.menu.presenter;

import java.util.ArrayList;
import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.viewer.Editor;


public class SettingDetail extends BaseDetail {
  protected Class<? extends Editor> editor     = null;
  private Node subMenu;

  
  public SettingDetail(String key, String label, String format, String defaultValue, Class<? extends Editor> editor) {
    this(key, label, format, defaultValue);
    this.editor = editor;
    selectable = true;
  }

public SettingDetail(String key, String label, String format, String defaultValue) {
    super(key, label, format, defaultValue);
    selectable = false;
    model.attach(key, this);
  }


public SettingDetail(String key, String label, String format, String defaultValue, Node subMenu) {
  this(key, label, format, defaultValue);
  this.subMenu = subMenu;
  selectable = true;
}


@Override
protected void refresh() {
  super.refresh();
  value = model.getSetting(key, defaultValue);
}

@Override
public void setValue(String value) {
  super.setValue(value);
  model.setSetting(key, value);
}

@Override
protected List<String> execute() {
  if (editor != null) {
  try {
    Editor edit = this.editor.newInstance();
    edit.edit(this);
  } catch (InstantiationException e) {
    e.printStackTrace();
  } catch (IllegalAccessException e) {
    e.printStackTrace();
  }
  }
  if (subMenu != null) {
    subMenu.needRefresh();
    menu.selectFromList(subMenu);
  }
  
  return null;
}

}
