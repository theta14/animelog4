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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import animelog4.collection.TypeCollection;
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
			public void mouseClicked(MouseEvent e) {
				if ( e.getClickCount() == 2 && table.getSelectedColumn() == 1 ) {
					if ( table.getSelectedRow() == 0 ) {
						// have to add about optionpane to select to edit or see information
						TVASeries ts = (TVASeries) table.getValueAt(0, 1);
						TVADetail td = new TVADetail();
						di.dispose();
						td.showSeriesDialog(ts.getKey());
					}
					else if ( table.getSelectedRow() == 1 ) {
						// showSeriesDialog(movie series key)
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
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		di.add(gridbag);
		di.add(save, BorderLayout.SOUTH);
		
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(upperComponent);
		di.setModal(true);
		di.setVisible(true);
	}
	
	private void showSeriesDialog(String address) {
		
	}

}
