package lejos.ev3.menu.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lejos.hardware.ev3.LocalEV3;

/** Model to maintain Linux and hardware related settings and functions
 * @author Aswin Bouwmeester
 *
 */
public class SystemModel extends AbstractModel{
  SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
  
  
  
  protected SystemModel() {
    myKeys = Arrays.asList("system.hostname", "lejos.version","system.time","system.volt","system.current");

  }
  
  public String getHostname() { 
    List<String> f;
    try {
      f = Files.readAllLines(Paths.get("/etc/hostname"), Charset.defaultCharset());
      return f.get(0);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public String getVersion() { 
    List<String> f;
    try {
      f = Files.readAllLines(Paths.get("/home/root/lejos/version"), Charset.defaultCharset());
      return f.get(0);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getSetting(String key, String defaultValue) {
    switch(key) {
    case "system.hostname" : return getHostname();
    case "lejos.version" : return getVersion();
    case "system.time" : return format.format(Calendar.getInstance().getTime());
    case "system.volt" : return String.format("%.2f",LocalEV3.get().getPower().getVoltage());
    case "system.current" : return String.format("%.2f",LocalEV3.get().getPower().getBatteryCurrent());
    }
    return null;
  }

  @Override
  public void setSetting(String key, String value) {
    switch(key) {
    case "system.hostname" : {setHostname(value); break;}
  }
}

  private void setHostname(String name) {
    try {
      PrintStream out = new PrintStream(new FileOutputStream("/etc/hostname"));
      out.println(name);
      out.close();
      broadcast("system.hostname", name);
    } catch (FileNotFoundException e) {
      System.err.println("Failed to write to /etc/hostname: " + e);
    }
  }

  @Override
  public List<String> execute(String command, String target, String... arguments) {
    return null;
  }

  @Override
  public List<String> getList(String list, String parameter) {
    return null;
  }
}
