package lejos.ev3.menu.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lejos.ev3.menu.viewer.WaitScreen;
import lejos.hardware.lcd.Image;

/** Skeleton implementation of a menu Model
 * @author Aswin Bouwmeester
 *
 */
public abstract class AbstractModel implements Model{
  private static Properties   props      = new Properties();
  private final static String PROPS_FILE = "/home/root/lejos/settings.properties";
  static {
    try {
      // load a properties file
      props.load(new FileInputStream(PROPS_FILE));
    } catch (FileNotFoundException e) {
      // Ignore
    } catch (IOException e) {
      System.err.println("Failed to load properties file");
    }
  }
  
  private List<ModelListener> listeners = new ArrayList<ModelListener>();
  protected List<String> myKeys = new ArrayList<String>(); 
  protected List<String> myCommands = new ArrayList<String>(); 
  protected List<String> myLists = new ArrayList<String>(); 
  private List<WaitScreen> waitListeners = new ArrayList<WaitScreen>();
  protected boolean initialized = false;
  
 protected static void setProperty(String key, String value) {
   props.setProperty(key, value);
   try {
     props.store(new FileOutputStream(PROPS_FILE), null);
   } catch (IOException e) {
     System.err.println("Failed to store properties");
   }
 }
 
 protected static String getProperty(String key, String defaultValue) {
   if (props.containsKey(key))
     return props.getProperty(key);
   return defaultValue;
 }
  
  
  
  @Override
  public void initialize() {
    initialized =true;
  }

  @Override
  public void terminate() {
  }

  @Override
  public boolean hasSetting(String key) {
    if (myKeys.contains(key)) return true;
    return false;
  }

  @Override
  public boolean canExecute(String command) {
    if (myCommands.contains(command)) return true;
    return false;
  }

  @Override
  public boolean canList(String list) {
    if (myLists.contains(list)) return true;
    return false;
  }

  @Override
  public void attach(String key, ModelListener listener) {
    listeners.add(listener);
  }

  @Override
  public void detach(String key, ModelListener listener) {
    listeners.remove(listener);
  }
  
  
  protected void broadcast(String id, String value) {
    for (ModelListener listener : listeners) {
      listener.keyChanged(id);
      listener.listChanged(id, value);
    }
  }

  @Override
  public void attach( WaitScreen listener) {
    waitListeners.add(listener);
  }

  @Override
  public void detach(WaitScreen listener) {
    waitListeners.remove(listener);
  }
  
  protected List<String> getStackTrace(Exception e) {
    List<String>target = new ArrayList<String>();
    StackTraceElement[] source = e.getStackTrace();
    for(StackTraceElement line : source) {
      target.add(line.toString());
    }
    return target;
  }

  protected void display(String text) {
    for (WaitScreen listener : waitListeners) {
      listener.msgBoxSetText(text);
    }
  }

  protected void openDisplay() {
    for (WaitScreen listener : waitListeners) {
      listener.openMsgBox();
    }
  }

  protected void closeDisplay() {
    for (WaitScreen listener : waitListeners) {
      listener.closeMsgBox();
    }
  }
  
  protected void openDisplay(Image icon) {
    for (WaitScreen listener : waitListeners) {
      listener.msgBoxSetIcon(icon);
      listener.openMsgBox();
    }
    
  }

  
  protected List<String> run(String script) {
    openDisplay();
    try {
        Process p = Runtime.getRuntime().exec(script);
          BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String statusMsg;
          while((statusMsg = input.readLine()) != null)
          {
              display(statusMsg);
          }
    int status = p.waitFor();
    System.out.println("Script returned" + status + " (" + script + ")");
    } catch (Exception e) {
    closeDisplay();
    System.err.println("Failed to execute: " + script + " : " + e);
    return this.getStackTrace(e);
  }
    closeDisplay();
    return null;
}


}
