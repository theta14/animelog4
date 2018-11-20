package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import animelog4.collection.PlannedCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALList;
import animelog4.type.Planned;
import lombok.Getter;

@Getter
public class PlannedToWatch {
	private ALList<Planned> list;
	private ALDialog di;
	
	public PlannedToWatch() {
		PlannedCollection pc = PlannedCollection.getInstance();
		di = new ALDialog(String.format("시청 예정 목록 (%d개)", pc.getPlannedMap().size()));
		list = new ALList<Planned>(pc.toArrayList());
		list.setFixedCellHeight(30);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JButton add = new JButton("추가");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddToPlanned atp = new AddToPlanned(PlannedToWatch.this);
				atp.setAdding();
				atp.show();
			}
		});
		
		JButton remove = new JButton("삭제");
		RemovePlannedElement rpe = new RemovePlannedElement();
		remove.addActionListener(rpe.getActionListener());
		list.addKeyListener(rpe.getKeyListener());
		list.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					if ( list.getSelectedIndex() == -1 ) {
						JOptionPane.showMessageDialog(di, "선택된 항목이 없습니다.", "항목 미선택", JOptionPane.ERROR_MESSAGE);
						return;
					}
					AddToPlanned atp = new AddToPlanned(PlannedToWatch.this);
					atp.setFixing();
					atp.show();
				}
			}
		});
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 ) {
					if ( list.getSelectedIndex() == -1 ) {
						JOptionPane.showMessageDialog(di, "선택된 항목이 없습니다.", "항목 미선택", JOptionPane.ERROR_MESSAGE);
						return;
					}
					AddToPlanned atp = new AddToPlanned(PlannedToWatch.this);
					atp.setFixing();
					atp.show();
				}
			}
		});
		
		JPanel south = new JPanel(new GridLayout(1, 2));
		south.add(add);
		south.add(remove);
		
		di.add(new JScrollPane(list));
		di.add(south, BorderLayout.SOUTH);
		di.pack();
		if ( di.getWidth() < 274 ) di.setWidth(274);
		di.setLocationRelativeTo(BasePanel.getInstance());
		di.setModal(true);
		di.setVisible(true);
	}
	
	private class RemovePlannedElement {
		private void doRemove() {
			final int i = list.getSelectedIndex();
			final Planned p = list.getSelectedValue();
			if ( i == -1 ) {
				JOptionPane.showMessageDialog(di, "선택된 항목이 없습니다.", "항목 미선택", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if ( JOptionPane.showConfirmDialog(di, String.format("[ %s ]\n정말 삭제하시겠습니까?", p.toString()), "삭제", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION )
				return;
			
			DefaultListModel<Planned> dlm = list.getDefaultListModel();
			dlm.remove(i);
			PlannedCollection.getInstance().remove(p);
			di.setTitle(String.format("시청 예정 목록 (%d개)", PlannedCollection.getInstance().getPlannedMap().size()));
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
					if ( e.getKeyCode() == KeyEvent.VK_DELETE )
						doRemove();
				}
			};
		}
	}
	
}
