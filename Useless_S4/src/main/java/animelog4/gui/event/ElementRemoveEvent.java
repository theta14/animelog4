package animelog4.gui.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.view.BasePanel;
import animelog4.gui.view.MoviePanel;
import animelog4.gui.view.TVAPanel;
import animelog4.gui.view.TypePanel;

public class ElementRemoveEvent {
	private TypePanel tp;
	private Component c;
	private int numberOfLinkedMovies = -1;
	
	public ElementRemoveEvent() {
		c = BasePanel.getInstance();
		tp = null;
	}
	
	public ElementRemoveEvent(TypePanel tp, Component c) {
		this.tp = tp;
		this.c = c;
	}
	
	public void setTypePanel(TypePanel tp) {
		this.tp = tp;
	}
	
	public int getNumberOfLinkedMovies() {
		final int i = numberOfLinkedMovies;
		numberOfLinkedMovies = -1;
		return i;
	}
	
	private void doRemove() {
		if ( tp == null ) tp = BasePanel.getInstance().getElementPanel();
		ALTable table = tp.getTable();
		TypeCollection tc = TypeCollection.getInstance();
		
		int selectedRow = -1;
		try {
			selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
		}
		catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(c, "선택된 항목이 없습니다.", "선택 에러", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String s = String.format("[ %s ]\n정말 삭제하시겠습니까?", (String) table.getModel().getValueAt(selectedRow, 1));
		int ans = JOptionPane.showConfirmDialog(c, s, "삭제", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if ( ans != JOptionPane.YES_OPTION ) return;
		
		switch ( tp.getType() ) {
		
		case TypePanel.WATCHING_TVA:
			String address = (String) table.getModel().getValueAt(selectedRow, 6);
			tc.getWatchingTVAMap().remove(address);
			ALDialog di = (ALDialog) c;
			di.setTitle(String.format("시청 중인 TVA (%d개)", tc.getWatchingTVAMap().size()));
			break;
			
		case TypePanel.SEARCHED_TVA:
		case TypePanel.TVA:
			address = (String) table.getModel().getValueAt(selectedRow, 6);
			String splitAddress[] = address.split("@");
			if ( tc.getTVAMap().get(splitAddress[0]).getElementMap().size() == 1 && tc.getTVAMap().get(splitAddress[0]).getMovieSeriesKey() != null )
				if ( JOptionPane.showConfirmDialog(c, "극장판도 같이 삭제됩니다.", "삭제", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION )
					return;
			numberOfLinkedMovies = tc.getMovieMap().get(tc.getTVAMap().get(splitAddress[0]).getMovieSeriesKey()).getElementMap().size();
			tc.removeTVAByAddress(address);
			
			if ( tp.getType() == TypePanel.SEARCHED_TVA ) {
				ALTable tvaPanelTable = TVAPanel.getInstance().getTable();
				for (int i=0; i<tvaPanelTable.getRowCount(); i++) {
					if ( ((String) tvaPanelTable.getDefaultTableModel().getValueAt(i, 6)).equals(address) ) {
						tvaPanelTable.getDefaultTableModel().removeRow(i);
						break;
					}
				}
			}
			break;
		
		case TypePanel.SEARCHED_MOVIE:
		case TypePanel.MOVIE:
			address = (String) table.getModel().getValueAt(selectedRow, 5);
			tc.removeMovieByAddress(address);
			
			if ( tp.getType() == TypePanel.SEARCHED_MOVIE ) {
				ALTable moviePanelTable = MoviePanel.getInstance().getTable();
				for (int i=0; i<moviePanelTable.getRowCount(); i++) {
					if ( ((String) moviePanelTable.getDefaultTableModel().getValueAt(i, 5)).equals(address) ) {
						moviePanelTable.getDefaultTableModel().removeRow(i);
						break;
					}
				}
			}
			break;
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
