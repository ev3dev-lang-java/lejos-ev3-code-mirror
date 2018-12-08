package lejos.ev3.startup.gui;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.utility.Delay;

public class IconCarousel {
	
	public final static int VERTICAL = 0;
	public final static int HORIZONTAL = 1;
	
	public final static int BUTTON_POLL_INTERVAL = 10;
	
	/* Carousel Bounds */
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	/* Icon Data */
	protected Image[] _icons;
	protected int _iconH;
	protected int _iconW;
	
	/* Formating Data*/
	protected int _padding = 10;
	protected boolean _wrap = true;
	protected int _orientation = HORIZONTAL;
	
	/* */
	protected int _selected = 0;
	protected int _startTime = 0;
	protected boolean _quit = false;
	
	protected CarouselListener _listener;
	
	protected GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
	
	public IconCarousel( int x, int y, int width, int height ) {
		setBounds(x, y, width, height);
	}
	
	public void setBounds( int x, int y, int width, int height ) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}
	
	public void setIcons( Image[] icons, int width, int height ) {
		this._icons = icons;
		this._iconH = height;
		this._iconW = width;
	}
	
	public void setPadding( int padding ) {
		this._padding = padding;
	}
	
	public void setWrap( boolean wrap) {
		this._wrap = wrap;
	}
	
	public void setOrientation( int orientation ) {
		this._orientation = orientation;
	}
	
	public void setListener( CarouselListener listener ) { this._listener = listener; }
	
	public void setSelected( int selected ) { this._selected = selected; }
	public int getSelected() { return this._selected; }
	
	public int getTotal() { return ( _icons == null) ? 0 : _icons.length ; }
	
	public void resetTimeout() {
		_startTime = (int) System.currentTimeMillis();
	}
	
	public int select() {
		return select( -1 );
	}
	
	public int select( int timeout ) {
		if ( _icons == null ) return -1;
		int selected;
		do {
			selected = _selected;
			render( _selected, 0 );
			
			int button;
			do {				
				if (_quit)
					return -2; // quit by another thread
				
				if (timeout > 0 && System.currentTimeMillis() - _startTime >= timeout) 
					return -3; // timeout
				
                button = Button.waitForAnyPress(BUTTON_POLL_INTERVAL);
			} while (button == 0);
			
			switch( button ) {
				case Button.ID_UP:
				case Button.ID_LEFT:
					if ( _selected <= 0 && !_wrap) break;
					if ( --_selected < 0 ) _selected = _icons.length - 1;
					animate( selected, _selected, 1 );
					if ( _listener != null ) _listener.change( _selected );
					break;
				case Button.ID_DOWN:
				case Button.ID_RIGHT:
					if ( _selected >= _icons.length - 1 && !_wrap) break;
					if ( ++_selected >= _icons.length ) _selected = 0;
					animate( selected, _selected, -1 );
					if ( _listener != null ) _listener.change( _selected );
					break;
				case Button.ID_ENTER:
					return _selected;
				case Button.ID_ESCAPE:
					return -1;
			}
		} while( true );
	}
	
	/**
	 * Helper method to perform a tick based animation from one index to another
	 * @param last - <i>int</i> - Previous icon index
	 * @param next - <i>int</i> - Next icon index
	 * @param direction - <i>int</i> - Animation direction ( 1, -1 )
	 */
	protected void animate( int last, int next, int direction ) {
		double step = (_iconW + _padding) / 10.0;
		for ( int i = 0; i < 10; i++) {
			render( last, (int)(direction * i * step) );
			Delay.msDelay(16);
		}
		render( next, 0 );
	}
	
	public void render() {
		render( _selected, 0 );
	}
	
	/**
	 * Renders the IconCarousel centered at the index <i>selected</i>, and shifted by <i>offset</i> in pixels.
	 * @param selected - <i>int</i> - Icon index
	 * @param offset - <i>int</i> - Pixel offset
	 */
	protected void render( int selected, int offset ) {
		/* Clear Out Rectangle */
		g.setColor( GraphicsLCD.WHITE );
		g.fillRect(x, y, w, h);
		g.setColor( GraphicsLCD.BLACK );
		
		if ( _icons == null ) return;
		
		/* Figure out how many indexes we need to fill the space */
		int min = _iconW, indexSpan = 0;
		while ( min < w + 4 ) {
			min += _iconW + _padding;
			indexSpan++;
		}
		
		if ( offset != 0 ) indexSpan++;
		
		int index, di, sx = 0, sy = 0, dx, dy, dw = _iconW, dh = _iconH;
		for ( int i = -indexSpan; i <= indexSpan; i++) {
			
				/* Normalize the index */
			index = i + selected;
			if ( index < 0 || index >= _icons.length ) {
				if ( !_wrap ) continue; /* If we need to normalize, it means that this icon was wrapped. */
				while( index < 0 ) { index += _icons.length; }
				while( index >= _icons.length) { index -= _icons.length; }
			}
			
				/* Compute bitBlt variables */
			if ( _orientation == HORIZONTAL ) {
				di = (w / 2) + ( i * ( _iconW + _padding )) - (_iconW / 2) + offset;
				sx = ( di < 0 ) ? -di : 0; /* Offset Source X when image hides under left edge */
				dx = ( di < 0 ) ? 0 : di; /* Offset destination x when image hides under left edge */
				dy = (h / 2) - ( _iconH / 2 );
				dw = ( di < 0 ) ? _iconW + di : ((di > ( w - _iconW )) ? (w - di) : _iconW ); /* Make sure we only copy what we need */
			} else {
				di = (h / 2) + ( i * ( _iconH + _padding )) - (_iconH / 2) + offset;
				sy = ( di < 0 ) ? -di : 0;
				dy = (di < 0 ) ? 0 : di;
				dx = ( w / 2 ) - ( _iconW / 2 );
				dh = ( di < 0 ) ? _iconH + di : ((di > ( h - _iconH )) ? (h - di) : _iconW );
			}
			g.bitBlt( _icons[index].getData(), _iconW, _iconH, sx, sy, x + dx, y + dy, dw, dh, GraphicsLCD.ROP_COPY);
		}
		g.refresh();
	}
	
	public static interface CarouselListener {
		public void change( int selected );
	}
	
}
