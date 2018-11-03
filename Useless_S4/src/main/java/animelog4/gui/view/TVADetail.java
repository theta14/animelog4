package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.component.RequestFocusListener;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class TVADetail {
	TypeCollection tc;
	private Component upperComponent;
	private int qtr, season, representValue;
	
	public TVADetail() {
		tc = TypeCollection.getInstance();
		upperComponent = BasePanel.getInstance();
	}
	
	public TVADetail(Component upperComponent) {
		tc = TypeCollection.getInstance();
		this.upperComponent = upperComponent;
	}
	
	public MouseListener getElementMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 ) {
					ALTable table = (ALTable) e.getSource();
					String address = (String) table.getModel().getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6);
					if ( table.getSelectedColumn() == 0 ) showSeriesDialog(address.split("@")[0]);
					else showElementDialog(address);
				}
			}
		};
	}
	
	public KeyListener getElementKeyListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					ALTable table = (ALTable) e.getSource();
					String address = (String) table.getModel().getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6);
					showElementDialog(address);
				}
			}
		};
	}
	
	private void showElementDialog(String address) {
		final ALDialog di = new ALDialog("상세정보");
		final ALTable sourceTable = BasePanel.getInstance().getElementPanel().getTable();
		final TVA tva = tc.getTVAByAddress(address);
		qtr = tva.getQTR();
		season = tva.getSeason();
		representValue = tva.getRepresentValue();
		
		final Object left[] = { "시리즈", "KOR", "ENG", "JPN", "제작사", "쿨", "시즌" };
		final Object right[] = { tc.getTVAMap().get(tva.getSeriesKey()), tva.getKOR(), tva.getENG(), tva.getJPN(), tva.getPD(), tva.getQTR(), tva.getSeason() + "기" };
		Object tableData[][] = new Object[left.length][2];
		for (int i=0; i<left.length; i++) {
			tableData[i][0] = left[i];
			tableData[i][1] = right[i];
		}
		
		DefaultTableModel dtm = new DefaultTableModel(tableData, new String[] { "property", "element" });
		final ALTable table = new ALTable(dtm) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) {
				return col == 1 && row != 0 && row != 4;
			}
		};
		
		table.setCellSelectionEnabled(true);
		table.setShowHorizontalLines(true);
		table.getTableHeader().setVisible(false);
		table.addMouseListener(new MouseAdapter() {
			private int selectedCheckBox;
			public void mouseClicked(MouseEvent e) {
				int row;
				if ( e.getClickCount() == 2 ) {
					if ( table.getSelectedColumn() == 0 ) {
						row = table.getSelectedRow();
						if ( row != 1 && row != 2 && row != 3 ) return;
						if ( JOptionPane.showConfirmDialog(di, String.format("대푯값을 %s 으로 바꾸시겠습니까?", left[row]), "대푯값 수정", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION )
							return;
						representValue = row - 1;						
					}
					else if ( table.getSelectedColumn() == 1 ) {
						if ( (row = table.getSelectedRow()) == 0 ) {
							JRadioButton rbtn[] = { new JRadioButton("시리즈 정보"), new JRadioButton("시리즈 수정") };
							rbtn[selectedCheckBox].setSelected(true);
							for (int i=0; i<rbtn.length; i++) {
								final int $i = i;
								rbtn[i].addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										selectedCheckBox = $i;
									}
								});
							}
							
							ButtonGroup bg = new ButtonGroup();
							bg.add(rbtn[0]);
							bg.add(rbtn[1]);
							
							if ( JOptionPane.showConfirmDialog(di, rbtn, "시리즈", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION )
								return;
							
							if ( rbtn[0].isSelected() ) {
								TVASeries ts = (TVASeries) table.getValueAt(row, 1);
								di.dispose();
								showSeriesDialog(ts.getKey());
							}
							else if ( rbtn[1].isSelected() ) {
								Vector<TVASeries> v = new Vector<TVASeries>(tc.getTVAMap().values());
								Collections.sort(v);
								final JComboBox<TVASeries> cbx = new JComboBox<TVASeries>(v);
								cbx.setEnabled(false);
								
								final JCheckBox chbox = new JCheckBox("새로 작성");
								chbox.setSelected(true);
								
								final JTextField series = new JTextField();
								series.addAncestorListener(new RequestFocusListener());
								
								cbx.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										series.setText(cbx.getSelectedItem().toString());
									}
								});
								chbox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										cbx.setEnabled(!chbox.isSelected());
										series.setEnabled(chbox.isSelected());
										if ( chbox.isSelected() ) series.setText("");
										else series.setText(cbx.getSelectedItem().toString());
									}
								});
								
								JPanel p1 = new JPanel(new GridLayout(2, 1));
								JPanel p2 = new JPanel(new BorderLayout());
								p1.add(cbx);
								p1.add(p2);
								p2.add(chbox, BorderLayout.WEST);
								p2.add(series);
								
								do {
									if ( JOptionPane.showConfirmDialog(di, p1, "시리즈 수정", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION )
										return;
								} while ( series.getText().trim().isEmpty() );
								
								if ( chbox.isSelected() ) table.setValueAt(series.getText(), row, 1);
								else {
									TVASeries ts = (TVASeries) cbx.getSelectedItem();
									table.setValueAt(ts, row, 1);
								}
							}
						}
						else if ( (row = table.getSelectedRow()) == 4 ) {
							final JComboBox<String> cbx = new JComboBox<String>(tc.getPDArray());
							final JTextField t = new JTextField((String) table.getValueAt(row, 1));
							t.addAncestorListener(new RequestFocusListener());
							cbx.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									t.setText((String) cbx.getSelectedItem());
								}
							});
							JPanel p = new JPanel(new GridLayout(2, 1));
							p.add(cbx);
							p.add(t);
							JOptionPane.showMessageDialog(di, p, "제작사", JOptionPane.PLAIN_MESSAGE);
							table.setValueAt(t.getText(), row, 1);
						}
					}
				}
			}
		});
		table.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				int row;
				if ( table.getSelectedColumn() == 1 ) {
					if ( (row = table.getSelectedRow()) == 5 ) {
						if ( !table.isEditing() ) {
							try {
								int i = Integer.parseInt((String) table.getValueAt(row, 1));
								if ( i < 1 ) i = qtr;
								else qtr = i;
								table.setValueAt(qtr, row, 1);
							}
							catch(NumberFormatException e) {
								table.setValueAt(qtr, row, 1);
							}
							catch(ClassCastException e) {
								table.setValueAt(qtr, row, 1);
							}
						}
					}
					else if ( (row = table.getSelectedRow()) == 6 ) {
						if ( table.isEditing() ) {
							table.getCellEditor().cancelCellEditing();
							table.setValueAt(season, row, 1);
							table.editCellAt(row, 1);
						}
						else {
							try {
								int i = Integer.parseInt((String) table.getValueAt(row, 1));
								if ( i < 1 ) i = season;
								else season = i;
								table.setValueAt(season + "기", row, 1);
							}
							catch(NumberFormatException e) {
								table.setValueAt(season + "기", row, 1);
							}
							catch(ClassCastException e) {
								table.setValueAt(season + "기", row, 1);
							}
						}
					}
				}
			}
		});
		
		final JTextArea ta = new JTextArea(4, 20);
		ta.setText(tva.getNote());
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel gridbag = new JPanel();
		gridbag.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 3;
		gbl.setConstraints(table, gbc);
		gridbag.add(table);
		
		JScrollPane sp = new JScrollPane(ta);
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbl.setConstraints(sp, gbc);
		gridbag.add(sp);
		
		JButton save = new JButton("저장");
		JButton divide = new JButton("분할");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object series = table.getValueAt(0, 1);
				TVASeries ts;
				boolean tvaSeriesExists;
				if ( series instanceof TVASeries ) {
					ts = (TVASeries) series;
					tvaSeriesExists = true;
				}
				else if ( series instanceof String ) {
					ts = new TVASeries((String) series);
					tvaSeriesExists = false;
				}
				else {
					System.out.println("There is something wrong about TVASeries cell");
					return;
				}
				
				String kor = ((String) table.getValueAt(1, 1)).trim();
				String eng = ((String) table.getValueAt(2, 1)).trim();
				String jpn = ((String) table.getValueAt(3, 1)).trim();
				String pd = ((String) table.getValueAt(4, 1)).trim();
				int qtr = (Integer) table.getValueAt(5, 1);
				String seasonStr = (String) table.getValueAt(6, 1);
				seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
				int season = Integer.parseInt(seasonStr);
				String note = ta.getText().trim();
				
				String s[] = { kor, eng, jpn, pd };
				for (int i=0; i<s.length; i++) {
					if ( s[i].isEmpty() ) {
						JOptionPane.showMessageDialog(di, String.format("%s 필드가 비어있습니다.", left[i+1]), "필드", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				final String pastSeriesKey = tva.getSeriesKey();
				final int pastSeason = tva.getSeason();
				final String pastAddress = tva.getAddress();
				
				if ( !tc.getTVAMap().get(pastSeriesKey).equals(ts) && ts.getElementMap().get(season) != null ) {
					JOptionPane.showMessageDialog(di, String.format("%d기가 이미 존재합니다.", season), "시즌 에러", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				tva.setKOR(kor);
				tva.setENG(eng);
				tva.setJPN(jpn);
				tva.setPD(pd);
				tva.setQTR(qtr);
				tva.setSeason(season);
				tva.setNote(note);
				tva.setRepresentValue(representValue);
				
				if ( !tc.getTVAMap().get(pastSeriesKey).equals(ts) ) {
					ts.add(tva);
					if ( !tvaSeriesExists ) tc.getTVAMap().put(ts.getKey(), ts);
					tc.getTVAMap().get(pastSeriesKey).getElementMap().remove(pastSeason);
					if ( tc.getTVAMap().get(pastSeriesKey).getElementMap().isEmpty() )
						tc.getTVAMap().remove(pastSeriesKey);
				}
				
				int row = -1;
				for (int i=0; i<sourceTable.getRowCount(); i++) {
					if ( pastAddress.equals(sourceTable.getModel().getValueAt(sourceTable.convertRowIndexToModel(i), 6)) ) {
						row = i;
						break;
					}
				}
				
				sourceTable.setValueAt(ts.getTitleFrontChar(2), row, 0);
				sourceTable.setValueAt(kor, row, 1);
				sourceTable.setValueAt(eng, row, 2);
				sourceTable.setValueAt(jpn, row, 3);
				sourceTable.setValueAt(pd, row, 4);
				sourceTable.setValueAt(qtr, row, 5);
				sourceTable.getModel().setValueAt(tva.getAddress(), sourceTable.convertRowIndexToModel(row), 6);
			}
		});
		
		JPanel south = new JPanel(new GridLayout(1, 2));
		south.add(save);
		south.add(divide);
		
		di.add(gridbag);
		di.add(south, BorderLayout.SOUTH);
		
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
	public void showSeriesDialog(String seriesKey) {	// it's not done, has to be fixed - save, list action
		final ALDialog di = new ALDialog("시리즈 정보");
		TVASeries ts = tc.getTVAMap().get(seriesKey);
		
		JTextField tf = new JTextField(ts.getTitle());
		tf.setHorizontalAlignment(JTextField.CENTER);
		
		Vector<TVA> v = new Vector<TVA>(ts.getElementMap().values());
		JList<TVA> list = new JList<TVA>(v);
		list.setFixedCellHeight(30);
		
		JButton save = new JButton("저장");
		
		di.add(tf, BorderLayout.NORTH);
		di.add(list);
		di.add(save, BorderLayout.SOUTH);
		
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
}
