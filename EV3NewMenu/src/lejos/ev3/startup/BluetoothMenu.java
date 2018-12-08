package lejos.ev3.startup;

import lejos.ev3.startup.gui.Submenu;
import lejos.hardware.lcd.Image;

public class BluetoothMenu extends Submenu {
	
	static final String ICBLUETOOTH = "\u0000\u00f0\u000f\u0000\u0000\u00fc\u003f\u0000\u0000\u00ff\u00ff\u0000\u0080\u00ff\u00ff\u0001\u00c0\u00bf\u00ff\u0003\u00c0\u003f\u00ff\u0003\u00e0\u003f\u00fe\u0007\u00e0\u003f\u00fc\u0007\u00f0\u003e\u00f8\u000f\u00f0\u003c\u00f0\u000f\u00f0\u0038\u00e3\u000f\u00f0\u0031\u00c7\u000f\u00f0\u0023\u00c7\u000f\u00f0\u0007\u00e3\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f8\u000f\u00f0\u000f\u00f8\u000f\u00f0\u000f\u00f0\u000f\u00f0\u0007\u00e3\u000f\u00f0\u0023\u00c7\u000f\u00f0\u0031\u00c7\u000f\u00f0\u0038\u00e3\u000f\u00f0\u003c\u00f0\u000f\u00f0\u003e\u00f8\u000f\u00e0\u003f\u00fc\u0007\u00e0\u003f\u00fe\u0007\u00c0\u003f\u00ff\u0003\u00c0\u00bf\u00ff\u0003\u0080\u00ff\u00ff\u0001\u0000\u00ff\u00ff\u0000\u0000\u00fc\u003f\u0000\u0000\u00f0\u000f\u0000";

	@Override
	public String getTitle() {
		return "Bluetooth";
	}

	@Override
	public Image getIcon() {
		return new Image(32, 32, Utils.stringToBytes8( ICBLUETOOTH ) );
	}

	@Override
	public String[] getDetails() {
		return new String[] { "Detail 1", "Detail 2", "Detail 3", "Detail 4", "Detail 5", "Detail 6"};
	}

	@Override
	public Image[] getItems() {
		return null;
	}

	@Override
	public int select(int selection) {
		// TODO Auto-generated method stub
		return 0;
	}

}
