package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.component.RequestFocusListener;
import animelog4.gui.event.ElementAddEvent;
import animelog4.gui.event.ElementRemoveEvent;

public class WatchingTVA implements TypePanel {
	private ALDialog di;
	private ALTable table;
	
	public WatchingTVA() {
		final TypeCollection tc = TypeCollection.getInstance();
		di = new ALDialog(String.format("시청 중인 TVA (%d개)", tc.getWatchingTVAMap().size()));
		final String header[] = { "SER", "KOR", "ENG", "JPN", "제작사", "쿨", "Address" };
		
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(TypeCollection.getInstance().toWatchingTVAArray(), header);
		table = new ALTable(dtm);
		table.addKeyListener(new ElementRemoveEvent().getKeyListener());
		table.sortAs(UserInfo.getInstance().getSelectedTVAHeader());
		
		TVADetail td = new TVADetail(this, di);
		table.addMouseListener(td.getElementMouseListener());
		table.addKeyListener(td.getElementKeyListener());
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(6));
		
		JButton add = new JButton("추가");
		JButton remove = new JButton("삭제");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int size = tc.getWatchingTVAMap().size();
				
				AddToTVA att = new AddToTVA();
				att.setDialog();
				att.getSeries().removeAncestorListener(att.getSeries().getAncestorListeners()[0]);
				att.getSeries().setEditable(false);
				att.getTf()[0].addAncestorListener(new RequestFocusListener());
				
				ElementAddEvent eae = new ElementAddEvent(att.getDi());
				att.getSave().addActionListener(eae.getWatchingTVAActionListener(table, att.getCbx(), att.getChbox(), att.getSeries(), att.getRbtn(), att.getTf(), att.getSpnr(), att.getSeason(), att.getTa()));
				att.show(di);
				
				if ( size < tc.getWatchingTVAMap().size() ) di.setTitle(String.format("시청 중인 TVA (%d개)", tc.getWatchingTVAMap().size()));
			}
		});
		ElementRemoveEvent ere = new ElementRemoveEvent(this, di);
		remove.addActionListener(ere.getActionListener());
		table.addKeyListener(ere.getKeyListener());
		
		JPanel south = new JPanel(new GridLayout(1, 2));
		south.add(add);
		south.add(remove);
		
		di.add(new JScrollPane(table));
		di.add(south, BorderLayout.SOUTH);
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.8)); 
		di.setLocationRelativeTo(BasePanel.getInstance());
		di.setModal(true);
		di.setVisible(true);
	}
	
	public ALTable getTable() {
		return table;
	}
	
	public int getType() {
		return WATCHING_TVA;
	}
	
	public Component getComponent() {
		return di;
	}
}
