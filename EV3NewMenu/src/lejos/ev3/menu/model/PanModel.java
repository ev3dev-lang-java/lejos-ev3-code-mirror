package lejos.ev3.menu.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import lejos.ev3.startup.Utils;
import lejos.hardware.Bluetooth;
import lejos.hardware.BluetoothException;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.RemoteBTDevice;

/** Model to maintain PAN settings
 * @author Aswin Bouwmeester
 *
 */
public class PanModel extends AbstractModel{
    private static final String START_PAN = "/home/root/lejos/bin/startpan";
    private static final String PAN_CONFIG = "/home/root/lejos/config/pan.config";
    private static final String PAN_INTERFACE = "br0";


    public static final int MODE_NONE = 0;
    public static final int MODE_AP = 1;
    public static final int MODE_APP = 2;
    public static final int MODE_BTC = 3;
    public static final int MODE_USBC = 4;

    final String[] modeIDS = { "NONE", "AP", "AP+", "BT", "USB" };
    final String[] serviceNames={"NAP", "PANU", "GN"};
    static final String autoIP = "0.0.0.0";
    static final String anyAP = "*";
    
    String[] IPAddresses = {autoIP, autoIP, autoIP, autoIP, autoIP };
    String[] IPNames = {"Address", "Netmask", "Brdcast", "Gateway", "DNS    "};
    String[] IPIDS = {"IP", "NM", "BC", "GW", "DN"};
    
    int curMode = MODE_NONE;
    String BTAPName = anyAP;
    String BTAPAddress = anyAP;
    String BTService = "NAP";
    String persist = "N";
    Boolean changed = false;

    protected PanModel()
    {
      myKeys = Arrays.asList("Pan.mode","Pan.address","Pan.netmask", "Pan.broadcast","Pan.gateway", "Pan.dns", "Pan.persist", "Pan.service", "Pan.apname", "Pan.apaddress");
      myCommands = Arrays.asList("PAN_APPLY");

        loadConfig();
    }
    
    @Override
    public String getSetting(String key, String defaultValue) {
      switch(key) {
      case "Pan.mode": return modeIDS[curMode];
      case "Pan.address": return IPAddresses[0];
      case "Pan.netmask": return IPAddresses[1];
      case "Pan.broadcast": return IPAddresses[2];
      case "Pan.gateway": return IPAddresses[3];
      case "Pan.dns": return IPAddresses[4];
      case "Pan.persist": return persist;
      case "Pan.service": return BTService;
      case "Pan.apname": return this.BTAPName;
      case "Pan.apaddress": return this.BTAPAddress;
      default: return null;
      }
    }

    @Override
    public void setSetting(String key, String value) {
      switch(key) {
      case "Pan.mode": {
        init(getCurMode(value));
        break;
      }
      case "Pan.address": {
        IPAddresses[0] = value;
        break;
      }
      case "Pan.netmask": {
        IPAddresses[1] = value;
        break;
      }
      case "Pan.broadcast": {
        IPAddresses[2] = value;
        break;
      }
      case "Pan.gateway": {
        IPAddresses[3] = value;
        break;
      }
      case "Pan.dns": {
        IPAddresses[4] = value;
        break;
      }
      case "Pan.persist": {
        persist = value;
        break;
      }
      case "Pan.service": {
        BTService = value;
        break;
      }
      case "Pan.apname": {
        this.BTAPName = value;
        this.BTAPAddress = ModelContainer.getModel().getSetting("bluetooth.remote_address", value);
        broadcast("Pan.apaddress", BTAPAddress);
        break;
      }
      default: return;
      }
      broadcast(key, value);
    }
    
    
    
 
@Override
    public List<String> execute(String command, String target, String... arguments) {
      switch(command) {
      case "PAN_APPLY": {
        runScript();
      }
      }
      return null;
    }

private int getCurMode(String id) {
  for (int i = 0; i< modeIDS.length; i++) {
    if (id.equals(modeIDS[i])) return i;
  }
  return MODE_NONE;
}
    
private void runScript() {
if (changed)
{
    //waitScreen.begin("Restart\nPAN\nServices");
    //waitScreen.status("Save configuration");
    saveConfig();
    this.run(START_PAN);
    //waitScreen.status("Restart name server");
    BrickFinder.stopDiscoveryServer();
    BrickFinder.startDiscoveryServer(curMode == MODE_APP);
    //waitScreen.end();
}
}

