package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.RequestFocusListener;
import animelog4.gui.event.ElementAddEvent;
import animelog4.type.TVASeries;
import lombok.Getter;

@Getter
public class AddToTVA implements AddToCollection {
	private ALDialog di;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private JPanel center;
	private JButton save;
	
	private JComboBox<TVASeries> cbx;
	private JCheckBox chbox;
	private JRadioButton rbtn[];
	private JTextField series, tf[];
	private JSpinner spnr, season;
	private JTextArea ta;
	
	public AddToTVA() {
		di = new ALDialog("추가");
		
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		center = new JPanel();
		tf = new JTextField[s.length];
		rbtn = new JRadioButton[3];
		ta = new JTextArea(4, fieldSize);
		
		spnr = new JSpinner();
		spnr.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		JComponent editor = spnr.getEditor();
		if ( editor instanceof JSpinner.DefaultEditor ) {
			JSpinner.DefaultEditor spnrEditor = (JSpinner.DefaultEditor) editor;
			spnrEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		}
		
		season = new JSpinner();
		String seasonList[] = new String[15];	// commonly, season can't be higher than 15, and even can't reach to 15 
		for (int i=0; i<seasonList.length; i++) seasonList[i] = (i+1) + "기";
		season.setModel(new SpinnerListModel(seasonList));
		editor = season.getEditor();
		if ( editor instanceof JSpinner.DefaultEditor ) {
			JSpinner.DefaultEditor spnrEditor = (JSpinner.DefaultEditor) editor;
			spnrEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		}
		
		chbox = new JCheckBox("새로 작성");
		series = new JTextField();
		
		ButtonGroup bg = new ButtonGroup();
		for (int i=0; i<rbtn.length; i++) {
			tf[i] = new JTextField(fieldSize);
			rbtn[i] = new JRadioButton(s[i]);
			rbtn[i].setHorizontalAlignment(JRadioButton.CENTER);
			bg.add(rbtn[i]);
		}
		rbtn[0].setSelected(true);
		tf[3] = new JTextField(fieldSize);
	}
	
	public void setDialog() {
		final TypeCollection tc = TypeCollection.getInstance();
		center.setLayout(gbl);
		
		for (int i=0; i<rbtn.length; i++) {
			add(rbtn[i], 0, i, 1, 1);
			add(tf[i], 1, i, 2, 1);
		}
		add(new JLabel(s[3], JLabel.CENTER), 0, 3, 1, 1);
		add(tf[3], 1, 3, 2, 1);
		
		tf[3].setEditable(false);
		tf[3].setBackground(Color.WHITE);
		tf[3].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				final JComboBox<String> cbx = new JComboBox<String>(tc.getPDArray());
				final JTextField t = new JTextField(tf[3].getText());
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
				tf[3].setText(t.getText());
			}
		});
		
		add(new JLabel("쿨", JLabel.CENTER), 0, 4, 1, 1);
		add(spnr, 1, 4, 2, 1);
		add(new JLabel("시즌", JLabel.CENTER), 0, 5, 1, 1);
		add(season, 1, 5, 1, 1);
		add(new JLabel("비고", JLabel.CENTER), 0, 6, 1, 1);
		add(new JScrollPane(ta), 1, 6, 1, 1);
		
		JPanel north = new JPanel(new GridLayout(2, 1));
		JPanel north2 = new JPanel(new BorderLayout());
		
		Vector<TVASeries> v = new Vector<TVASeries>(tc.getTVAMap().values());	// declaration as Vector to put to JComboBox
		Collections.sort(v);
		cbx = new JComboBox<TVASeries>(v);
		
		cbx.setEnabled(false);
		chbox.setSelected(true);
		series.addAncestorListener(new RequestFocusListener());
		
		cbx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				series.setText(cbx.getSelectedItem().toString());
				setSeasonSpinner(cbx, season);
			}
		});
		chbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbx.setEnabled(!chbox.isSelected());
				series.setEnabled(chbox.isSelected());
				if ( chbox.isSelected() ) {
					series.setText("");
					season.setValue("1기");
				}
				else {
					series.setText(cbx.getSelectedItem().toString());
					setSeasonSpinner(cbx, season);
				}
			}
		});
		north.add(cbx);
		north.add(north2);
		north2.add(series);
		north2.add(chbox, BorderLayout.WEST);
		
		
		JPanel south = new JPanel();
		save = new JButton("저장");
		JButton close = new JButton("닫기");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				di.dispose();
			}
		});
		south.add(save);
		south.add(close);
		
		di.add(center);
		di.add(north, BorderLayout.NORTH);
		di.add(south, BorderLayout.SOUTH);
	}
	
	private void setSeasonSpinner(JComboBox<TVASeries> cbx, JSpinner spnr) {
		int lastKey = ((TVASeries) cbx.getSelectedItem()).getElementMap().lastKey();
		spnr.setValue((lastKey + 1) + "기");
	}
	
	public void show() {
		di.pack();
		di.setLocationRelativeTo(TVAPanel.getInstance());
		di.setModal(true);
		di.setVisible(true);
	}
	
	public void show(Component component) {
		di.pack();
		di.setLocationRelativeTo(component);
		di.setModal(true);
		di.setVisible(true);
	}
	
	public void addEventToCollection() {
		ElementAddEvent eae = new ElementAddEvent(di);
		save.addActionListener(eae.getTVAActionListener(cbx, chbox, series, rbtn, tf, spnr, season, ta));
	}
	
	private void add(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbl.setConstraints(c, gbc);
		center.add(c);
	}
	
}
