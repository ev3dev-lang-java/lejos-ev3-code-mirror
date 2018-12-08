package lejos.ev3.menu.model;


import java.util.ArrayList;
import java.util.List;

import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.presenter.BaseDetail;
import lejos.ev3.menu.presenter.BaseNode;
import lejos.ev3.menu.presenter.BtDevices;
import lejos.ev3.menu.presenter.BtPairedDevices;
import lejos.ev3.menu.presenter.Command;
import lejos.ev3.menu.presenter.DynamicDetail;
import lejos.ev3.menu.presenter.Files;
import lejos.ev3.menu.presenter.LanNode;
import lejos.ev3.menu.presenter.Node;
import lejos.ev3.menu.presenter.PanNode;
import lejos.ev3.menu.presenter.RunDefault;
import lejos.ev3.menu.presenter.SettingDetail;
import lejos.ev3.menu.presenter.SubmenuDetail;
import lejos.ev3.menu.viewer.EditorBoolean;
import lejos.ev3.menu.viewer.EditorBtKey;
import lejos.ev3.menu.viewer.EditorNumeric;
import lejos.ev3.menu.viewer.EditorString;
import lejos.ev3.menu.viewer.GrMenu;
import lejos.ev3.menu.viewer.Menu;
import lejos.ev3.menu.viewer.WaitScreen;

/** The ModelContainer class acts as a wrapper that bundles more specific models and hides these for the users. 
 * @author Aswin Bouwmeester
 *
 */
public class ModelContainer implements Model{
  private static ModelContainer model;
  private List<Model> subModels = new ArrayList<Model>(); 
  
  public static void main(String[] args) {



    model = new ModelContainer();
    Menu menu = GrMenu.getMenu();

    BaseDetail.setEnvironment( model, menu);
    BaseNode.setEnvironment( model, menu);
    menu.setEnvironment(model);
    model.initialize();

    ArrayList<Node> top = new ArrayList<Node>();
    top.add(new BaseNode("leJOS EV3", Icons.LEJOS).addDetail(new RunDefault())
        .addDetail(new SettingDetail("wlan0", "LAN", "%2$s: %3$s", null).addSpecialValue(null, "No Wifi"))
        .addDetail(new SettingDetail("br0", "PAN", "%2$s: %3$s", null).addSpecialValue(null, "No PAN")));
    top.add(new BaseNode("System", Icons.EV3)
        .addDetail(new SettingDetail("system.hostname", "Name", "%2$s: %3$s", "", EditorString.class))
        .addDetail(new SettingDetail("lejos.sleeptime", "Sleep time", "%2$s: %3$s", "", EditorNumeric.class))
        .addDetail(new Command("CLOSE_PORTS","Reset ports",""))
        .addDetail(new Command("SUSPEND_MENU","Suspend Menu",""))
        .addDetail(new SettingDetail("lejos.ntp", "NTP", "%2$4s: %3$s", "1.uk.pool.ntp.org", EditorString.class))
        );
    top.add(new BaseNode("Info", Icons.INFO)
        .addDetail(new SettingDetail("lejos.version", "Ver", "%2$s: %3$s", ""))
        .addDetail(new DynamicDetail("system.time", "Time", "%2$s: %3$s", ""))
        .addDetail(new DynamicDetail("system.volt", "Battery", "%2$s: %3$s", ""))
        .addDetail(new DynamicDetail("system.current", "Current", "%2$s: %3$s", "")));
    top.add(new BaseNode("Wifi", Icons.WIFI)
        .addDetail(new SettingDetail("ssid", "SSID", "%2$4s: %3$s", "", new LanNode("Access points", Icons.WIFI)).addSpecialValue("", "No Wifi"))
        .addDetail(new SettingDetail("wlan0", "IP", "%2$4s: %3$s", "").addSpecialValue("", "No connection"))
        );
    top.add(new BaseNode("BlueTooth", Icons.BLUETOOTH)
        .addDetail(new SubmenuDetail("Pair", new BtDevices("Pair", Icons.SEARCH))) 
        .addDetail(new SubmenuDetail("Devices", new BtPairedDevices("Devices", Icons.BLUETOOTH)))
        .addDetail(new SettingDetail( "bluetooth.visibility","Visibility", "%2$s: %3$s", "false", EditorBoolean.class).addSpecialValue("true", "on").addSpecialValue("false", "off"))
        .addDetail(new SettingDetail( "bluetooth.pin","PIN", "%2$s: %3$s","1234", EditorBtKey.class))
        .addDetail(new SettingDetail( "bluetooth.name","Name", "%2$s: %3$s", "?"))
       .addDetail(new SettingDetail( "bluetooth.address","Address", "%3$s", "?"))
    );
    top.add(new Files(Files.PROGRAMS_DIRECTORY, Icons.PROGRAM));
    top.add(new Files(Files.SAMPLES_DIRECTORY, Icons.SAMPLES));
    top.add(new Files(Files.TOOLS_DIRECTORY, Icons.TOOLS));
    top.add(new Files(Files.LIB_DIRECTORY, Icons.FILES));
    top.add(new Files("/home/root/lejos/config", Icons.FILES));
    top.add(new BaseNode("Sound", Icons.EYE)
        .addDetail(new SettingDetail("audio.volume", "Volume", "%2$s : %3$4s", "30", EditorNumeric.class))
        .addDetail(new SettingDetail("lejos.keyclick_volume", "Key vol", "%2$s : %3$4s", "30", EditorNumeric.class))
        .addDetail(new SettingDetail("lejos.keyclick_length", "Key length", "%2$s : %3$4s", "30", EditorNumeric.class))
        .addDetail(new SettingDetail("lejos.keyclick_frequency", "Key freq", "%2$s : %3$4s", "800", EditorNumeric.class)));
    top.add(new PanNode("PAN",Icons.ACCESSPOINT));

    ;
    menu.runMenu(top);
    model.terminate();
  }

  
  


