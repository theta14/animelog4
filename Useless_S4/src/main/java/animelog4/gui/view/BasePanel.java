package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import animelog4.gui.event.ElementRemoveEvent;
import animelog4.gui.event.MenuListener;

public class BasePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final BasePanel instance = new BasePanel();
	
	private JPanel panel;
	private TypePanel tp;
	
	private BasePanel() {
		setLayout(new BorderLayout());
		panel = new JPanel(new BorderLayout());
		tp = TVAPanel.getInstance();
		
		JButton changePanel = new JButton("극장판");	// First seen panel is TVA panel, so its text is 'Movie'
		JButton add = new JButton("추가");
		JButton remove = new JButton("삭제");
		JButton menu = new JButton(new ImageIcon("menu.png"));
		
		changePanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				if ( tp.getType() == TypePanel.TVA ) btn.setText("TVA");
				else if ( tp.getType() == TypePanel.MOVIE ) btn.setText("극장판");
				changePanel();
			}
		});
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddToCollection atc = null;
				if ( tp.getType() == TypePanel.TVA ) atc = new AddToTVA();
				else if ( tp.getType() == TypePanel.MOVIE ) atc = new AddToMovie();
				else {
					System.out.println("Unknown error occured near (BasePanel.java:52)");
					return;
				}
				atc.setDialog();
				atc.addEventToCollection();
				atc.show();
			}
		});
		remove.addActionListener(new ElementRemoveEvent().getActionListener());
		menu.addActionListener(new MenuListener(this));
		
		JPanel south = new JPanel(new BorderLayout());
		JPanel south_center = new JPanel(new GridLayout(1, 3));
		south_center.add(changePanel);
		south_center.add(add);
		south_center.add(remove);
		
		south.add(south_center);
		south.add(menu, BorderLayout.EAST);
		add(south, BorderLayout.SOUTH);
		panel.add((JPanel) tp);
		add(panel);
	}
	
	public static BasePanel getInstance() {
		return instance;
	}
	
	private void changePanel() {
		panel.removeAll();
		if ( tp.getType() == TypePanel.TVA ) tp = MoviePanel.getInstance();
		else if ( tp.getType() == TypePanel.MOVIE ) tp = TVAPanel.getInstance();
		panel.add((JPanel) tp);
		panel.revalidate();
		panel.repaint();
	}
	
	public TypePanel getElementPanel() {
		return tp;
	}
	
}
