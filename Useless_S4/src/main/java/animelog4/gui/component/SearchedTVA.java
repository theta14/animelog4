package animelog4.gui.component;

import java.awt.BorderLayout;
import java.awt.Component;
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
	
	public SearchedTVA(String item) {
		TypeCollection tc = TypeCollection.getInstance();
		item = item.replaceAll(" ", "").toUpperCase();
		setLayout(new BorderLayout());
		
		ArrayList<TVA> searchedList = new ArrayList<TVA>();
		for ( String key : tc.getTVAMap().keySet() ) {
			for ( int intKey : tc.getTVAMap().get(key).getElementMap().keySet() ) {
				TVA t = tc.getTVAMap().get(key).getElementMap().get(intKey);
				if ( t.contains(item) || tc.getTVAMap().get(key).getTitle().contains(item) )
					searchedList.add(t);
			}
		}
		String data[][] = new String[searchedList.size()][];
		for (int i=0; i<searchedList.size(); i++) data[i] = searchedList.get(i).toArray();
		size = searchedList.size();
		
		final String header[] = { "SER", "KOR", "ENG", "JPN", "제작사", "쿨", "Address" };
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(data, header);
		table = new ALTable(dtm);
		ElementRemoveEvent ere = new ElementRemoveEvent(this, this);
		table.addKeyListener(ere.getKeyListener());
		table.sortAs(UserInfo.getInstance().getSelectedTVAHeader());
		
		TVADetail td = new TVADetail(this, this);
		table.addMouseListener(td.getElementMouseListener());
		table.addKeyListener(td.getElementKeyListener());
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(6));
		
		JButton remove = new JButton("삭제");
		remove.addActionListener(ere.getActionListener());
		
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
	
}
