package lejos.ev3.menu.viewer;

import java.util.ArrayList;
import java.util.List;

import lejos.ev3.menu.components.CompoundPanel;
import lejos.ev3.menu.components.Dialog;
import lejos.ev3.menu.components.Fonts;
import lejos.ev3.menu.components.Icons;
import lejos.ev3.menu.components.ImagePanel;
import lejos.ev3.menu.components.Panel;
import lejos.ev3.menu.components.TextPanel;
import lejos.ev3.menu.components.UI;
import lejos.ev3.menu.model.Model;
import lejos.ev3.menu.presenter.Detail;
import lejos.ev3.menu.presenter.Node;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.utility.Delay;

public class GrMenu implements Menu, WaitScreen {
  // TODO: work on suspend
  // TODO: work on system exit

  private List<Node> siblings;
  private Node       currentNode;
  private GraphicsLCD    canvas          = LocalEV3.get().getGraphicsLCD();
  private int            toprow          = 0;
  private List<Node> nodeStack = new ArrayList<Node>(0);
  private List<List<Node>>  siblingStack = new ArrayList<List<Node>>(0);
  private boolean        suspended       = false;
  private boolean        idle            = false;

  private TextPanel      statusBar;
  private CompoundPanel  title;
  private TextPanel      detailLine;
  private ImagePanel     selector;
  private Panel          detailPanel;
  private CompoundPanel  messagePanel;
  private Detail         currentDetail;
  private Panel          nodePanel;
  
  private Refresh        refresh;
  private int            refreshInterval = 3000;
  private Model model;
  private boolean messageVisible;
  private static Menu menu;



  public static Menu getMenu() {
    if (menu == null) menu = new GrMenu();
    return menu;
    
  }

  private GrMenu() {
    initializeScreenComponents();
    initializeRefreshThread();
  }

  protected void initializeScreenComponents() {
    statusBar = new TextPanel("Status bar", 0, 0);
    statusBar.setWidth(canvas.getWidth());
    statusBar.setFont(Fonts.Courier12);
    statusBar.setReverse(true);
    
    nodePanel = new Panel(0,statusBar.getBottom() + 1, canvas.getWidth(), canvas.getHeight() -  statusBar.getBottom() - 1);

    title = new CompoundPanel(Icons.EYE, "Title", 0, statusBar.getBottom() + 1);
    title = new CompoundPanel(Icons.EYE, "Title", 0, 0);
    title.setWidth(canvas.getWidth());
    title.setHeight(24);
    title.setHeight(33);
    title.setFont(Fonts.Courier17);
    title.setBorders(Panel.BOTTOMBORDER);
    title.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.VCENTER);

    detailPanel = new Panel(0, title.getBottom() + 3, canvas.getWidth(), canvas.getHeight() - title.getBottom() - 3);

    selector = new ImagePanel(Icons.ARROW_RIGHT);
    selector.setX(0);

    detailLine = new TextPanel("Detail");
    detailLine.setX(selector.getRight() + 2);
    detailLine.setWidth(canvas.getWidth() - selector.getRight() - 2);
    detailLine.setFont(Fonts.Courier13);
    detailLine.setTextAlign(GraphicsLCD.LEFT + GraphicsLCD.TOP);

