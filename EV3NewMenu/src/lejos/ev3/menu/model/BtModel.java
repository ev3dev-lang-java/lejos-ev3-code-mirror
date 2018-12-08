package lejos.ev3.menu.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lejos.hardware.BluetoothException;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.RemoteBTDevice;

/** This model controls all Bluetooth related stuff
 * @author Aswin Bouwmeester
 *
 */
public class BtModel extends AbstractModel {
  LocalBTDevice bt;
  private Collection<RemoteBTDevice> cachedPaired;
  private Collection<RemoteBTDevice> cachedRemote;
  

  
  protected BtModel(){
    myKeys = Arrays.asList("bluetooth.visibility", "bluetooth.address", "bluetooth.name", "bluetooth.remote_address", "bluetooth.pin" );
    myCommands = Arrays.asList("PAIR", "FORGET" );
    myLists = Arrays.asList("PAIRED_DEVICES", "REMOTE_DEVICES" );
  }
  
  
  
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    super.initialize();
    openDisplay();
    display("Start/nBluetooth");
    bt = new LocalBTDevice();
    closeDisplay();
  }




  public String getSetting(String key, String defaultValue) {
    switch(key.split("\\.")[1]) {
    case "pin" : return getProperty(key, defaultValue);
    case "address" :  return bt.getBluetoothAddress();
    case "visibility" : return Boolean.toString(bt.getVisibility());
    case "name" : return bt.getFriendlyName(); 
    case "remote_address": return toAddress(defaultValue);
    }
  return null;
  }
  
  public void setSetting(String key, String value) {
    switch(key.split("\\.")[1]) {
    case "visibility" : { bt.setVisibility(Boolean.parseBoolean(value)); break;}
    case "pin" : { setProperty(key, value); break;}
    default: return;
    }
    broadcast(key, value); 
  }
  
  
  @Override
  public List<String> getList(String list, String parameter) {
    switch(list) {
    case "PAIRED_DEVICES": {
      try {
      cachedPaired = bt.getPairedDevices();
      return toNameList(cachedPaired);
      }
      catch(BluetoothException e) {
        System.err.println("Search exeception " + e);
      return null;
    }
    }
    case "REMOTE_DEVICES": {
      try {
      cachedRemote =  bt.search();
      return toNameList(cachedRemote);
      }
      catch(BluetoothException e) {
        System.err.println("Search exeception " + e);
      return null;
    }
    }
    }
    return null;
  }


  private List<String> toNameList(Collection<RemoteBTDevice> b) {
    List<String> a = new ArrayList<String>();
    for (RemoteBTDevice device : b) a.add(device.getName());
    return a;
  }

  public List<String> execute(String command, String name, String... arguments) {
    switch (command) {
    case ("PAIR"): { 
      try {
        bt.authenticate(toAddress(this.cachedRemote, name), arguments[0]);
        broadcast("REMOTE_DEVICES","");
        broadcast("PAIRED_DEVICES","");
        } catch (BluetoothException e) {
          System.err.println("Failed to set visibility: " + e);
          return getStackTrace(e);
      }
      break;
      }
    case ("FORGET"): {
      try {
        bt.removeDevice(toAddress(this.cachedPaired, name));
        broadcast("REMOTE_DEVICES","");
        broadcast("PAIRED_DEVICES","");
        } catch (BluetoothException e) {
          System.err.println("Failed to unpair device: " + e);
          return getStackTrace(e);
      }
      break;
    }
    
    }
    return null;
  }
  
private String toAddress(Collection<RemoteBTDevice> collection, String name) {
  for (RemoteBTDevice device : collection) 
    if(device.getName().equals(name)) return device.getAddress();
  return null;
}

private String toAddress( String name) {
  String address = toAddress(cachedPaired, name);
  if (address != null) return address;
  return toAddress(cachedRemote, name);
}



}
