package animelog4.gui.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Util {
	
	public ArrayList<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		ArrayList<Component> compList = new ArrayList<Component>();
		for ( Component comp : comps ) {
			compList.add(comp);
			if ( comp instanceof Container ) {
				compList.addAll(getAllComponents((Container) comp));
			}
		}
		return compList;
	}
	
	public KeyListener getKeyListener(final int keyCode, final EventToExecute ete) {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ( e.getKeyCode() == keyCode )
					ete.execute();
			}
		};
	}
	
	public MouseListener getMouseListener(final int clickCount, final EventToExecute ete) {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == clickCount )
					ete.execute();
			}
		};
	}
	
	public void addKeyEventToAllComponents(Container c, int keyCode, EventToExecute ete) {
		KeyListener keyListener = getKeyListener(keyCode, ete);
		for ( Component comp : getAllComponents(c) ) comp.addKeyListener(keyListener);
	}
	
	public void addMouseEventToAllComponents(Container c, int clickCount, EventToExecute ete) {
		MouseListener mouseListener = getMouseListener(clickCount, ete);
		for ( Component comp : getAllComponents(c) ) comp.addMouseListener(mouseListener);
	}
	
}
