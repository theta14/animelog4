package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import animelog4.collection.PlannedCollection;
import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.RequestFocusListener;
import animelog4.gui.event.ElementAddEvent;
import animelog4.type.Planned;

public class AddToPlanned {
	private ALDialog di;
	private JComboBox<String> elementTypeCbx;
	private JComboBox<String> titleTypeCbx;
	private JTextField titleField;
	private JTextArea memo;
	private JPanel center;
	
	private PlannedToWatch ptw;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	
	public AddToPlanned(PlannedToWatch ptw) {
		this.ptw = ptw;
		di = new ALDialog("추가");
		
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		center = new JPanel(gbl);
		
		final int fieldSize = 8;
		elementTypeCbx = new JComboBox<String>(new String[] { "TVA", "극장판" });
		titleTypeCbx = new JComboBox<String>(new String[] { "KOR", "ENG", "JPN" });
		titleField = new JTextField(fieldSize);
		titleField.addAncestorListener(new RequestFocusListener());
		memo = new JTextArea(4, fieldSize);
		
		add(elementTypeCbx, 0, 0, 4, 1);
		add(titleTypeCbx, 0, 1, 1, 1);
		add(titleField, 1, 1, 3, 1);
		add(new JScrollPane(memo), 0, 2, 4, 1);
		di.add(center);
	}
	
	private void add(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbl.setConstraints(c, gbc);
		center.add(c);
	}
	
	public void setAdding() {
		JButton save = new JButton("저장");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( titleField.getText().trim().isEmpty() ) {
					JOptionPane.showMessageDialog(di, "타이틀을 입력해야 합니다", "미입력", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if ( JOptionPane.showConfirmDialog(di, "저장하시겠습니까?", "저장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION )
					return;
				
				Planned p = new Planned(elementTypeCbx.getSelectedIndex(), titleTypeCbx.getSelectedIndex(), titleField.getText().trim(), memo.getText());
				PlannedCollection.getInstance().add(p);
				DefaultListModel<Planned> dlm = ptw.getList().getDefaultListModel();
				dlm.addElement(p);
				di.dispose();
				ptw.getDi().setTitle(String.format("시청 예정 목록 (%d개)", PlannedCollection.getInstance().getPlannedMap().size()));
			}
		});
		JButton cancel = new JButton("닫기");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				di.dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(save);
		di.add(panel, BorderLayout.SOUTH);
	}
	
	public void setFixing() {
		final Planned p = ptw.getList().getSelectedValue();
		elementTypeCbx.setSelectedIndex(p.getType());
		titleTypeCbx.setSelectedIndex(p.getTitleType());
		titleField.setText(p.getTitle());
		memo.setText(p.getMemo());
		
		JButton save = new JButton("저장");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( titleField.getText().trim().isEmpty() ) {
					JOptionPane.showMessageDialog(di, "타이틀을 입력해야 합니다", "미입력", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Planned selectedPlanned = ptw.getList().getSelectedValue();
				selectedPlanned.setType(elementTypeCbx.getSelectedIndex());
				selectedPlanned.setTitleType(titleTypeCbx.getSelectedIndex());
				selectedPlanned.setTitle(titleField.getText().trim());
				selectedPlanned.setMemo(memo.getText());
				
				DefaultListModel<Planned> dlm = ptw.getList().getDefaultListModel();
				dlm.set(ptw.getList().getSelectedIndex(), selectedPlanned);
				JOptionPane.showMessageDialog(di, "저장 완료", "저장", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(save);
		
		JButton toList = new JButton("시청");
		toList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TypeCollection tc = TypeCollection.getInstance();
				boolean b;
				
				if ( elementTypeCbx.getSelectedIndex() == Planned.TYPE_TVA ) {
					JRadioButton[] rbtn = { new JRadioButton("시청 중인 목록으로"), new JRadioButton("시청 완료 목록으로") };
					ButtonGroup bg = new ButtonGroup();
					bg.add(rbtn[0]); bg.add(rbtn[1]);
					rbtn[0].setSelected(true);
					
					if ( JOptionPane.showConfirmDialog(di, rbtn, "TVA", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION )
						return;
					
					di.dispose();
					if ( rbtn[0].isSelected() ) {	// watching tva
						final int size = tc.getWatchingTVAMap().size();
						
						AddToTVA att = new AddToTVA();
						att.setDialog();
						att.getSeries().removeAncestorListener(att.getSeries().getAncestorListeners()[0]);
						att.getSeries().setEditable(false);
						att.getTf()[0].addAncestorListener(new RequestFocusListener());
						
						att.getRbtn()[titleTypeCbx.getSelectedIndex()].setSelected(true);
						att.getTf()[titleTypeCbx.getSelectedIndex()].setText(titleField.getText());
						att.getTa().setText(memo.getText());
						
						ElementAddEvent eae = new ElementAddEvent(att.getDi());
						att.getSave().addActionListener(eae.getWatchingTVAActionListener(null, att.getCbx(), att.getChbox(), att.getSeries(), att.getRbtn(), att.getTf(), att.getSpnr(), att.getSeason(), att.getTa()));
						att.show(di);
						
						b = size == tc.getWatchingTVAMap().size();
					}
					else {	// tva panel
						final int size = tc.getTVAMap().size();
						
						AddToTVA att = new AddToTVA();
						att.setDialog();
						
						att.getRbtn()[titleTypeCbx.getSelectedIndex()].setSelected(true);
						att.getTf()[titleTypeCbx.getSelectedIndex()].setText(titleField.getText());
						att.getTa().setText(memo.getText());
						
						att.addEventToCollection();
						att.show();
						
						b = size == tc.getWatchingTVAMap().size();
					}
				}
				else if ( elementTypeCbx.getSelectedIndex() == Planned.TYPE_MOVIE ) {
					di.dispose();
					final int size = tc.getMovieMap().size();
					
					AddToMovie atm = new AddToMovie();
					atm.setDialog();
					
					atm.getTf()[titleTypeCbx.getSelectedIndex()].setText(titleField.getText());
					atm.getTa().setText(memo.getText());
					
					atm.addEventToCollection();
					atm.show();
					
					b = size == tc.getMovieMap().size();
				}
				else {
					System.out.println("????????? How can you get in here???");
					return;
				}
				
				if ( !b ) {
					DefaultListModel<Planned> dlm = ptw.getList().getDefaultListModel();
					dlm.removeElementAt(ptw.getList().getSelectedIndex());
					PlannedCollection.getInstance().remove(p);
				}
			}
		});
		panel.add(toList);
		di.add(panel, BorderLayout.SOUTH);
	}
	
	public void show() {
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.5));
		di.setLocationRelativeTo(ptw.getDi());
		di.setModal(true);
		di.setVisible(true);
	}
	
}
