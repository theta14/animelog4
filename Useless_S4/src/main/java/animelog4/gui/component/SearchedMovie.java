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
import animelog4.gui.view.MovieDetail;
import animelog4.gui.view.TypePanel;
import animelog4.type.Movie;

public class SearchedMovie extends JPanel implements TypePanel {
	private static final long serialVersionUID = 1L;
	
	private ALTable table;
	private int size;
	
	public SearchedMovie(String item, final Component component) {
		TypeCollection tc = TypeCollection.getInstance();
		item = item.replaceAll(" ", "").toUpperCase();
		setLayout(new BorderLayout());
		
		ArrayList<Movie> searchedList = new ArrayList<Movie>();
		for ( String key : tc.getMovieMap().keySet() ) {
			for ( int intKey : tc.getMovieMap().get(key).getElementMap().keySet() ) {
				Movie m = tc.getMovieMap().get(key).getElementMap().get(intKey);
				if ( m.contains(item) || tc.getTVAMap().get(tc.getMovieMap().get(m.getSeriesKey()).getTVASeriesKey()).getTitle().contains(item) )
					searchedList.add(m);
			}
		}
		size = searchedList.size();
		String data[][] = new String[size][];
		for (int i=0; i<size; i++) data[i] = searchedList.get(i).toArray();
		
		final String header[] = { "S", "KOR", "ENG", "JPN", "제작사", "Address" };
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(data, header);
		table = new ALTable(dtm);
		ElementRemoveEvent ere = new ElementRemoveEvent(this, component);
		table.addKeyListener(ere.getKeyListener());
		table.sortAs(UserInfo.getInstance().getSelectedTVAHeader());
		
		MovieDetail md = new MovieDetail(component);
		md.setOtherSourceTables(table);
		table.addMouseListener(md.getElementMouseListener());
		table.addKeyListener(md.getElementKeyListener());
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(5));
		
		JButton remove = new JButton("삭제");
		remove.addActionListener(ere.getActionListener());
		
		add(new JScrollPane(table));
		add(remove, BorderLayout.SOUTH);
	}

	public ALTable getTable() {
		return table;
	}

	public int getType() {
		return TypePanel.SEARCHED_MOVIE;
	}

	public Component getComponent() {
		return this;
	}
	
	public int numOfSearched() {
		return size;
	}

}
