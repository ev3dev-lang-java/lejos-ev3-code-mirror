package lejos.ev3.startup.gui;

import lejos.ev3.startup.Utils;
import lejos.ev3.startup.gui.IconCarousel.CarouselListener;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

/**
 * Displays an icon based menu system with a vertical scrolling icon carousel on the left side of the screen to select the major menus,
 * and each major menu has a title, a couple lines of details, and another horizontal icon carousel on the right side of the screen. As
 * the vertical icons are scrolled, the right side of the screen changes to match the selected menu. When the icon is selected, the horizontal 
 * icon carousel is then selected for scrolling. 
 * 
 * All functionality and information for displaying the menus and responding to input is defined in the <i>Submenu</i> classes that are passed 
 * into the <i>DetailMenu</i>.
 * 
 * @author Legoabram
 *
 */
public class DetailMenu implements CarouselListener {
	
	private static final Image ARROW_LEFT = new Image(7, 10, Utils.stringToBytes8( "\u0030\u0048\u0044\u0042\u0041\u0041\u0042\u0044\u0048\u0030" ));
	private static final Image ARROW_RIGHT = new Image(7, 10, Utils.stringToBytes8( "\u0006\u0009\u0011\u0021\u0041\u0041\u0021\u0011\u0009\u0006" ));
	private static final Image ARROW_UP = new Image(10, 7, Utils.stringToBytes8( "\u0030\u0000\u0048\u0000\u0084\u0000\u0002\u0001\u0001\u0002\u0001\u0002\u00fe\u0001" ));
	
	protected Submenu[] _submenus;
	
	protected IconCarousel sidebar;
	protected IconCarousel bottombar;
	protected int _activebar = 0;

	protected GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
	
	public DetailMenu () {
		
		sidebar = new IconCarousel( 5, 11, 34, 117 );
		sidebar.setPadding(4);
		sidebar.setWrap(true);
		sidebar.setOrientation(IconCarousel.VERTICAL);
		sidebar.setListener( this );
		
		bottombar = new IconCarousel( 65, 100, 92, 16 );
		bottombar.setPadding(2);
		bottombar.setWrap(true);
		bottombar.setOrientation(IconCarousel.HORIZONTAL);
		
	}
	
	public void start () {
		int selectA = 0, selectB = 0;
		do {
			_activebar = 1;
			render();
			selectA = sidebar.select();
			if ( selectA >= 0 && selectA < _submenus.length) {
				Submenu menu = _submenus[ selectA ];
				bottombar.setIcons( menu.getItems(), 16, 16 );
				_activebar = 2;
				do {
					render();
					selectB = bottombar.select();
					menu.select( selectB );
				} while( selectB >= 0 );
			}
		} while( selectA >= 0 );
	}
	
	public void render() {
		Submenu menu = _submenus[sidebar.getSelected()];
		
			/* Clear Region */
		g.setColor( GraphicsLCD.WHITE );
		g.fillRect(39, 12, 130, 115);
		g.setColor( GraphicsLCD.BLACK );
		
		if ( _activebar == 1)
			g.drawRegion( ARROW_LEFT, 0, 0, 7, 10, 0, 40, 70, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
		else {
			g.drawRegion( ARROW_UP, 0, 0, 10, 7, 0, 111, 118, GraphicsLCD.HCENTER | GraphicsLCD.TOP);
			g.drawRegion( ARROW_LEFT, 0, 0, 7, 10, 0, 65, 108, GraphicsLCD.VCENTER | GraphicsLCD.RIGHT);
			g.drawRegion( ARROW_RIGHT, 0, 0, 7, 10, 0, 157, 108, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
		}
		
			/* Draw Menu Title */
		g.setFont( Font.getDefaultFont() );
		g.drawString( menu.getTitle(), 111, 20, GraphicsLCD.HCENTER );
		
			/* Draw Details */
		String[] details = menu.getDetails();
		if ( details != null ){
			g.setFont( Font.getSmallFont() );
			int fontHeight = g.getFont().getHeight() + 2;
			for ( int i = 0; i < Math.min( details.length, 5 ); i++ ) {
				g.drawString( details[i], 111, 40 + fontHeight * i, GraphicsLCD.HCENTER );
			}
		}
		
			/* Draw Menu Items */
		bottombar.render();
	}
	
	public void setItems( Submenu[] submenus ) {
		this._submenus = submenus;
		// TODO: Make sure selected menu is valid
		
		/* Collect Menu Icons */
		Image[] icons = new Image[submenus.length];
		for( int i = 0; i < submenus.length; i++ ) {
			icons[i] = submenus[i].getIcon();
		}
		sidebar.setIcons( icons, 32, 32 );
		bottombar.setIcons( submenus[ sidebar.getSelected() ].getItems(), 16, 16 );
		
		render();
	}
	
	@Override
	public void change(int selected) {
		Submenu menu = _submenus[ selected ];
		bottombar.setIcons( menu.getItems(), 16, 16 );
		render();
	}
	
}
