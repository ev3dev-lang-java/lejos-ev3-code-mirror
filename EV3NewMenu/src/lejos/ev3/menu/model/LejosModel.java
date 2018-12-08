package lejos.ev3.menu.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

import lejos.ev3.menu.components.Icons;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.internal.ev3.EV3IOPort;
import lejos.utility.Delay;


public class LejosModel extends AbstractModel{
  // TODO: move "lejos.version" to this model
  private static final String JAVA_RUN_CP = "jrun -cp ";
  private static final String JAVA_DEBUG_CP = "jrun -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y -cp ";
  
  private static final String EV3_WRAPPER = "lejos.internal.ev3.EV3Wrapper";
  
  private Process program;
  private boolean forceDestruction = false;
  private boolean hasRunningProgram =false;

  
  
  protected LejosModel(){
    myKeys = Arrays.asList( "lejos.default_program","lejos.sleeptime","lejos.autorun_program");
    myCommands = Arrays.asList("CLOSE_PORTS","RUN_TOOL","RUN_PROGRAM","DEBUG_PROGRAM","RUN_SAMPLE","DELETE","KILL_PROGRAM" );
  }
  
  @Override
  public void setSetting(String key, String value) {
    setProperty(key, value);
    broadcast(key, value);
    }
  
  @Override
  public List<String> execute(String command, String target, String... arguments) {
    File file = new  File(target);
        switch(command) {
    case "RUN_TOOL": {
      try {
    JarMain.executeJar(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
      break;
    }
    case "RUN_PROGRAM":
    case "DEBUG_PROGRAM":
    case "RUN_SAMPLE" :
    {
    try {
      openDisplay(Icons.HOUR_GLASS);
      display("Start/nprogram");
      
      forceDestruction = false;
      hasRunningProgram = true;
      JarFile jar = new JarFile(file);
      String prefix = (command.equals("DEBUG_PROGRAM") ? JAVA_DEBUG_CP : JAVA_RUN_CP);
      String cmd = prefix + target.toString() + " " + EV3_WRAPPER + "  " + jar.getManifest().getMainAttributes().getValue("Main-class");
      jar.close();
      String directory = target.substring(0, target.lastIndexOf(java.io.File.separator)+1);
      UI ui = new UI();
      ui.setDaemon(true);
      ui.start();
      program = new ProcessBuilder(cmd.split(" ")).directory(new File(directory)).start();
        
        BufferedReader input = new BufferedReader(new InputStreamReader(program.getInputStream()));
        BufferedReader err= new BufferedReader(new InputStreamReader(program.getErrorStream()));
            
        EchoThread echoIn = new EchoThread(target.toString().replace(".jar", ".out"), input, System.out);
        EchoThread echoErr = new EchoThread(target.toString().replace(".jar", ".err"), err, System.err);
            
        echoIn.start(); 
        echoErr.start();
        closeDisplay();
            
        while(true) {
          if (forceDestruction) {
            program.destroy(); 
            break;
          }
          if (!echoIn.isAlive() && !echoErr.isAlive()) break;           
          Delay.msDelay(200);
        }
            
        program.waitFor();
        hasRunningProgram = false;
        program = null;
        ui=null;
        broadcast("GET_FILES","");
 
      } catch (Exception e) {
        hasRunningProgram = false;
        e.printStackTrace();
        return getStackTrace(e);
    }
      break;
    }
    case "DELETE": {
      try {
        Files.delete(Paths.get(target));
        broadcast("GET_FILES","");
      } catch (IOException e) {
        return getStackTrace(e);
      }
      break;
    }
    case "CLOSE_PORTS": {EV3IOPort.closeAll(); break;}
    case "KILL_PROGRAM": {forceDestruction = true;break;}
    }
    return null;
  }


  @Override
  public String getSetting(String key, String defaultValue) {
    return getProperty(key, defaultValue);
  }


  @Override
  public List<String> getList(String list, String parameter) {
    return null;
  }
  
  class UI extends Thread {
    private Keys keys = BrickFinder.getLocal().getKeys();

    @Override
    public void run(){  
      while (! forceDestruction ) {
        Delay.msDelay(10);
//        if (keys.getButtons()  == (Button.ID_LEFT + Button.ID_RIGHT)) {
          if (Button.readButtons()  == (Button.ID_LEFT + Button.ID_RIGHT)) {
          forceDestruction = true;
            }
        }
      }
}  
  

}
