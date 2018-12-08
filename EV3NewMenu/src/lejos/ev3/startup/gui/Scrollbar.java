package lejos.ev3.startup.gui;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

public class Scrollbar {
	
	/* Scrollbar Bounds */
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	protected int _minimum;
	protected int _maximum;
	protected int _value;
	
	protected GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
	
	public Scrollbar( int x, int y, int width, int height, int min, int max, int val ) {
		setBounds(x, y, width, height);
		_minimum = min;
		_maximum = max;
		_value = val;
	}
	
	public void setBounds( int x, int y, int width, int height ) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}
	
	public void render() {
		/* Clear Out Rectangle */
		g.setColor( GraphicsLCD.WHITE );
		g.fillRect(x + 1, y + 1, w - 2, h - 2);
		g.setColor( GraphicsLCD.BLACK );
		
		/* Draw Rectangle */
		g.drawRect(x, y, w, h);
		
		/* Draw Value */
		if ( _maximum == _minimum ) return; // We're done here.
		int valueWidth = (int)((float)( _value - _minimum) / (float)( _maximum - _minimum ) * w);
		g.fillRect(x + valueWidth - 1, y, 3, h);
	}
	
	public static void main( String[] args ){
		Scrollbar bar = new Scrollbar( 20, 20, 100, 10, 0, 100, 50 );
		bar.render();
		Button.waitForAnyPress();
	}
	
}
