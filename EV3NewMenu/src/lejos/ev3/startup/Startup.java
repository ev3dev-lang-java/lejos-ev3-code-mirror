package lejos.ev3.startup;

import lejos.ev3.startup.gui.DetailMenu;
import lejos.ev3.startup.gui.Submenu;

public class Startup {
	
	public static void main( String[] args ) {
		DetailMenu menu = new DetailMenu();
		menu.setItems( new Submenu[] {
			new WiFiMenu(),
			new BluetoothMenu(),
			new SystemMenu()
		});
		menu.start();
	}
	
}
