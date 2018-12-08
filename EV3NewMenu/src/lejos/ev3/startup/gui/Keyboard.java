package lejos.ev3.startup.gui;
import lejos.ev3.startup.Utils;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.CustomFont;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;


public class Keyboard {
	
	private static Image ICOK = new Image(9, 12, Utils.stringToBytes8( "\0\0\0\0\0\0\u00ae\0\u00aa\0\u006a\0\u00aa\0\u00aa\0\u00ae\0\0\0\0\0\0\0" ) );
	private static Image ICSHIFT = new Image(9, 12, Utils.stringToBytes8( "\0\0\0\0\0\0\u0010\0\u0028\0\u0044\0\u0082\0\u0082\0\u007c\0\0\0\0\0\0\0" ) );
	private static Image ICSHIFTON = new Image( 9, 12, Utils.stringToBytes8( "\0\0\0\0\0\0\u0010\0\u0038\0\u007c\0\u00fe\0\u00fe\0\u007c\0\0\0\0\0\0\0" ) );
	
	private static GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
	
	private static String[][] keyboards = {
		{
		"1234567890@",
		"qwertyuiop<",
		"asdfghjkl.>",
		"^zxcvbnm  ^"
		},{
		"1234567890@",
		"QWERTYUIOP<",
		"ASDFGHJKL,>",
		"^ZXCVBNM  ^"
		},{
		"[]{}#%^*+=@",
		"-\\|:;()$@\"<",
		"~/_`&.,?!'>",
		"^     <>  ^"
		}};
	
	public static String getString() {
		
		String str = "";
		int sx = 0, sy = 0, keyboard = 0;
			
		do {
			lcd.setColor(GraphicsLCD.WHITE);
			lcd.fillRect(8, 46, 162, 82);
			lcd.setColor(GraphicsLCD.BLACK);
			lcd.drawRect(7, 45, 164, 84);
			lcd.drawLine( 6, 46, 6, 128);
			lcd.drawLine( 172, 46, 172, 128);
			
			lcd.setStrokeStyle(GraphicsLCD.DOTTED);
			lcd.setFont( CustomFont.getMediumFont() );
			
			String substr = str;
			if ( str.length() > 20 ){
				substr = str.substring( str.length() - 20 );
				lcd.drawString("<", 16, 60, GraphicsLCD.BOTTOM | GraphicsLCD.RIGHT);
			}
			lcd.drawString(substr, 15, 60, GraphicsLCD.BOTTOM);
			lcd.drawString(" ", 15 + lcd.getFont().stringWidth(substr), 60, GraphicsLCD.BOTTOM, true);
			
			lcd.drawLine( 15, 60, 163, 60);
			
			for ( int yi = 0; yi < 4; yi++ ){
				for ( int xi = 0; xi < 11; xi++ ){
					int x = xi * 14 + 16;
					int y = yi * ( lcd.getFont().getHeight() + 3 ) + 64;
					
						/* Draw Key Character */
					lcd.drawChar( keyboards[keyboard][yi].charAt(xi), x, y, 0);
					
						/* Draw Finish Key */
					if ( yi == 2 && xi == 10)
						lcd.drawImage( ICOK, x - 1, y, 0);
					
						/* Draw Shift Key */
					if ( yi == 3 && ( xi == 0 || xi == 10 ) )
						lcd.drawImage( ( keyboard == 1)? ICSHIFTON : ICSHIFT , x - 1, y, 0);
					
						/* Draw Space Bar*/
					if ( yi == 3 && xi == 8 ){
						lcd.drawRect(x - 2, y - 1, lcd.getFont().glyphWidth * 2 + 8, lcd.getFont().getHeight() + 1);
						if ( sy == 3 && sx >= 8 && sx <= 9 )
							lcd.fillRect(x - 1, y, lcd.getFont().glyphWidth * 2 + 7, lcd.getFont().getHeight());
					}
					
						/* Invert Key if Selected */
					if ( sx == xi && sy == yi  && !( sy == 3 && sx >= 8 && sx <= 9 ) )
						lcd.drawRegionRop( null, x - 1, y, 9, 12, x - 1, y, 0, GraphicsLCD.ROP_COPYINVERTED );
					
						/* Draw dotted boxes around action keys */
					if ( xi >= 10 || ( xi == 0 && yi == 3 ) )
						lcd.drawRect(x - 2, y - 1, lcd.getFont().glyphWidth + 2, lcd.getFont().getHeight() + 1);
				}
			}
			
			switch( Button.waitForAnyPress() ){
				case Button.ID_RIGHT:
					if (sy == 3 && sx == 8) sx++;
					if (sx < 10) sx++;
					else sx = 0; break;
				case Button.ID_LEFT:
					if (sy == 3 && sx == 9) sx--;
					if (sx > 0) sx--;
					else sx = 10; break;
				case Button.ID_UP:
					if (sy > 0) sy--;
					else sy = 3; break;
				case Button.ID_DOWN:
					if (sy < 3) sy++; 
					else sy = 0; break;
				case Button.ID_ENTER:
					if (sy == 3 && ( sx == 0 || sx == 10) ){ // Shift Key Pressed
						if ( keyboard == 0 ) keyboard = 1;
						else if ( keyboard == 1 ) keyboard = 0;
					} else if ( sy == 0 && sx == 10 ) { // Symbols Key Pressed
						if ( keyboard != 2 ) keyboard = 2;
						else keyboard = 0;
					} else if ( sy == 1 && sx == 10 ) { // Backspace Key Pressed
						if ( str.length() > 0 )
							str = str.substring( 0, str.length() - 1 );
					} else if ( sy == 2 && sx == 10) { // Finish Key Pressed
						return str; 
					} else // Character Key Pressed
						str += keyboards[keyboard][sy].substring(sx, sx + 1);
					break;
				case Button.ID_ESCAPE:
					return null;
			}
		
		} while (true);
	}

	public static void main(String[] args) {
		String s = Keyboard.getString();
		System.out.println("String is " + s);
	}
	
}
