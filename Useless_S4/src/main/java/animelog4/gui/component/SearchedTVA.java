package animelog4.gui.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.gui.event.ElementRemoveEvent;
import animelog4.gui.view.TVADetail;
import animelog4.gui.view.TypePanel;
import animelog4.type.TVA;

public class SearchedTVA extends JPanel implements TypePanel {
	private static final long serialVersionUID = 1L;
	
	private ALTable table;
	private int size;
	private JButton remove;
	private ElementRemoveEvent ere;
	
	public SearchedTVA(String item, final ALDialog di) {
		TypeCollection tc = TypeCollection.getInstance();
		item = item.replaceAll(" ", "").toUpperCase();
		ere = new ElementRemoveEvent(this, di);
		setLayout(new BorderLayout());
		
		ArrayList<TVA> searchedList = new ArrayList<TVA>();
		for ( String key : tc.getTVAMap().keySet() ) {
			for ( int intKey : tc.getTVAMap().get(key).getElementMap().keySet() ) {
				TVA t = tc.getTVAMap().get(key).getElementMap().get(intKey);
				if ( t.contains(item) || tc.getTVAMap().get(key).getTitle().contains(item) )
					searchedList.add(t);
			}
		}
		size = searchedList.size();
		String data[][] = new String[size][];
		for (int i=0; i<size; i++) data[i] = searchedList.get(i).toArray();
		
		final String header[] = { "S", "KOR", "ENG", "JPN", "제작사", "쿨", "Address" };
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(data, header);
		table = new ALTable(dtm);
		table.sortAs(UserInfo.getInstance().getSelectedTVAHeader());
		
		TVADetail td = new TVADetail(this, di);
		td.setOtherSourceTables(table);
		table.addMouseListener(td.getElementMouseListener());
		table.addKeyListener(td.getElementKeyListener());
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(6));
		
		remove = new JButton("삭제");
		
		add(new JScrollPane(table));
		add(remove, BorderLayout.SOUTH);
	}
	
	public ALTable getTable() {
		return table;
	}

	public int getType() {
		return SEARCHED_TVA;
	}

	public Component getComponent() {
		return this;
	}
	
	public int numOfSearched() {
		return size;
	}
	
	public void setEventWhenTVAOnlySearched() {
		remove.addActionListener(ere.getActionListener());
		table.addKeyListener(ere.getKeyListener());
	}
	
	public void setSearchedMovie(final SearchedMovie sm) {
		RemovingAll ra = new RemovingAll(sm);
		remove.addActionListener(ra.getActionListener());
		table.addKeyListener(ra.getKeyListener());
		remove.addActionListener(ere.getActionListener());
		table.addKeyListener(ere.getKeyListener());
	}
	
	private class RemovingAll {
		private SearchedMovie sm;
		
		private RemovingAll(SearchedMovie sm) {
			this.sm = sm;
		}
		
		private void doRemove() {
			int linkedMovies = -1;
			if ( (linkedMovies = ere.getNumberOfLinkedMovies()) != -1 ) {
				ALTable t = sm.getTable();
				TypeCollection tp = TypeCollection.getInstance();
				for (int i=0,j=0; i<t.getRowCount(); i++) {
					String movieAddress = (String) t.getModel().getValueAt(i, 5);
					if ( tp.getMovieMap().get(movieAddress.split("@")[0]) == null ) {
						t.getDefaultTableModel().removeRow(i--);
						j++;
					}
					if ( j == linkedMovies ) break;
				}
			}
		}
		
		private ActionListener getActionListener() {
			return new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doRemove();
				}
			};
		}
		
		private KeyListener getKeyListener() {
			return new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					doRemove();
				}
			};
		}
	}
	
}