 private void saveConfig()
    {
        System.out.println("Save PAN config");
        try {
            PrintWriter out = new PrintWriter(PAN_CONFIG);
            out.print(modeIDS[curMode] + " " + BTAPName.replace(" ", "\\ ") + " " + BTAPAddress);
            for(String ip : IPAddresses)
                out.print(" " + ip);
            out.print(" " + BTService + " " + persist);
            out.println();
            out.close();
            changed = false;
        } catch (IOException e) {
            System.out.println("Failed to write PAN config to " + PAN_CONFIG + ": " + e);
        }            
    }
    
    private String getConfigString(String[] vals, int offset, String def)
    {
        if (vals == null || offset >= vals.length || vals[offset] == null || vals[offset].length() == 0)
            return def;
        return vals[offset];
    }
    
    public void loadConfig()
    {
        System.out.println("Load PAN config");
        String[] vals = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(PAN_CONFIG));
            // nasty cludge preserve escaped spaces (convert them to no-break space
            String line = in.readLine().replace("\\ ", "\u00a0");
            vals = line.split("\\s+");
            in.close();
        } catch (IOException e) {
            System.out.println("Failed to load PAN config from " + PAN_CONFIG + ": " + e);
        }            
        String mode = getConfigString(vals, 0, modeIDS[MODE_NONE]);
        // turn mode into value
        curMode = MODE_NONE;
        for(int i = 0; i < modeIDS.length; i++)
            if (modeIDS[i].equalsIgnoreCase(mode))
            {
                curMode = i;
                break;
            }
        // be sure to convert no-break space back - ahem.
        BTAPName = getConfigString(vals, 1, anyAP).replace("\u00a0", " ");
        BTAPAddress = getConfigString(vals, 2, anyAP);
        for(int i = 0; i < IPAddresses.length; i++)
            IPAddresses[i] = getConfigString(vals, i + 3, autoIP);
        if (curMode == MODE_AP && IPAddresses[0].equals(autoIP))
            IPAddresses[0] = "10.0.1.1";
        BTService = getConfigString(vals, 8, "NAP");
        persist = getConfigString(vals, 9, "N");
        changed = false;
    }
    
    
    public void init(int mode)
    {
        if (mode != curMode)
        {
            for(int i = 0; i < IPAddresses.length; i++)
                IPAddresses[i] = autoIP;
            switch(mode)
            {
            case MODE_AP:
                IPAddresses[0] = "10.0.1.1";
                break;
            case MODE_APP:
                // For access point plus we need to use a sub-net within the
                // sub-net being used for WiFi. Set a default that may work for
                // most - well it does for me!
                
                {
                  String wlanAddress = ModelContainer.getModel().getSetting("wlan0", null);
                  if (wlanAddress == null) {
                    init(MODE_NONE);
                    return;
                  }
                    String[] parts = wlanAddress.split("\\.");
                    if (parts.length == 4)
                    {
                        IPAddresses[0] = parts[0] + "." + parts[1] + "." + parts[2] + ".208";
                    }
                }
                break;
            }
            BTAPName = anyAP;
            BTAPAddress = anyAP;
            BTService = "NAP";
            persist = "N";
            curMode = mode;
            changed = true;
            for (String key : myKeys)
              broadcast(key, "");
        }
    }
