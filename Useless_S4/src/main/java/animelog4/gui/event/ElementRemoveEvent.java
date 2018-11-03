package animelog4.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALTable;
import animelog4.gui.view.BasePanel;
import animelog4.gui.view.TypePanel;

public class ElementRemoveEvent {
	
	private void doRemove() {		
		BasePanel bp = BasePanel.getInstance();
		boolean isTVAPanelOn = bp.isTVAPanelOn();
		TypePanel tp = bp.getElementPanel();
		ALTable table = tp.getTable();
		TypeCollection tc = TypeCollection.getInstance();
		
		final int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
		String s = String.format("[ %s ]\n정말 삭제하시겠습니까?", (String) table.getModel().getValueAt(selectedRow, 1));
		int ans = JOptionPane.showConfirmDialog(bp, s, "삭제", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if ( ans != JOptionPane.YES_OPTION ) return;
		
		if ( isTVAPanelOn ) {
			String address = (String) table.getModel().getValueAt(selectedRow, 6);
			tc.removeTVAByAddress(address);
		}
		else {
			String address = (String) table.getModel().getValueAt(selectedRow, 5);
			tc.removeMovieByAddress(address);
		}
		table.getDefaultTableModel().removeRow(selectedRow);
	}
	
	public ActionListener getActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemove();
			}
		};
	}
	
	public KeyListener getKeyListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ( e.getKeyCode() == KeyEvent.VK_DELETE )
					doRemove();
			}
		};
	}
}
