package lejos.ev3.menu.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Model for file related menu items
 * @author Aswin Bouwmeester
 *
 */
public class FilesModel extends AbstractModel {

  FilesModel() {
    myCommands = Arrays.asList("DELETE","VIEW");
    myLists = Arrays.asList("READ_FILE", "GET_FILES");
  };

  @Override
  public List<String> execute(String command, String path, String... arguments) {
    if (command.equals("VIEW"))
      return readFile(path);
    if (command.equals("DELETE"))
      return deleteFile(path);
    return null;
  }

  @Override
  public List<String> getList(String list, String parameter) {
    switch (list) {
    case "READ_FILE":
      return readFile(parameter);
    case "GET_FILES":
      return getFiles(parameter);
    }
    return null;
  }

  public boolean isDirectory(String path) {
    File f = new File(path);
    return f.isDirectory();
  }

  private List<String> deleteFile(String path) {
    try {
      Files.delete(Paths.get(path));
    } catch (IOException e) {
      return getStackTrace(e);
    }
    broadcast("GET_FILES", path.substring(0, path.lastIndexOf(java.io.File.separator)));
    return null;
  }

  private List<String> getFiles(String path) {
    List<String> entries = new ArrayList<String>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
      for (Path entry : stream) {
        if (Files.isDirectory(entry))
          entries.add(entry.toString()+" ");
        else 
          entries.add(entry.toString());
      }
    } catch (IOException e) {
      System.err.println("Failed to read directory:" + path + e);
    }
    return entries;

  }

  private List<String> readFile(String path) {
    try {
      return Files.readAllLines(Paths.get(path), Charset.defaultCharset());
    } catch (IOException e) {
      System.err.println("Failed to read file: " + path + e);
    }
    return null;
  }

  @Override
  public String getSetting(String key, String defaultValue) {
    // Files model has no settings
    return null;
  }

  @Override
  public void setSetting(String key, String value) {
    // Files model has no settings
  }

}
