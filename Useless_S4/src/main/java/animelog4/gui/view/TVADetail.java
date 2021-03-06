package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.ArrayList;
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
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.component.RequestFocusListener;
import animelog4.gui.event.DividedElementsAdd;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class TVADetail {
	private TypeCollection tc;
	private Component upperComponent;
	private int qtr, season, representValue;
	
	private ALDialog di;
	private JPanel south;
	private ALTable table;
	private JTextArea ta;
	private TypePanel tp;
	private ALTable[] otherSourceTables = null;
	
	public TVADetail() {
		tc = TypeCollection.getInstance();
		upperComponent = BasePanel.getInstance();
	}
	
	public TVADetail(TypePanel tp, Component upperComponent) {
		this.tp = tp;
		this.upperComponent = upperComponent;
		tc = TypeCollection.getInstance();
	}
	
	public MouseListener getElementMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 ) {
					ALTable table = (ALTable) e.getSource();
					String address = (String) table.getModel().getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6);
					
					if ( table.getSelectedColumn() == 0 ) showSeriesDialog(address.split("@")[0]);
					else {
						setElementDialog(address);
						
						if ( tp.getType() == TypePanel.WATCHING_TVA ) setElementDialogWatchingSouthPanel(address);
						else setElementDialogDefaultSouthPanel(address);
						
						setElementDialogShown();
					}
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
					setElementDialog(address);
					
					if ( upperComponent instanceof ALDialog ) setElementDialogWatchingSouthPanel(address);
					else setElementDialogDefaultSouthPanel(address);
					
					setElementDialogShown();
				}
			}
		};
	}
	
	public void setOtherSourceTables(ALTable... otherSourceTables) {
		this.otherSourceTables = otherSourceTables;
	}
	
	private void setElementDialog(String address) {
		di = new ALDialog("상세정보");
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
		table = new ALTable(dtm) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) {
				return col == 1 && row != 0 && row != 4;
			}
		};
		
		table.setValueAt("#" + left[representValue + 1], representValue + 1, 0);
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
						if ( !((String) table.getValueAt(row, 0)).contains("#") ) {
							table.setValueAt(left[representValue + 1], representValue + 1, 0);
							representValue = row - 1;
							table.setValueAt("#" + left[representValue + 1], representValue + 1, 0);
							setTitleChanged(di, "상세정보");
						}
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
								if ( ts == null ) {
									JOptionPane.showMessageDialog(upperComponent, "아직 등록되어 있지 않습니다.", "미등록", JOptionPane.WARNING_MESSAGE);
									return;
								}
								di.dispose();
								showSeriesDialog(ts.getKey());
							}
							else if ( rbtn[1].isSelected() ) {
								Vector<TVASeries> v = new Vector<TVASeries>(tc.getTVAMap().values());
								Collections.sort(v);
								final JComboBox<TVASeries> cbx = new JComboBox<TVASeries>(v);
								final JCheckBox chbox = new JCheckBox("새로 작성");
								final JTextField series = new JTextField();
								
								cbx.setEnabled(false);
								chbox.setSelected(true);
								series.addAncestorListener(new RequestFocusListener());
								if ( tp.getType() == TypePanel.WATCHING_TVA ) series.setEditable(false);
								
								cbx.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										series.setText(cbx.getSelectedItem().toString());
									}
								});
								chbox.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										cbx.setEnabled(!chbox.isSelected());
										if ( tp.getType() != TypePanel.WATCHING_TVA ) series.setEnabled(chbox.isSelected());
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
								} while ( series.getText().trim().isEmpty() && tp.getType() != TypePanel.WATCHING_TVA );
								
								if ( chbox.isSelected() ) {
									if ( series.getText().isEmpty() ) table.setValueAt(null, row, 1);
									else table.setValueAt(series.getText(), row, 1);
									setTitleChanged(di, "상세정보");
								}
								else {
									TVASeries ts = (TVASeries) cbx.getSelectedItem();
									try {
										if ( table.getValueAt(row, 1) instanceof String || !ts.equals(table.getValueAt(row, 1)) ) {
											setTitleChanged(di, "상세정보");
											table.setValueAt(ts, row, 1);
										}
									}
									catch(NullPointerException e1) {
										setTitleChanged(di, "상세정보");
										table.setValueAt(ts, row, 1);
									}
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
							if ( !((String) table.getValueAt(row, 1)).equals(t.getText()) ) {
								table.setValueAt(t.getText(), row, 1);
								setTitleChanged(di, "상세정보");
							}
						}
					}
				}
			}
		});
		table.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ( table.isEditing() ) setTitleChanged(di, "상세정보");
				
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
		
		ta = new JTextArea(4, 20);
		ta.setText(tva.getNote());
		ta.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				setTitleChanged(di, "상세정보");
			}
			public void removeUpdate(DocumentEvent e) {
				setTitleChanged(di, "상세정보");
			}
			public void changedUpdate(DocumentEvent e) {
				setTitleChanged(di, "상세정보");
			}
		});
		
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
		
		di.add(gridbag);
	}
	
	private void setElementDialogDefaultSouthPanel(String address) {
		final String left[] = { "시리즈", "KOR", "ENG", "JPN", "제작사", "쿨", "시즌" };
		final ALTable sourceTable = TVAPanel.getInstance().getTable();
		final TVA tva = tc.getTVAByAddress(address);
		
		final JButton save = new JButton("저장");
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
				
				if ( season != pastSeason ) {
					ts.getElementMap().remove(pastSeason);
					ts.add(tva);
				}
				if ( !tc.getTVAMap().get(pastSeriesKey).equals(ts) ) {
					ts.add(tva);
					if ( !tvaSeriesExists ) tc.getTVAMap().put(ts.getKey(), ts);
					tc.getTVAMap().get(pastSeriesKey).getElementMap().remove(pastSeason);
					
					String movieSeriesKey;
					if ( (movieSeriesKey = tc.getTVAMap().get(pastSeriesKey).getMovieSeriesKey()) != null ) {
						ts.setMovieSeriesKey(movieSeriesKey);
						tc.getMovieMap().get(movieSeriesKey).setTVASeriesKey(ts.getKey());
						ALTable movieTable = MoviePanel.getInstance().getTable();
						for (int i=0; i<movieTable.getRowCount(); i++)
							if ( ((String) movieTable.getModel().getValueAt(i, 5)).contains(movieSeriesKey) )
								movieTable.getModel().setValueAt(ts.getTitle(), i, 0);
					}
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
				
				sourceTable.setValueAt(ts.getTitleFrontChar(), row, 0);
				sourceTable.setValueAt(kor, row, 1);
				sourceTable.setValueAt(eng, row, 2);
				sourceTable.setValueAt(jpn, row, 3);
				sourceTable.setValueAt(pd, row, 4);
				sourceTable.setValueAt(qtr, row, 5);
				sourceTable.getModel().setValueAt(tva.getAddress(), sourceTable.convertRowIndexToModel(row), 6);
				
				if ( otherSourceTables != null ) {
					for (int i=0; i<otherSourceTables.length; i++) {
						for (int j=0; j<otherSourceTables[i].getRowCount(); j++) {
							if ( ((String) otherSourceTables[i].getModel().getValueAt(otherSourceTables[i].convertRowIndexToModel(j), 6)).equals(pastAddress) ) {
								otherSourceTables[i].setValueAt(ts.getTitleFrontChar(), j, 0);
								otherSourceTables[i].setValueAt(kor, j, 1);
								otherSourceTables[i].setValueAt(eng, j, 2);
								otherSourceTables[i].setValueAt(jpn, j, 3);
								otherSourceTables[i].setValueAt(pd, j, 4);
								otherSourceTables[i].setValueAt(qtr, j, 5);
								otherSourceTables[i].getModel().setValueAt(tva.getAddress(), otherSourceTables[i].convertRowIndexToModel(j), 6);
								break;
							}
						}
					}
				}
				
				di.setTitle("상세정보");
				di.setFixing(false);
			}
		});
		divide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( tva.getQTR() == 1 ) {
					JOptionPane.showMessageDialog(di, "분할은 2쿨 이상만 가능합니다.", "분할 에러", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JSpinner spnr = new JSpinner();
				spnr.setModel(new SpinnerNumberModel(2, 2, tva.getQTR(), 1));
				// don't know why, but the spinner's align cannot be center
				if ( JOptionPane.showConfirmDialog(di, new Object[] { "분할할 개수를 입력해주세요.", spnr }, "분할", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION )
					return;
				
				final int div = (Integer) spnr.getValue();
				JPanel grid = new JPanel(new GridLayout(1, div));
				final AddToTVA att[] = new AddToTVA[div];
				for (int i=0; i<div; i++) {
					att[i] = new AddToTVA();
					att[i].getRbtn()[tva.getRepresentValue()].setSelected(true);
					att[i].getTf()[0].setText(tva.getKOR());
					att[i].getTf()[1].setText(tva.getENG());
					att[i].getTf()[2].setText(tva.getJPN());
					att[i].getTf()[3].setText(tva.getPD());
					att[i].getSpnr().setModel(new SpinnerNumberModel(tva.getQTR(), 1, tva.getQTR(), 1));
					JSpinner.DefaultEditor spnrEditor = (JSpinner.DefaultEditor) att[i].getSpnr().getEditor();
					spnrEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
					att[i].getSeason().setValue((tva.getSeason() + i) + "기");
					att[i].getTa().setText(tva.getNote());
					att[i].setDialog();
					grid.add(att[i].getCenter());
				}
				
				final ALDialog divDialog = new ALDialog("분할");
				divDialog.add(grid);
				JButton save = new JButton("저장");
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if ( JOptionPane.showConfirmDialog(divDialog, "저장하시겠습니까?", "저장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION )
							return;
						DividedElementsAdd dea = new DividedElementsAdd();
						String message = dea.toTVACollection(tva, att);
						if ( message != null ) {
							JOptionPane.showMessageDialog(divDialog, message, "분할 에러", JOptionPane.ERROR_MESSAGE);
							return;
						}
						for (int i=0; i<sourceTable.getRowCount(); i++)
							if ( ((String) sourceTable.getModel().getValueAt(i, 6)).equals(tva.getAddress()) ) {
								sourceTable.getDefaultTableModel().removeRow(i);
								break;
							}
						
						TVA t[] = dea.getTVAArray();
						String frontChar = tc.getTVAMap().get(tva.getSeriesKey()).getTitleFrontChar();
						for (int i=0; i<t.length; i++) {
							String row[] = { frontChar, t[i].getKOR(), t[i].getENG(), t[i].getJPN(), t[i].getPD(), Integer.toString(t[i].getQTR()), t[i].getAddress() };
							TVAPanel.getInstance().getTable().getDefaultTableModel().addRow(row);
						}
						divDialog.dispose();
					}
				});
				divDialog.add(save, BorderLayout.SOUTH);
				
				divDialog.pack();
				divDialog.setLocationRelativeTo(di);
				di.dispose();
				divDialog.setModal(true);
				divDialog.setVisible(true);
			}
		});
		south = new JPanel(new GridLayout(1, 2));
		south.add(save);
		south.add(divide);
		di.setSaveButton(save);
	}
	
	private void setElementDialogWatchingSouthPanel(String address) {
		final String left[] = { "시리즈", "KOR", "ENG", "JPN", "제작사", "쿨", "시즌" };
		final ALTable sourceTable = tp.getTable();
		final TVA tva = tc.getTVAByAddress(address);
		
		final JButton save = new JButton("저장");
		JButton done = new JButton("시청 완료");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object series = table.getValueAt(0, 1);
				System.out.println(series);
				TVASeries ts;
				if ( series instanceof TVASeries )
					ts = (TVASeries) series;
				else if ( series == null )
					ts = null;
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
				final String pastAddress = tva.getAddress();
				
				if ( ts != null ) {
					if ( ts.getElementMap().get(season) != null ) {
						JOptionPane.showMessageDialog(di, String.format("%d기가 이미 존재합니다.", season), "시즌 에러", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				tva.setKOR(kor);
				tva.setENG(eng);
				tva.setJPN(jpn);
				tva.setPD(pd);
				tva.setQTR(qtr);
				tva.setSeason(season);
				tva.setNote(note);
				tva.setRepresentValue(representValue);
				
				try {
					if ( !tc.getTVAMap().get(pastSeriesKey).equals(ts) ) {
						tva.setSeriesKey(ts.getKey());
						tc.getWatchingTVAMap().remove(pastAddress);
						tc.getWatchingTVAMap().put(tva.getAddress(), tva);
					}
				}
				catch(NullPointerException e1) {
					if ( !tva.getSeriesKey().startsWith("x") ) {
						tva.setSeriesKey('x' + Long.toHexString(System.currentTimeMillis()));
						tc.getWatchingTVAMap().remove(pastAddress);
						tc.getWatchingTVAMap().put(tva.getAddress(), tva);
					}
					else if ( ts != null ) {
						tva.setSeriesKey(ts.getKey());
						tc.getWatchingTVAMap().remove(pastAddress);
						tc.getWatchingTVAMap().put(tva.getAddress(), tva);
					}
				}
				
				int row = -1;
				for (int i=0; i<sourceTable.getRowCount(); i++) {
					if ( pastAddress.equals(sourceTable.getModel().getValueAt(sourceTable.convertRowIndexToModel(i), 6)) ) {
						row = i;
						break;
					}
				}
				
				if ( ts != null )
					sourceTable.setValueAt(ts.getTitleFrontChar(), row, 0);
				else {
					switch ( representValue ) {
					case 0:
						sourceTable.setValueAt(kor.substring(0, 2), row, 0);
						break;
					case 1:
						sourceTable.setValueAt(eng.substring(0, 2), row, 0);
						break;
					case 2:
						sourceTable.setValueAt(jpn.substring(0, 2), row, 0);
						break;
					}
				}
				
				sourceTable.setValueAt(kor, row, 1);
				sourceTable.setValueAt(eng, row, 2);
				sourceTable.setValueAt(jpn, row, 3);
				sourceTable.setValueAt(pd, row, 4);
				sourceTable.setValueAt(qtr, row, 5);
				sourceTable.getModel().setValueAt(tva.getAddress(), sourceTable.convertRowIndexToModel(row), 6);
				
				di.setTitle("상세정보");
				di.setFixing(false);
			}
		});
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( JOptionPane.showConfirmDialog(di, "TVA 패널로 옮기시겠습니까?", "시청 완료", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION )
					return;
				
				final String pastAddress = tva.getAddress();
				if ( tva.getSeriesKey().startsWith("x") ) {
					String s = (String) JOptionPane.showInputDialog(null, "시리즈 타이틀을 입력해주세요", "시리즈 타이틀", JOptionPane.QUESTION_MESSAGE, null, null, tva.toString());
					if ( s == null || s.trim().isEmpty() ) return;
					TVASeries ts = new TVASeries(s.trim());
					ts.add(tva);
					tc.getTVAMap().put(ts.getKey(), ts);
				}
				else {
					if ( tc.getTVAMap().get(tva.getSeriesKey()).getElementMap().get(tva.getSeason()) != null ) {
						JOptionPane.showMessageDialog(di, String.format("%d기가 이미 존재합니다.", tva.getSeason()), "시즌 에러", JOptionPane.ERROR_MESSAGE);
						return;
					}
					tc.getTVAMap().get(tva.getSeriesKey()).add(tva);
				}
				
				tc.getWatchingTVAMap().remove(pastAddress);
				sourceTable.getDefaultTableModel().removeRow(sourceTable.convertRowIndexToModel(sourceTable.getSelectedRow()));
				
				String row[] = { tc.getTVAMap().get(tva.getSeriesKey()).getTitleFrontChar(), tva.getKOR(), tva.getENG(), tva.getJPN(), tva.getPD(), Integer.toString(tva.getQTR()), tva.getAddress() };
				TVAPanel.getInstance().getTable().getDefaultTableModel().addRow(row);
				
				di.dispose();
			}
		});
		south = new JPanel(new GridLayout(1, 2));
		south.add(save);
		south.add(done);
		di.setSaveButton(save);
	}
	
	private void setElementDialogShown() {
		di.add(south, BorderLayout.SOUTH);
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
	public void showSeriesDialog(String seriesKey) {
		final ALDialog di = new ALDialog("시리즈 정보");
		final ALTable sourceTable = TVAPanel.getInstance().getTable();
		final TVASeries ts = tc.getTVAMap().get(seriesKey);
		if ( ts == null ) {
			JOptionPane.showMessageDialog(upperComponent, "아직 등록되어 있지 않습니다.", "미등록", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		final JTextField tf = new JTextField(ts.getTitle());
		tf.setHorizontalAlignment(JTextField.CENTER);
		tf.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				setTitleChanged(di, "시리즈 정보");
			}
			public void removeUpdate(DocumentEvent e) {
				setTitleChanged(di, "시리즈 정보");
			}
			public void changedUpdate(DocumentEvent e) {
				setTitleChanged(di, "시리즈 정보");
			}
		});
		
		Vector<TVA> v = new Vector<TVA>(ts.getElementMap().values());
		final JList<TVA> list = new JList<TVA>(v);
		list.setFixedCellHeight(30);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 ) {
					di.dispose();
					String address = list.getSelectedValue().getAddress();
					setElementDialog(address);
					setElementDialogDefaultSouthPanel(address);
					setElementDialogShown();
				}
			}
		});
		
		JButton save = new JButton("저장");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( tf.getText().trim().isEmpty() ) {
					JOptionPane.showMessageDialog(di, "비어있습니다.", "공백", JOptionPane.ERROR_MESSAGE);
					return;
				}
				ts.setTitle(tf.getText().trim());
				di.setTitle("시리즈 정보");
				di.setFixing(false);
				String s = ts.getTitleFrontChar();
				for (int i=0; i<sourceTable.getRowCount(); i++) {
					String value = (String) sourceTable.getModel().getValueAt(sourceTable.convertRowIndexToModel(i), 6);
					if ( value.contains(ts.getKey()) ) {
						sourceTable.getModel().setValueAt(s, sourceTable.convertRowIndexToModel(i), 0);
						if ( (i + 1) == ts.getElementMap().size() ) return;
					}
				}
			}
		});
		JButton fixAll = new JButton("일괄 수정");
		fixAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int div = ts.getElementMap().size();
				JPanel grid = new JPanel(new GridLayout(1, div));
				final AddToTVA att[] = new AddToTVA[div];
				ArrayList<TVA> tvaList = new ArrayList<TVA>(ts.getElementMap().values());
				for (int i=0; i<div; i++) {
					att[i] = new AddToTVA();
					att[i].setFromElement(tvaList.get(i));
					grid.add(att[i].getCenter());
				}
				att[0].getTf()[0].addAncestorListener(new RequestFocusListener());
				
				final ALDialog divDialog = new ALDialog("일괄 수정");
				divDialog.add(grid);
				JButton save = new JButton("저장");
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e1) {
						if ( JOptionPane.showConfirmDialog(divDialog, "저장하시겠습니까?", "저장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION )
							return;
						DividedElementsAdd dea = new DividedElementsAdd();
						String message = dea.fixedTVAEntirely(ts, att);
						if ( message != null ) {
							JOptionPane.showMessageDialog(divDialog, message, "분할 에러", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						ArrayList<TVA> list = new ArrayList<TVA>(ts.getElementMap().values());
						for (int i=sourceTable.getRowCount()-1; i>=0; i--) {
							for (int j=0; j<list.size(); j++) {
								if ( ((String) sourceTable.getModel().getValueAt(i, 6)).equals(list.get(j).getAddress()) ) {
									sourceTable.getDefaultTableModel().removeRow(i);
									list.remove(j);
									break;
								}
							}
						}
						
						TVA t[] = dea.getTVAArray();
						ts.getElementMap().clear();
						String frontChar = ts.getTitleFrontChar();
						for (int i=0; i<t.length; i++) {
							ts.add(t[i]);
							String row[] = { frontChar, t[i].getKOR(), t[i].getENG(), t[i].getJPN(), t[i].getPD(), Integer.toString(t[i].getQTR()), t[i].getAddress() };
							sourceTable.getDefaultTableModel().addRow(row);
						}
						divDialog.dispose();
					}
				});
				
				divDialog.add(save, BorderLayout.SOUTH);
				divDialog.pack();
				divDialog.setLocationRelativeTo(di);
				di.dispose();
				divDialog.setModal(true);
				divDialog.setVisible(true);
			}
		});
		
		if ( ts.getMovieSeriesKey() != null ) {
			final JTextField movie = new JTextField("극장판 확인 (더블클릭)");
			movie.setEditable(false);
			movie.setBackground(Color.WHITE);
			movie.setHorizontalAlignment(JTextField.CENTER);
			movie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if ( e.getClickCount() == 2 ) {
						di.dispose();
						new MovieDetail().showSeriesDialog(ts.getMovieSeriesKey());
					}
				}
			});
			
			JPanel p = new JPanel(new GridLayout(2, 1));
			p.add(tf);
			p.add(movie);
			di.add(p, BorderLayout.NORTH);
		}
		else di.add(tf, BorderLayout.NORTH);
		
		JPanel south = new JPanel(new GridLayout(1, 2));
		south.add(save);
		south.add(fixAll);
		
		di.setSaveButton(save);
		di.add(list);
		di.add(south, BorderLayout.SOUTH);
		
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
	private void setTitleChanged(ALDialog di, String s) {
		di.setTitle("*" + s);
		di.setFixing(true);
	}
	
}
