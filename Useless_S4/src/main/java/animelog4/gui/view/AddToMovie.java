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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.RequestFocusListener;
import animelog4.gui.event.ElementAddEvent;
import animelog4.type.MovieSeries;
import animelog4.type.TVASeries;
import lombok.Getter;

@Getter
public class AddToMovie implements AddToCollection {
	private ALDialog di;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private JPanel center;
	private JPanel south;
	
	JComboBox<TVASeries> cbx;
	JTextField tf[];
	JSpinner spnr;
	JTextArea ta;
	JButton save;
	
	public AddToMovie() {
		di = new ALDialog("추가");
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		center = new JPanel();
		tf = new JTextField[s.length];
		ta = new JTextArea(4, fieldSize);
		spnr = new JSpinner();
	}
	
	public void setDialog() {
		final TypeCollection tc = TypeCollection.getInstance();
		center.setLayout(gbl);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		Vector<TVASeries> v = new Vector<TVASeries>(tc.getTVAMap().values());	// declaration as Vector to put to JComboBox
		Collections.sort(v);
		
		cbx = new JComboBox<TVASeries>(v);
		add(new JLabel("원작", JLabel.CENTER), 0, 0, 1, 1);
		add(cbx, 1, 0, 2, 1);
		
		for (int i=0; i<tf.length; i++) {
			tf[i] = new JTextField(fieldSize);
			add(new JLabel(s[i], JLabel.CENTER), 0, i+1, 1, 1);
			add(tf[i], 1, i+1, 2, 1);
		}
		
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
		
		spnr.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		JComponent editor = spnr.getEditor();
		if ( editor instanceof JSpinner.DefaultEditor ) {
			JSpinner.DefaultEditor spnrEditor = (JSpinner.DefaultEditor) editor;
			spnrEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		}
		
		add(new JLabel("순서", JLabel.CENTER), 0, 5, 1, 1);
		add(spnr, 1, 5, 2, 1);
		add(new JLabel("비고", JLabel.CENTER), 0, 6, 1, 1);
		add(new JScrollPane(ta), 1, 6, 1, 1);
		
		setOrderSpinner(cbx, spnr);
		cbx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOrderSpinner(cbx, spnr);
			}
		});
		
		south = new JPanel();
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
		di.add(south, BorderLayout.SOUTH);
	}
	
	private void setOrderSpinner(JComboBox<TVASeries> cbx, JSpinner spnr) {
		String tvaSeriesKey = ((TVASeries) cbx.getSelectedItem()).getKey();
		MovieSeries ms;
		if ( (ms = TypeCollection.getInstance().getMovieSeriesWithTVAKey(tvaSeriesKey)) != null )
			spnr.setValue(ms.getElementMap().lastKey() + 1);
		else spnr.setValue(1);
	}
	
	public void show() {
		di.pack();
		di.setLocationRelativeTo(MoviePanel.getInstance());
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
		save.addActionListener(eae.getMovieActionListener(cbx, tf, spnr, ta));
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
