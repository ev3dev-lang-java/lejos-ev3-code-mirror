package lejos.ev3.menu.model;

import java.util.Arrays;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;

/** Model to maintain al settings that must be stored in a property file
 * @author Aswin Bouwmeester
 *
 */
public class AudioModel extends AbstractModel{

  protected AudioModel() {
    myKeys = Arrays.asList( "audio.volume", "lejos.keyclick_volume", "lejos.keyclick_length", "lejos.keyclick_frequency" );
  }
  


  @Override
  public void setSetting(String key, String value) {
    setProperty(key, value);
    broadcast(key, value);
      switch(key) {
      case "volume": {BrickFinder.getLocal().getAudio().setVolume(Integer.parseInt(value)); break;}
      case "lejos.keyclick_volume": {Button.setKeyClickVolume(Integer.parseInt(value));break;}
      case "lejos.keyclick_length": {Button.setKeyClickLength(Integer.parseInt(value)); break;}
      case "lejos.keyclick_frequency": {Button.setKeyClickTone(Button.ID_ENTER, Integer.parseInt(value));break;}
      case "lejos.ntp": {ModelContainer.getModel().execute("SET_TIME", " "); break;}
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

  @Override
  public String getSetting(String key, String defaultValue) {
    return getProperty(key, defaultValue);
  }

}
