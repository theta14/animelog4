package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.component.RequestFocusListener;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.TVASeries;

public class MovieDetail {
	TypeCollection tc;
	private Component upperComponent;
	private int order;
	
	public MovieDetail() {
		tc = TypeCollection.getInstance();
		upperComponent = BasePanel.getInstance();
	}
	
	public MovieDetail(Component upperComponent) {
		tc = TypeCollection.getInstance();
		this.upperComponent = upperComponent;
	}
	
	public MouseListener getElementMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 ) {
					ALTable table = (ALTable) e.getSource();
					String address = (String) table.getModel().getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 5);
					if ( table.getSelectedColumn() == 0 ) {
						String tvaSeriesKey = tc.getMovieMap().get(address.split("@")[0]).getTVASeriesKey();
						new TVADetail().showSeriesDialog(tvaSeriesKey);
					}
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
		final JPanel basePanel = new JPanel(new GridLayout(1, 2));
		final JPanel leftPanel = new JPanel(new BorderLayout());
		final JPanel rightPanel = new JPanel(new BorderLayout());
		final ALTable sourceTable = BasePanel.getInstance().getElementPanel().getTable();
		final Movie movie = tc.getMovieByAddress(address);
		final MovieSeries movieSeries = tc.getMovieMap().get(movie.getSeriesKey());
		order = movie.getOrder();
		
		final Object left[] = { "원작", "시리즈", "KOR", "ENG", "JPN", "제작사", "순서" };
		final Object right[] = { tc.getTVAMap().get(movieSeries.getTVASeriesKey()), movieSeries, movie.getKOR(), movie.getENG(), movie.getJPN(), movie.getPD(), movie.getOrder() };
		Object tableData[][] = new Object[left.length][2];
		for (int i=0; i<left.length; i++) {
			tableData[i][0] = left[i];
			tableData[i][1] = right[i];
		}
		
		DefaultTableModel dtm = new DefaultTableModel(tableData, new String[] { "property", "element" });
		final ALTable table = new ALTable(dtm) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) {
				return col == 1 && row != 0 && row != 1 && row != 5;
			}
		};
		
		table.setCellSelectionEnabled(true);
		table.setShowHorizontalLines(true);
		table.getTableHeader().setVisible(false);
		table.addMouseListener(new MouseAdapter() {
			private int selectedCheckBox;
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 && table.getSelectedColumn() == 1 ) {
					if ( table.getSelectedRow() == 0 ) {
						final int row = 0;
						JRadioButton rbtn[] = { new JRadioButton("원작 정보"), new JRadioButton("원작 수정") };
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
						
						if ( JOptionPane.showConfirmDialog(di, rbtn, "원작", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION )
							return;
						
						if ( rbtn[0].isSelected() ) {
							TVASeries ts = (TVASeries) table.getValueAt(row, 1);
							TVADetail td = new TVADetail();
							di.dispose();
							td.showSeriesDialog(ts.getKey());
						}
						else if ( rbtn[1].isSelected() ) {
							Vector<TVASeries> v = new Vector<TVASeries>(tc.getTVAMap().values());
							Collections.sort(v);
							final JComboBox<TVASeries> cbx = new JComboBox<TVASeries>(v);
							
							if ( JOptionPane.showConfirmDialog(di, cbx, "원작 수정", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION )
								return;
							
							TVASeries ts = (TVASeries) cbx.getSelectedItem();
							if ( !ts.equals(table.getValueAt(row, 1)) ) {
								setTitleChanged(di, "상세정보");
								table.setValueAt(ts, row, 1);
							}
						}
					}
					else if ( table.getSelectedRow() == 1 ) {
						final int row = 1;
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
							MovieSeries ms = (MovieSeries) table.getValueAt(row, 1);
							di.dispose();
							showSeriesDialog(ms.getKey());
						}
						else if ( rbtn[1].isSelected() ) {
							Vector<MovieSeries> v = new Vector<MovieSeries>(tc.getMovieMap().values());
							Collections.sort(v);
							final JComboBox<MovieSeries> cbx = new JComboBox<MovieSeries>(v);
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
							
							if ( chbox.isSelected() ) {
								table.setValueAt(series.getText(), row, 1);
								setTitleChanged(di, "상세정보");
							}
							else {
								MovieSeries ms = (MovieSeries) cbx.getSelectedItem();
								if ( table.getValueAt(row, 1) instanceof String || !ms.equals(table.getValueAt(row, 1)) ) {
									setTitleChanged(di, "상세정보");
									table.setValueAt(ms, row, 1);
								}
							}
						}
					}
					else if ( table.getSelectedRow() == 5 ) {
						final JComboBox<String> cbx = new JComboBox<String>(tc.getPDArray());
						final JTextField t = new JTextField((String) table.getValueAt(5, 1));
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
						table.setValueAt(t.getText(), 5, 1);
					}
				}
			}
		});
		table.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ( table.isEditing() ) setTitleChanged(di, "상세정보");
				
				int row;
				if ( table.getSelectedColumn() == 1 && (row = table.getSelectedRow()) == 6 ) {
					if ( !table.isEditing() ) {
						try {
							int i = Integer.parseInt((String) table.getValueAt(row, 1));
							if ( i < 1 ) i = order;
							else order = i;
							table.setValueAt(order, row, 1);
						}
						catch(NumberFormatException e) {
							table.setValueAt(order, row, 1);
						}
						catch(ClassCastException e) {
							table.setValueAt(order, row, 1);
						}
					}
				}
			}
		});
		
		final JTextArea ta = new JTextArea(4, 20);
		ta.setText(movie.getNote());
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
		
		final JButton save = new JButton("저장");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				di.setTitle("상세정보");
			}
		});
		
		di.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				UserInfo ui = UserInfo.getInstance();
				if ( !ui.getSavePopUp() ) {
					if ( di.getTitle().contains("*") ) {
						JCheckBox chbx = new JCheckBox("다시 표시하지 않기");
						int ans = JOptionPane.showConfirmDialog(di, new Object[] { "변경사항이 있습니다.\n저장하시겠습니까?" , chbx }, "변경사항", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if ( chbx.isSelected() ) UserInfo.getInstance().setSavePopUp(true);
						if ( ans == JOptionPane.YES_OPTION ) save.doClick();
					}
				}
			}
		});
		
		leftPanel.add(gridbag);
		JDialog temp = new JDialog();
		temp.setLayout(new BorderLayout());
		temp.add(leftPanel);
		temp.pack();
		int width = temp.getWidth();
		int height = temp.getHeight();
		
		final String imagePath = "data/image/" + movie.getImageKey();
		ImageIcon icon = getProperIcon(imagePath, rightPanel);
		JLabel la = new JLabel(icon, JLabel.CENTER);
		
		rightPanel.add(la);
		rightPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ImageIcon icon = getProperIcon(imagePath, rightPanel);
				JLabel la = new JLabel(icon, JLabel.CENTER);
				rightPanel.removeAll();
				rightPanel.add(la);
				rightPanel.revalidate();
				rightPanel.repaint();
			}
		});
		
		basePanel.add(leftPanel);
		basePanel.add(rightPanel);
		
		JPanel south = new JPanel(new GridLayout(1, 2));
		south.add(save);
		south.add(new JButton("분할"));
		
		di.add(basePanel);
		di.add(south, BorderLayout.SOUTH);
		di.setSize((int) (width * 2.4), (int) (height * 1.1));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
	public void showSeriesDialog(String seriesKey) {
		final ALDialog di = new ALDialog("시리즈 정보");
		final MovieSeries ms = tc.getMovieMap().get(seriesKey);
		
		final JTextField tf = new JTextField(ms.toString());
		tf.setHorizontalAlignment(JTextField.CENTER);
		tf.setEditable(false);
		tf.setBackground(Color.WHITE);
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
		
		Vector<Movie> v = new Vector<Movie>(ms.getElementMap().values());
		final JList<Movie> list = new JList<Movie>(v);
		list.setFixedCellHeight(30);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 ) {
					di.dispose();
					showElementDialog(list.getSelectedValue().getAddress());
				}
			}
		});
		
		di.add(tf, BorderLayout.NORTH);
		di.add(list);
		
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
	private void setTitleChanged(ALDialog di, String s) {
		di.setTitle("*" + s);
	}
	
	private ImageIcon getProperIcon(String path, JPanel p) {
		ImageIcon icon = new ImageIcon(path);
		double iw = icon.getIconWidth();
		double ih = icon.getIconHeight();
		double pw = p.getWidth();
		double ph = p.getHeight();
		
		if ( iw > pw || ih > ph ) {
			double ir = ih / iw;
			double pr = ph / pw;
			double ratio;
			double bottom;
			if ( ir < pr ) {
				ratio = pw / iw;
				bottom = 2;
			}
			else {
				ratio = ph / ih;
				bottom = 8;
			}
			iw = Math.round(iw * ratio) - bottom;
			ih = Math.round(ih * ratio) - bottom;
		}
		
		Image image = icon.getImage().getScaledInstance((int) iw, (int) ih, Image.SCALE_SMOOTH);
		return new ImageIcon(image);
	}

}
