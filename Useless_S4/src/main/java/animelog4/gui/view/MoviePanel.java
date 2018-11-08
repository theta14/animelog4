package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.gui.component.ALTable;
import animelog4.gui.event.ElementRemoveEvent;

public class MoviePanel extends JPanel implements TypePanel {
	private static final long serialVersionUID = 1L;
	private static final MoviePanel instance = new MoviePanel();
	
	private ALTable table;
	
	private MoviePanel() {
		setLayout(new BorderLayout());
		
		final String header[] = { "원작", "KOR", "ENG", "JPN", "제작사", "Address" };
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(TypeCollection.getInstance().toMovieArray(), header);
		table = new ALTable(dtm);
		table.addKeyListener(new ElementRemoveEvent().getKeyListener());
		
		final UserInfo ui = UserInfo.getInstance();
		table.sortAs(ui.getSelectedTVAHeader());
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ui.setSelectedTVAHeader(table.columnAtPoint(e.getPoint()));
			}
		});
		
		MovieDetail md = new MovieDetail();
		table.addMouseListener(md.getElementMouseListener());
		table.addKeyListener(md.getElementKeyListener());
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(5));
		
		add(new JScrollPane(table));
	}
	
	public static MoviePanel getInstance() {
		return instance;
	}
	
	public ALTable getTable() {
		return table;
	}
	
	public int getType() {
		return MOVIE;
	}
}
