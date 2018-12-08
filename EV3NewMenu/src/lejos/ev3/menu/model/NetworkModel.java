package lejos.ev3.menu.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import com.sun.jna.Memory;

import lejos.hardware.BrickFinder;
import lejos.hardware.Wifi;
import lejos.internal.io.NativeWifi;
import lejos.remote.ev3.RMIRemoteEV3;
import lejos.utility.Delay;

@SuppressWarnings("restriction")
public class NetworkModel extends AbstractModel {
  private static final String  WIFI_CONFIG  = "/home/root/lejos/config/wpa_supplicant.conf";
  private static final String  WIFI_BASE    = "/home/root/lejos/bin/utils/wpa_supplicant.txt";
  private static final String  START_WLAN   = "/home/root/lejos/bin/startwlan";
  private static final String  START_PAN    = "/home/root/lejos/bin/startpan";
  private static final String  PAN_CONFIG   = "/home/root/lejos/config/pan.config";
  private NativeWifi           wifi;
  private NativeWifi.WReqPoint reqP;

  public static final int      MODE_NONE    = 0;
  public static final int      MODE_AP      = 1;
  public static final int      MODE_APP     = 2;
  public static final int      MODE_BTC     = 3;
  public static final int      MODE_USBC    = 4;

  final String[]               modeIDS      = { "NONE", "AP", "AP+", "BT", "USB" };
  final String[]               serviceNames = { "NAP", "PANU", "GN" };
  static final String          autoIP       = "0.0.0.0";
  static final String          anyAP        = "*";

  String[]                     IPAddresses  = { autoIP, autoIP, autoIP, autoIP, autoIP };
  String[]                     IPNames      = { "Address", "Netmask", "Brdcast", "Gateway", "DNS    " };
  String[]                     IPIDS        = { "IP", "NM", "BC", "GW", "DN" };

  int                          curMode      = MODE_NONE;
  String                       BTAPName     = anyAP;
  String                       BTAPAddress  = anyAP;
  String                       BTService    = "NAP";
  String                       persist      = "N";
  Boolean                      changed      = false;

  protected NetworkModel() {
    myKeys = Arrays.asList("wlan0", "br0", "ssid", "lejos.ntp", "Pan.mode", "Pan.address", "Pan.netmask", "Pan.broadcast", "Pan.gateway",
        "Pan.dns", "Pan.persist", "Pan.service", "Pan.apname", "Pan.apaddress");
    myLists = Arrays.asList("ACCESSPOINTS");
    myCommands = Arrays.asList("CONNECT", "PAN_APPLY");
  }

  @Override
  public void initialize() {
    openDisplay();
    display("Start/nwifi");
    wifi = new NativeWifi();
    reqP = new NativeWifi.WReqPoint();
    // startNetwork(START_WLAN);
    loadPanConfig();
    // if (curMode != MODE_NONE) startNetwork(START_PAN);
    startRMIServer();
    BrickFinder.startDiscoveryServer(false);
    //BrickFinder.startDiscoveryServer(curMode == MODE_APP);
    setTime();
    closeDisplay();
  }

  @Override
  public String getSetting(String key, String defaultValue) {
    switch (key) {
    case "wlan0":
    case "br0":
      return getInetAddress(key);
    case "ssid":
      return getAccessPointName("wlan0");
    case "lejos.ntp":
      return getProperty(key, defaultValue);
    case "Pan.mode":
      return modeIDS[curMode];
    case "Pan.address":
      return IPAddresses[0];
    case "Pan.netmask":
      return IPAddresses[1];
    case "Pan.broadcast":
      return IPAddresses[2];
    case "Pan.gateway":
      return IPAddresses[3];
    case "Pan.dns":
      return IPAddresses[4];
    case "Pan.persist":
      return persist;
    case "Pan.service":
      return BTService;
    case "Pan.apname":
      return this.BTAPName;
    case "Pan.apaddress":
      return this.BTAPAddress;

    }
    return null;
  }

  @Override
  public List<String> getList(String list, String parameter) {
    switch (list) {
    case "ACCESSPOINTS":
      return Arrays.asList(Wifi.getLocalDevice("wlan0").getAccessPointNames());
    default:
      return null;
    }
  }

  @Override
  public List<String> execute(String command, String target, String... arguments) {
    switch (command) {
    case "CONNECT": {
      openDisplay();
      display("Saving/nconfiguration");
      WPASupplicant.writeConfiguration(WIFI_BASE, WIFI_CONFIG, target, arguments[0]);
      startNetwork(START_WLAN);
      startRMIServer();
      setTime();
      this.broadcast("ssid", "");
      this.broadcast("wlan0", "");
      closeDisplay();
      return null;
    }
    case "PAN_APPLY": {
      savePanConfig();
      startNetwork(START_PAN);
      startRMIServer();
      startDiscoveryServer();
      setTime();
      broadcast("br0", "");
      return null;
    }
    default:
      return null;
    }
  }

  @Override
  public void setSetting(String key, String value) {
    switch (key) {
    case "Pan.mode": {
      initPan(getCurMode(value));
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
    case "lejos.ntp": {
      setProperty(key, value);
      setTime();
      break;
    }
    default:
      return;
    }
    broadcast(key, value);
  }

  /*
   * Shared
   */

  private void startNetwork(String startup) {
    run(startup);
    Delay.msDelay(2000);
  }

  private String getInetAddress(String wifiInterface) {
    {
      Enumeration<NetworkInterface> interfaces;
      try {
        interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
          NetworkInterface current = interfaces.nextElement();
          try {
            if (!current.isUp() || current.isLoopback() || current.isVirtual())
              continue;
          } catch (SocketException e) {
            System.err.println("Failed to get network properties: " + e);
          }
          Enumeration<InetAddress> addresses = current.getInetAddresses();
          while (addresses.hasMoreElements()) {
            InetAddress current_addr = addresses.nextElement();
            if (current_addr.isLoopbackAddress())
              continue;
            if (current.getName().equals(wifiInterface))
              return current_addr.getHostAddress();
          }
        }
      } catch (SocketException e) {
        System.err.println("Failed to get network interfaces: " + e);
      }
      return null;

    }
  }