    messagePanel = new CompoundPanel(Icons.HOUR_GLASS, "Wait\na\nsecond...");
    messagePanel.setBorders(15);
    messagePanel.setShadow(true);
    messagePanel.setWidth(168);
    messagePanel.setTextAlign(GraphicsLCD.HCENTER + GraphicsLCD.VCENTER);
    messagePanel.setShadow(true);
    }
    
  protected void initializeRefreshThread(){
    
    refresh = new Refresh();
    refresh.setDaemon(true);
    refresh.start();
  }
  
  
  public void runMenu(Node menuItem) {
    List<Node> m = new ArrayList<Node>();
    m.add(menuItem);
  }
  
  public void runMenu(List<Node> menuItems) {
    addCurrentMenuToStack();
    currentNode = menuItems.get(0);
    siblings = menuItems;
    {
    run();
    if (!nodeStack.isEmpty()) {
      getPreviousMenuFromStack();
      return;
    }
    } while(!dialog("Shut down EV3?", 3));
    canvas.clear();
  }
  
  protected void addCurrentMenuToStack() {
    if (currentNode != null) {
      nodeStack.add(currentNode);
      siblingStack.add(siblings);
    }
  }
  
  protected void getPreviousMenuFromStack() {
    if (!nodeStack.isEmpty()) {
      currentNode = nodeStack.remove(nodeStack.size() - 1);
      siblings = siblingStack.remove(siblingStack.size() - 1);
    } 
    else {
      currentNode = null;
      siblings = null;
    }
  }
  

  protected void run() {
    int currentKey = 0;
    paintNode();
    while (true) {
      if (!currentNode.isFresh()) paintNode();
      idle = true;
      currentKey = UI.getUI(Keys.ID_DOWN + Keys.ID_UP, 1000, 1000);
      idle = false;
      switch (currentKey) {
        case Button.ID_LEFT: {
          selectPreviousSibling();
          paintNode();
          break;
        }
        case Button.ID_RIGHT: {
          selectNextSibling();
          paintNode();
          break;
        }
        case Button.ID_UP: {
          currentNode.selectPreviousDetail();
          paintDetails();
          break;
        }
        case Button.ID_DOWN: {
          currentNode.selectNextDetail();
          paintDetails();
          break;
        }
        case Button.ID_ESCAPE: {
          return;
        }
        case Button.ID_ENTER: {
          currentNode.getSelected().select();
          break;
        }
        }
      }
  }
  
  
  public Detail selectFromList(Node list ) {
    addCurrentMenuToStack();
    currentNode = list;
    siblings = null;
    paintNode();
    int current = 0;
    while (true) {
      idle = true;
      current = UI.getUI(Keys.ID_DOWN + Keys.ID_UP, 1000, 1000);
      idle = false;
      switch (current) {
        case Button.ID_UP: {
          currentNode.selectPreviousDetail();
          paintDetails();
          break;
        }
        case Button.ID_DOWN: {
          currentNode.selectNextDetail();
          paintDetails();
          break;
        }
        case Button.ID_ESCAPE: {
          getPreviousMenuFromStack();
          paintNode();
          return null;
        }
        case Button.ID_ENTER: {
          if (currentNode.getSelected() != null) {
            currentNode.getSelected().select();
            Detail selected = currentNode.getSelected();
            getPreviousMenuFromStack();
            paintNode();
            return selected;
          }
        }
        }
      }
  }
  

  private void selectNextSibling() {
    int iNode = siblings.indexOf(currentNode);
    if (iNode < siblings.size() - 1)
      currentNode = siblings.get(iNode + 1);
    else
      currentNode = siblings.get(0);
  }

  private void selectPreviousSibling() {
    int iNode = siblings.indexOf(currentNode);
    if (iNode > 0)
      currentNode = siblings.get(iNode - 1);
    else
      currentNode = siblings.get(siblings.size()-1);
  }
  
  private void paintNode() {
    title.setMessage(currentNode.getLabel());
    title.setIcon(currentNode.getIcon());
    nodePanel.clear();
    title.paint();
    paintDetails();
  }

  private void paintDetails() {
    selector.clear();
    detailPanel.paint();
    int y = detailPanel.getY();
    int i = 0;
    List<Detail> details = currentNode.getDetails();
    Detail currentDetail = currentNode.getSelected();
    int selectedIndex = currentNode.getSelectedIndex();
    if (selectedIndex < toprow && selectedIndex !=-1) toprow = selectedIndex;
    boolean selectedIsVisible =false;
    while(y + detailLine.getFont().getHeight() < detailPanel.getBottom() && i + toprow < details.size()) {
        Detail d = details.get(i + toprow);
        detailLine.setMessage(split(d.toString()));
        detailLine.setY(y);
        detailLine.paint();
        y += detailLine.getHeight();
        if (d==currentDetail) {
          selector.setY(detailLine.getVCenter()-selector.getHeight()/2);
          selector.paint();
          selectedIsVisible = true;
        }
        i++;
    }
    if (currentDetail != null && !selectedIsVisible) {
      toprow ++;
      paintDetails();
      
    }
  }
  
  private String[] split(String text) {
    int max = detailLine.getWidth() / detailLine.getFont().width;
    if (text.length() <= max) {
      return new String[]{text}; 
    }
    else {
      max=max-1;
      int lines = (int) Math.ceil((double)text.length() / (double)(max));
      String[] ret = new String[lines];
      for (int i =0;i<lines;i++) {
        if (i==0)
          ret[i]=text.substring(0, max+1);
        else if (i<lines-1)
          ret[i]=" "+ text.substring(i*max+1, (i+1) * max+1);
        else 
          ret[i]=" "+ text.substring(i*max+1);
      }
      return ret;
    }
    
  }

  
  @Override
  public boolean dialog(String text, int buttons) {
    return Dialog.display(text, buttons);
  }

  @Override
  public boolean isSuspended() {
    return suspended;
  }

  @Override
  public void suspendMenu() {
    suspended = true;
    messagePanel.paint();
    canvas.refresh();
    canvas.setAutoRefresh(false);
  }

  @Override
  public void resumeMenu() {
    suspended = false;
    canvas.setAutoRefresh(true);
    paintNode();
  }

  
  public void progress(String text) {
    messagePanel.setMessage(text);
    messagePanel.paint();
  }


  class Refresh extends Thread {
    long lastTime = System.currentTimeMillis();
           
    @Override
    public void run(){  
      // TODO: Make thread safe
      while (true) {
        Delay.msDelay(refreshInterval);
        if (idle && !suspended) {
          for (Detail detail : currentNode.getDetails())
            if (idle && (detail.isAutoFefresh() || !detail.isFresh())) {
              paintDetails();
              break;
            }
        }
      }
      
    }
}


  @Override
  public void openMsgBox() {
    messageVisible = true;
    messagePanel.saveScreen();
    messagePanel.paint();
  }

  @Override
  public void closeMsgBox() {
    messagePanel.restoreScreen();
    messageVisible = false;
  }

  @Override
  public void msgBoxSetText(String text) {
    messagePanel.setMessage(text);
    if (messageVisible) {
    messagePanel.paint();
    canvas.refresh();
    }
  }

  @Override
  public void setEnvironment(Model model) {
    if (this.model != null) 
      this.model.detach(this);
    this.model = model;
    this.model.attach(this);
  }

  @Override
  public void msgBoxSetIcon(Image icon) {
    messagePanel.setIcon(icon);
    if (messageVisible) {
    messagePanel.paint();
    canvas.refresh();
    }
  }

  
}