  private ModelContainer(){
    subModels.add( new LejosModel());
    subModels.add(new FilesModel());
    subModels.add(new BtModel());
    subModels.add(new AudioModel());
    subModels.add(new SystemModel());
    subModels.add( new NetworkModel());
  };
  
  public static ModelContainer getModel() {
    if (model == null) 
      model = new ModelContainer();
    return model;
  }

  @Override
  public void attach(String key, ModelListener listener) {
    Model target;
    target = this.getResponsibleForSetting(key);
    if (target != null) target.attach(key, listener);
    target = this.getResponsibleForCommand(key);
    if (target != null) target.attach(key, listener);
    target = this.getResponsibleForList(key);
    if (target != null) target.attach(key, listener);
  }

  @Override
  public void detach(String key, ModelListener listener) {
    for (Model subModel : subModels) 
      subModel.detach(key, listener);
  }
  
  @Override
  public void attach(WaitScreen listener) {
    for (Model subModel : subModels) 
      subModel.attach(listener);
  }

  @Override
  public void detach(WaitScreen listener) {
    for (Model subModel : subModels) 
      subModel.detach(listener);
  }


  @Override
  public boolean hasSetting(String key) {
    Model target;
    target = this.getResponsibleForSetting(key);
    if (target != null) return true;
    return false;
  }

  @Override
  public boolean canExecute(String command) {
    Model target;
    target = this.getResponsibleForCommand(command);
    if (target != null) return true;
    return false;
  }

  @Override
  public boolean canList(String list) {
    Model target;
    target = this.getResponsibleForList(list);
    if (target != null) return true;
    return false;
  }

  @Override
  public String getSetting(String key, String defaultValue) {
    Model target;
    target = this.getResponsibleForSetting(key);
    if (target != null) return target.getSetting(key, defaultValue);
    else
      throw new RuntimeException("Key " + key + " is not supported by the model");
  }

  @Override
  public void setSetting(String key, String value) {
    Model target;
    target = this.getResponsibleForSetting(key);
    if (target != null) target.setSetting(key, value);
    else
      throw new RuntimeException("Key " + key + " is not supported by the model");
  }

  @Override
  public List<String> execute(String command, String target, String... arguments) {
    Model targetModel;
    if (command.equals("SHUTDOWN")) terminate();
    targetModel = this.getResponsibleForCommand(command);
    if (targetModel != null) return targetModel.execute(command, target, arguments );
    else
      throw new RuntimeException("Command " + command + " is not supported by the model");
  }

  @Override
  public List<String> getList(String list, String parameter) {
    Model target;
    target = this.getResponsibleForList(list);
    if (target != null) return target.getList(list, parameter);
    else
      throw new RuntimeException("List " + list + " is not supported by the model");
  }
  
  private Model getResponsibleForSetting(String key) {
    for (Model subModel : subModels)
      if (subModel.hasSetting(key)) return subModel;
    return null;
  }
  
  private Model getResponsibleForCommand(String command) {
    for (Model subModel : subModels)
      if (subModel.canExecute(command)) return subModel;
    return null;
  }
  
  private Model getResponsibleForList(String list) {
    for (Model subModel : subModels)
      if (subModel.canList(list)) return subModel;
    return null;
  }

  @Override
  public void initialize() {
    for (Model subModel : subModels) 
      subModel.initialize();
    
  }

  @Override
  public void terminate() {
    for (Model subModel : subModels) 
      subModel.terminate();
  }

 
  
  
}
