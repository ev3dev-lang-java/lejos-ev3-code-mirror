package lejos.ev3.startup.gui;

import lejos.hardware.lcd.Image;

/**
 * 
 * Describes a submenu screen to be used by <i>DetailMenu</i> in displaying an icon based menu system. 
 * <i>DetailMenu</i> will call the methods defined in this class to retrieve the information that needs
 * to be displayed. This includes the title of the menu, the icon (32x32) for this menu, the names of 
 * this menu's items, and the icons (16x16) for those items.
 * 
 * See <i>DetailMenu</i> for more usage details
 * 
 * @author Abram Early
 *
 */
public abstract class Submenu {
	
	/**
	 * @return <i>String</i> - The title for this menu
	 */
	public abstract String getTitle();
	
	/**
	 * Returns a 32 x 32 pixel icon that represents this menu.
	 * @return <i>Image</i> - The icon for this menu
	 */
	public abstract Image getIcon();
	
	/**
	 * Returns an array of item names.
	 * @return <i>Image</i> - The items for this menu
	 */
	public abstract String[] getDetails();
	
	/**
	 * Returns an array of 16 x 16 pixel icons that represent this menus items.
	 * @return <i>Image[]</i> - The icons for the menu items
	 */
	public abstract Image[] getItems();
	
	/**
	 * Called when one of this menu's items is selected, or when this menu was selected if it doesn't contain any items
	 * @param selection <i>int</i> - The item that was selected from the menu
	 * @return <i>int</i> - Response code ( -1 to leave menu )
	 */
	public abstract int select( int selection );
	
}
