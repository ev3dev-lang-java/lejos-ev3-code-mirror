package lejos.ev3.menu.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import lejos.remote.ev3.Menu;
import lejos.remote.ev3.RMIMenu;

public class RMIRemoteMenu extends UnicastRemoteObject implements RMIMenu {
	private Menu menu;
	private Model model;
	
	private static final long serialVersionUID = 9132686914626791288L;

	protected RMIRemoteMenu(Menu menu) throws RemoteException {
		super(0);
		this.menu = menu;
	}

	 protected RMIRemoteMenu(Model model) throws RemoteException {
	    super(0);
	    this.model = model;
	  }

	
	@Override
	public void runProgram(String programName) throws RemoteException {
		//menu.runProgram(programName);
		model.execute("RUN_PROGRAM", programName);
	}

	@Override
	public boolean deleteFile(String fileName) throws RemoteException {
		// return menu.deleteFile(fileName);
		return (model.execute("DELETE", fileName)) == null ? false : true;
	}

	@Override
	public String[] getProgramNames() throws RemoteException {
		//return menu.getProgramNames();
		List<String> l = model.getList("GET_FILES", "/home/lejos/programs");
		return l.toArray(new String[]{});
	}

	@Override
	public void debugProgram(String programName) throws RemoteException {
		//menu.debugProgram(programName);
    model.execute("DEBUG_PROGRAM", programName);
	}

	@Override
	public void runSample(String programName) throws RemoteException {
		menu.runSample(programName);
    model.execute("RUN_SAMPLE", programName);

	}

	@Override
	public String[] getSampleNames() throws RemoteException {
		//return menu.getSampleNames();
    List<String> l = model.getList("GET_FILES", "/home/root/lejos/samples");
    return l.toArray(new String[]{});
	}
	
	public long getFileSize(String filename) {
		//return menu.getFileSize(filename);
    // TODO: implement
		return 0;
	}

	@Override
	public boolean uploadFile(String fileName, byte[] contents)
			throws RemoteException {
		// return menu.uploadFile(fileName, contents);
    // TODO: implement
	  return false;
	}

	@Override
	public byte[] fetchFile(String fileName) throws RemoteException {
		//return menu.fetchFile(fileName);
    // TODO: implement
    return null;

	}

	@Override
	public String getSetting(String setting) throws RemoteException {
		//return menu.getSetting(setting);
		return model.getSetting(setting, "");
	}

	@Override
	public void setSetting(String setting, String value)
			throws RemoteException {
		//menu.setSetting(setting, value);
	  model.setSetting(setting,  value);
	}

	@Override
	public void deleteAllPrograms() throws RemoteException {
		//menu.deleteAllPrograms();
    // TODO: implement
	}

	@Override
	public String getVersion() throws RemoteException {
		//return menu.getVersion();
	  return model.getSetting("lejos.version",  "");
	}

	@Override
	public String getMenuVersion() throws RemoteException {
		// return menu.getMenuVersion();
    // TODO: implement
    return model.getSetting("lejos.version",  "");
	}

	@Override
	public String getName() throws RemoteException {
		//return menu.getName();
		return model.getSetting("system.hostname","");
	}

	@Override
	public void setName(String name) throws RemoteException {
		//menu.setName(name);
	  model.getSetting("system.hostname",name);
	}

	@Override
	public void configureWifi(String ssid, String pwd) throws RemoteException {
		//WPASupplicant.writeConfiguration("wpa_supplicant.txt",  "wpa_supplicant.conf", ssid, pwd);
	  model.execute("CONNECT", ssid, pwd);
	}

	@Override
	public void stopProgram() throws RemoteException {
		//menu.stopProgram();
	  model.execute("KILL_PROGRAM", "");
	}

	@Override
	public String getExecutingProgramName() throws RemoteException {
		//return menu.getExecutingProgramName();
	  // TODO: implement;
	  return null;
	}

	@Override
	public void shutdown() throws RemoteException {
		//menu.shutdown();
	  model.execute( "SHUTDOWN","");
	}

	@Override
	public void suspend() throws RemoteException {
		//menu.suspend();	
	  // TODO: implement?
	}

	@Override
	public void resume() throws RemoteException {
		//menu.resume();	
    // TODO: implement?
	}
}