  /*
   * RMI
   */

  private void startRMIServer() {
    // Start the RMI server
    System.out.println("Starting RMI");
    String rmiIP = getSetting("wlan0", getSetting("br0", "127.0.0.1"));
    // String rmiIP = (wlanAddress != null ? wlanAddress : (panAddress != null ?
    // panAddress : "127.0.0.1"));
    System.out.println("Setting java.rmi.server.hostname to " + rmiIP);
    System.setProperty("java.rmi.server.hostname", rmiIP);

    try { // special exception handler for registry creation
      LocateRegistry.createRegistry(1099);
      System.out.println("java RMI registry created.");
    } catch (RemoteException e) {
      // do nothing, error means registry already exists
      System.out.println("java RMI registry already exists.");
    }

    try {
      RMIRemoteEV3 ev3 = new RMIRemoteEV3();
      Naming.rebind("//localhost/RemoteEV3", ev3);
      RMIRemoteMenu remoteMenu = new RMIRemoteMenu(ModelContainer.getModel());
      Naming.rebind("//localhost/RemoteMenu", remoteMenu);
    } catch (Exception e) {
      System.err.println("RMI failed to start: " + e);
    }
  }

  /*
   * NTP
   */

  private void setTime() {
    openDisplay();
    display("Set time");
    try {
      String dt = NtpClient.getDate(ModelContainer.getModel().getSetting("lejos.ntp", "1.uk.pool.ntp.org"));
      Runtime.getRuntime().exec("date -s " + dt);
    } catch (IOException e) {
      closeDisplay();
      System.err.println("Failed to get time from ntp: " + e);
    }
    closeDisplay();
  }

  /*
   * Discovery server
   */

  private void startDiscoveryServer() {
    BrickFinder.stopDiscoveryServer();
    BrickFinder.startDiscoveryServer(curMode == MODE_APP);
  }

  /*
   * Wifi
   */

  /**
   * Return the current access point name
   * 
   * @return access point name
   */
  private String getAccessPointName(String ifName) {
    // Create buffer for the results

    reqP.point.flags = 0;
    reqP.point.length = 256;
    reqP.point.p = new Memory(reqP.point.length);
    // Copy the name to the request structure
    System.arraycopy(ifName.getBytes(), 0, reqP.ifname, 0, ifName.length());
    try {
      int ret = wifi.ioctl(NativeWifi.SIOCGIWESSID, reqP);
      // System.out.println("error " + ret);
      if (ret >= 0) {

        StringBuilder sb = new StringBuilder();
        int len = reqP.point.length;
        // System.out.println("length is " + len);
        for (int j = 0; j < len; j++) {
          sb.append((char) reqP.point.p.getByte(j));
        }

        // System.out.println("Access Point Name:" + sb.toString());

        return sb.toString();
      }
      return "";
    } catch (Exception e) {
      System.err.println("Failed to get SSID: " + e);
      return "";
    }
  }

  /*
   * PAN
   */

  private int getCurMode(String id) {
    for (int i = 0; i < modeIDS.length; i++) {
      if (id.equals(modeIDS[i]))
        return i;
    }
    return MODE_NONE;
  }

  private void savePanConfig() {
    System.out.println("Save PAN config");
    try {
      PrintWriter out = new PrintWriter(PAN_CONFIG);
      out.print(modeIDS[curMode] + " " + BTAPName.replace(" ", "\\ ") + " " + BTAPAddress);
      for (String ip : IPAddresses)
        out.print(" " + ip);
      out.print(" " + BTService + " " + persist);
      out.println();
      out.close();
      changed = false;
    } catch (IOException e) {
      System.out.println("Failed to write PAN config to " + PAN_CONFIG + ": " + e);
    }
  }

  private String getConfigString(String[] vals, int offset, String def) {
    if (vals == null || offset >= vals.length || vals[offset] == null || vals[offset].length() == 0)
      return def;
    return vals[offset];
  }

  public void loadPanConfig() {
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
    for (int i = 0; i < modeIDS.length; i++)
      if (modeIDS[i].equalsIgnoreCase(mode)) {
        curMode = i;
        break;
      }
    // be sure to convert no-break space back - ahem.
    BTAPName = getConfigString(vals, 1, anyAP).replace("\u00a0", " ");
    BTAPAddress = getConfigString(vals, 2, anyAP);
    for (int i = 0; i < IPAddresses.length; i++)
      IPAddresses[i] = getConfigString(vals, i + 3, autoIP);
    if (curMode == MODE_AP && IPAddresses[0].equals(autoIP))
      IPAddresses[0] = "10.0.1.1";
    BTService = getConfigString(vals, 8, "NAP");
    persist = getConfigString(vals, 9, "N");
    changed = false;
  }

  public void initPan(int mode) {
    if (mode != curMode) {
      for (int i = 0; i < IPAddresses.length; i++)
        IPAddresses[i] = autoIP;
      switch (mode) {
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
          initPan(MODE_NONE);
          return;
        }
        String[] parts = wlanAddress.split("\\.");
        if (parts.length == 4) {
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

}
