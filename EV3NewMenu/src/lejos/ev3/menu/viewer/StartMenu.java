package lejos.ev3.menu.viewer;

import java.util.ArrayList;
import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.model.Model;
import lejos.ev3.menu.model.ModelContainer;
import lejos.ev3.menu.presenter.*;

/**
 * Defines the menu structure of the leJOS menu and starts the menu
 * 
 * @author Aswin Bouwmeester
 *
 */
public class StartMenu {

  public static void main(String[] args) {



    Model model = ModelContainer.getModel();
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
  }

}



