package animelog4.gui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.event.ElementRemoveEvent;

public class WatchingTVA implements TypePanel {
	private ALDialog di;
	private ALTable table;
	
	public WatchingTVA() {
		TypeCollection tc = TypeCollection.getInstance();
		di = new ALDialog(String.format("시청 중인 TVA (%d개)", tc.getWatchingTVAMap().size()));
		final String header[] = { "시리즈", "KOR", "ENG", "JPN", "제작사", "쿨", "Address" };
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(TypeCollection.getInstance().toTVAArray(), header);
		table = new ALTable(dtm);
		table.addKeyListener(new ElementRemoveEvent().getKeyListener());
		
		final UserInfo ui = UserInfo.getInstance();
		table.sortAs(ui.getSelectedTVAHeader());
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ui.setSelectedTVAHeader(table.columnAtPoint(e.getPoint()));
			}
		});
		
		TVADetail td = new TVADetail(di);
		table.addMouseListener(td.getElementMouseListener());
		table.addKeyListener(td.getElementKeyListener());
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(6));
		
		JButton add = new JButton("추가");
		JButton remove = new JButton("삭제");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// logic to add
			}
		});
		ElementRemoveEvent ere = new ElementRemoveEvent(this, di);
		remove.addActionListener(ere.getActionListener());
		table.addKeyListener(ere.getKeyListener());
	}
	
	public ALTable getTable() {
		return table;
	}
	
	public int getType() {
		return WATCHING_TVA;
	}
}
