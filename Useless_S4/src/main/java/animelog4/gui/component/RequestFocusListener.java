package animelog4.gui.component;

import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class RequestFocusListener implements AncestorListener {
	private boolean removeListener;

	public RequestFocusListener() {
		this(true);
	}
	
	public RequestFocusListener(boolean removeListener) {
		this.removeListener = removeListener;
	}
	
	public void ancestorAdded(AncestorEvent e) {
		JComponent component = e.getComponent();
		component.requestFocusInWindow();

		if ( removeListener )
			component.removeAncestorListener(this);
	}
	
	public void ancestorMoved(AncestorEvent e) {}
	public void ancestorRemoved(AncestorEvent e) {}
	
}
