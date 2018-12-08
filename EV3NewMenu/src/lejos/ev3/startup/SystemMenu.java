package lejos.ev3.startup;

import lejos.ev3.startup.gui.Submenu;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

public class SystemMenu extends Submenu {
	
	static final String ICEV3 = "\u00c0\u00ff\u00ff\u0003\u0040\u0000\u0000\u0002\u0060\u00ff\u00ff\u0006\u0060\u0001\u0080\u0006\u0060\u0001\u0080\u0006\u0060\u0001\u0080\u0006\u0060\u0003\u0080\u0006\u0060\u0001\u0080\u0006\u0060\u0003\u0080\u0006\u0060\u0005\u0080\u0006\u0060\u000b\u0080\u0006\u0060\u0055\u0080\u0006\u0060\u00ff\u00ff\u0006\u0060\u0000\u0000\u0006\u00e0\u00ff\u00ff\u0007\u00a0\u000f\u0000\u0004\u00a0\u0087\u0001\u0004\u0020\u00c0\u0003\u0004\u0020\u00e0\u0007\u0004\u0020\u0040\u0002\u0004\u0020\u00bc\u003d\u0004\u0020\u00bc\u003d\u0004\u0020\u0040\u0002\u0004\u0060\u00e0\u0007\u0004\u00a0\u00c0\u0003\u0004\u0060\u0080\u0001\u0004\u00a0\u0002\u0000\u0004\u0060\u0015\u0000\u0006\u00c0\u00ff\u00ff\u0003\u00c0\u00ea\u00bf\u0003\u00c0\u00f5\u007f\u0003\u00c0\u00ff\u00ff\u0003";

	static final String ICSOUND = "\u0000\u0003\u0080\u001a\u0040\34\u0020\u0042\u001f\u004a\u0009\u0052\u0009\u0052\u001b\u0052\r\u0052\u001b\u0052\u002d\u0052\u005f\u004b\u00a0\u0042\u0040\u0023\u0080\u001a\u0000\u0003";

	protected GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
	
	@Override
	public String getTitle() {
		return "System";
	}

	@Override
	public Image getIcon() {
		return new Image(32, 32, Utils.stringToBytes8( ICEV3 ) );
	}

	@Override
	public String[] getDetails() {
		return new String[] {
			"Menu Version: " + Utils.versionToString(Config.VERSION)
		};
	}

	@Override
	public Image[] getItems() {
		return new Image[] {
			new Image( 16, 16, Utils.stringToBytes8( ICSOUND ) )
		};
	}

	@Override
	public int select(int selection) {
		
		switch ( selection ) {
			case 0:
				volumeMenu();
				break;
		}
		
		return 0;
	}
	
	public void volumeMenu() {
		
		String[] volumes = new String[] { "Volume", "Keys", "Freq", "Length" };
		
		g.drawRect(31, 21, 118, 88);
		g.setColor( GraphicsLCD.WHITE);
		g.fillRect(30, 20, 118, 88);
		g.setColor( GraphicsLCD.BLACK);
		g.drawRect(30, 20, 118, 88);
		
		g.setFont( Font.getSmallFont() );
		
		for( int i = 0; i < volumes.length; i++ ) {
			g.drawString( volumes[i], 35, 27 + 20 * i, GraphicsLCD.LEFT | GraphicsLCD.TOP );
		}
		
		Button.waitForAnyPress();
	}

}