//
//    /**
//     * Test to see if the IP address string is the special case auto address
//     * @param ip
//     * @return true if the address is the auto address.
//     */
//    private boolean isAutoIP(String ip)
//    {
//        return ip.equals(autoIP);
//    }
//
//    /**
//     * Return an IP address suitable for display, replace the auto address with a
//     * more readable version.
//     * @param ip
//     * @return the display string
//     */
//    private String getDisplayIP(String ip)
//    {
//        return isAutoIP(ip) ? "<Auto>" : ip;
//    }
//
//    private boolean isAnyAP(String bt)
//    {
//        return bt.equals(anyAP);
//    }
//    
//    private String getDisplayAP(String bt)
//    {
//        return isAnyAP(bt) ? "Any Access Point" : bt;
//    }
//    
//    /**
//     * Validate and cleanup the IP address
//     * @param address
//     * @return validated IP or null if there is an error.
//     */
//    private String getValidatedIP(String address)
//    {
//        try 
//        {
//            return InetAddress.getByName(address).getHostAddress();
//        }
//        catch (UnknownHostException e)
//        {
//            return null;
//        }
//    }
//    
//
//    /**
//     * Allow the user to enter an IP address
//     * @param title String to display as the title of the screen
//     * @param ip IP address to edit
//     * @return new validated address
//     */
//    private String enterIP(String title, String ip)
//    {
//        String[] parts = ip.split("\\.");
//        for(int i = 0; i < parts.length; i++)
//            parts[i] = "000".substring(parts[i].length()) + parts[i];
//        String address = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
//        int curDigit = 0;
//        while (true)
//        {
//            newScreen(title);
//            lcd.drawString(address, 2, 4);
//            if (curDigit < 0)
//                curDigit = 14;
//            if (curDigit >= 15)
//                curDigit = 0;
//            Utils.drawRect(curDigit * 10 + 18, 60, 13, 20);
//            lcd.refresh();
//            int key = getButtonPress();
//            switch (key)
//            {
//                case Button.ID_ENTER:
//                { // ENTER
//                    // remove leading zeros
//                    String ret = getValidatedIP(address);
//                    if (ret == null)
//                        msg("Invalid address");
//                    else
//                        return ret;
//                    break;
//                }
//                case Button.ID_LEFT:
//                { // LEFT
//                    curDigit--;
//                    if (curDigit < 0)
//                        curDigit = 14;
//                    if (address.charAt(curDigit) == '.')
//                        curDigit--;
//                    break;
//                }
//                case Button.ID_RIGHT:
//                { // RIGHT
//                    curDigit++;
//                    if (curDigit >= 15)
//                        curDigit = 0;
//                    if (address.charAt(curDigit) == '.')
//                        curDigit++;
//                    break;
//                }
//                case Button.ID_ESCAPE:
//                { // ESCAPE
//                    return ip;
//                }
//                case Button.ID_UP:
//                {
//                    int val = (address.charAt(curDigit) - '0');
//                    if (++val > 9)
//                        val = 0;
//                    address = address.substring(0, curDigit) + ((char)('0' + val)) + address.substring(curDigit + 1);
//                    break;
//                }
//                case Button.ID_DOWN:
//                {
//                    int val = (address.charAt(curDigit) - '0');
//                    if (--val < 0)
//                        val = 9;
//                    address = address.substring(0, curDigit) + ((char)('0' + val)) + address.substring(curDigit + 1);
//                    break;
//                }
//            }               
//        }
//    }
//
//    /**
//     * Allow the user to choose between an automatic or manual IP address if
//     * manual allow the address to be edited 
//     * @param title
//     * @param ip
//     * @return new ip address
//     */
//    private String getIPAddress(String title, String ip)
//    {
//        String [] strings = {"Automatic", "Advanced"};
//        String [] icons = new String[strings.length];
//        GraphicMenu menu = new GraphicListMenu(strings,icons, 4);
//        newScreen(title);
//        String dispIP = getDisplayIP(ip);
//        lcd.drawString(dispIP, (lcd.getTextWidth() - dispIP.length())/2, 2);
//        menu.setItems(strings, icons);
//        int selection = getSelection(menu, isAutoIP(ip) ? 0 : 1);
//        switch (selection)
//        {
//        case 0:
//            return autoIP;
//        case 1:
//            return enterIP(title, ip);
//        default:
//            return ip;
//        }
//    }
//    
//    /**
//     * Allow the user to choose the Bluetooth service to connect to.
//     * @param title
//     * @param service
//     * @return new service
//     */
//    private String getBTService(String title, String service)
//    {
//        String [] strings = serviceNames;
//        String [] icons = new String[strings.length];
//        GraphicMenu menu = new GraphicListMenu(strings,icons, 4);
//        newScreen(title);
//        lcd.drawString(service, (lcd.getTextWidth() - service.length())/2, 2);
//        menu.setItems(strings, icons);
//        int item = 0;
//        while(!strings[item].equalsIgnoreCase(service))
//            item++;
//        int selection = getSelection(menu, item);
//        if (selection > 0)
//            return strings[selection];
//        else
//            return service;
//    }
//    
//    
//    public void configureAdvanced()
//    {
//        int selection = 0;
//        int extra = (curMode == MODE_BTC ? 2 : curMode == MODE_USBC ? 1 : 0);
//        String [] strings = new String[IPAddresses.length + extra];
//        String [] icons = new String[IPAddresses.length+ extra];
//        GraphicMenu menu = new GraphicListMenu(strings,icons);
//        for(;;)
//        {
//            newScreen(modeNames[curMode]);
//            for(int i = 0; i < IPAddresses.length; i++)
//                strings[i] = IPNames[i] + " " + getDisplayIP(IPAddresses[i]);
//            if (extra > 0)
//                strings[IPAddresses.length] = "Persist " + persist;
//            if (extra > 1)
//                strings[IPAddresses.length+1] = "Service " + BTService;
//            
//            menu.setItems(strings, icons);
//            selection = getSelection(menu, selection);
//            if (selection < 0) break;
//            changed = true;
//            if (selection < IPAddresses.length)
//                IPAddresses[selection] = getIPAddress(IPNames[selection], IPAddresses[selection]);
//            else if (selection == IPAddresses.length)
//            {
//                switch (getYesNo("Persist Connection", persist.equalsIgnoreCase("Y")))
//                {
//                case 0:
//                    persist = "N";
//                    break;
//                case 1:
//                    persist = "Y";
//                    break;
//                }
//            }
//            else
//                BTService = getBTService("Service", BTService);
//
//
//        }
//    }
//    
//    
//    /**
//     * Display all currently known Bluetooth devices.
//     */
//    private void selectAP()
//    {
//        newScreen("Devices");
//        lcd.drawString("Searching...", 3, 2);
//        List<RemoteBTDevice> devList;
//        try {
//            devList = (List<RemoteBTDevice>) Bluetooth.getLocalDevice().getPairedDevices();
//        } catch (BluetoothException e) {
//            return;
//        }
//        if (devList.size() <= 0)
//        {
//            msg("No known devices");
//            return;
//        }
//        
//        String[] names = new String[devList.size()];
//        String[] icons = new String[devList.size()];
//        int i=0;
//        for (RemoteBTDevice btrd: devList)
//        {
//            names[i] = btrd.getName();
//            i++;
//        }
//
//        GraphicListMenu deviceMenu = new GraphicListMenu(names, icons);
//        int selected = 0;
//        newScreen("Devices");
//        selected = getSelection(deviceMenu, selected);
//        if (selected >= 0)
//        {
//            RemoteBTDevice btrd = devList.get(selected);
//            //byte[] devclass = btrd.getDeviceClass();
//            BTAPName = btrd.getName();
//            BTAPAddress = btrd.getAddress();
//            changed = true;
//        }
//    }
//    
//    public void configureBTClient(String title)
//    {
//        String [] strings = {"Any", "Select", "Advanced"};
//        String [] icons = new String[strings.length];
//        GraphicMenu menu = new GraphicListMenu(strings, icons, 4);
//        while (true)
//        {
//            newScreen(title);
//            String dispIP = getDisplayAP(BTAPName);
//            lcd.drawString(dispIP, (lcd.getTextWidth() - dispIP.length())/2, 2);
//            if (!isAnyAP(BTAPName))
//                lcd.drawString(BTAPAddress, (lcd.getTextWidth() - BTAPAddress.length())/2, 3);                    
//            int selection = getSelection(menu, isAnyAP(BTAPName) ? 0 : 1);
//            switch (selection)
//            {
//            case 0:
//                BTAPName = anyAP;
//                BTAPAddress = anyAP;
//                changed = true;
//                return;
//            case 1:
//                selectAP();
//                break;
//            case 2:
//                configureAdvanced();
//                break;
//            default:
//                return;
//            }    
//        }
//    }
//    
//    public void configure()
//    {
//        if (curMode == MODE_NONE)
//            return;
//        if (curMode == MODE_BTC)
//            configureBTClient(modeNames[curMode]);
//        else
//            configureAdvanced();
//    }
//    
//    public void panMenu()
//    {
//        int selection = 0;
//        GraphicMenu menu = new GraphicMenu(null,null,3);
//        do
//        {
//            newScreen("PAN");
//            menu.setItems(modeNames,
//                    new String[]{ICNone,ICAccessPoint,ICAccessPointPlus,ICBTClient,ICUSBClient});
//            selection = getSelection(menu, curMode);
//            if (selection >= 0)
//            {
//                init(selection);
//                configure();
//            }
//        } while (selection >= 0);
//        if (changed)
//        {
//            waitScreen.begin("Restart\nPAN\nServices");
//            waitScreen.status("Save configuration");
//            saveConfig();
//            startNetwork(START_PAN, true);
//            waitScreen.status("Restart name server");
//            BrickFinder.stopDiscoveryServer();
//            BrickFinder.startDiscoveryServer(curMode == MODE_APP);
//            waitScreen.end();
//        }

    @Override
    public List<String> getList(String list, String parameter) {
      return null;
    }
    }

 
/* Old menu structure
 * PAN
 * - BT Client
 * -- Any
 * -- Select
 * --- Devices
 * ---- device 1
 * ....
 * ---- device N
 * -- Advanced
 * --- Address
 * --- Netmask
 * --- Brdcast
 * --- Gateway
 * --- DNS
 * --- Persist
 * --- Service 
 * - USB Client
 * --- Address
 * --- Netmask
 * --- Brdcast
 * --- Gateway
 * --- DNS
 * --- Persist
 * - None
 * - Access Pt
 * -- Address
 * -- Netmask
 * -- Brdcast
 * -- Gateway
 * -- DNS
 * - Access Pt+
 * -- Address
 * -- Netmask
 * -- Brdcast
 * -- Gateway
 * -- DNS
 *     
 *     
 *     */
    
 
    
    


