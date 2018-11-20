package animelog4.gui.component;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ALList<T> extends JList<T> {
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<T> dlm;
	
	public ALList(ArrayList<T> arrayList) {
		super(new DefaultListModel<T>());
		dlm = (DefaultListModel<T>) getModel();
		for ( T t : arrayList ) dlm.addElement(t);
	}
	
	public DefaultListModel<T> getDefaultListModel() {
		return dlm;
	}

}
