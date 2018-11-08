package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import animelog4.collection.TypeCollection;
import animelog4.gui.event.ElementRemoveEvent;
import animelog4.gui.event.MenuListener;
import animelog4.type.Movie;
import animelog4.type.TVA;

public class BasePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final BasePanel instance = new BasePanel();
	
	private JPanel panel;
	private TypePanel tp;
	private boolean isTVAPanelOn;
	
	private BasePanel() {
		setLayout(new BorderLayout());
		panel = new JPanel(new BorderLayout());
		tp = TVAPanel.getInstance();
		isTVAPanelOn = true;
		
		JButton changePanel = new JButton("극장판");	// First seen panel is TVA panel, so its text is 'Movie'
		JButton add = new JButton("추가");
		JButton remove = new JButton("삭제");
		JButton menu = new JButton(new ImageIcon("menu.png"));
		
		changePanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				btn.setText(isTVAPanelOn ? "TVA" : "극장판");
				changePanel();
			}
		});
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddToCollection atc = isTVAPanelOn ? new AddToTVA() : new AddToMovie();
				atc.setDialog();
				atc.addEventToCollection();
				atc.show();
			}
		});
		remove.addActionListener(new ElementRemoveEvent().getActionListener());
		
		menu.addActionListener(new MenuListener(this));
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> a = new ArrayList<String>();
				TypeCollection tc = TypeCollection.getInstance();
				
				for ( String key : tc.getTVAMap().keySet() ) {
					Map<Integer, TVA> map = tc.getTVAMap().get(key).getElementMap();
					for ( int intKey : map.keySet() ) {
						a.add("{" + map.get(intKey).getKOR() + " / " + map.get(intKey).getAddress() + "}");
					}
				}
				Collections.sort(a);
				System.out.println(a);
				System.out.println(a.size());
				a = new ArrayList<String>();
				
				for ( String key : tc.getMovieMap().keySet() ) {
					Map<Integer, Movie> map = tc.getMovieMap().get(key).getElementMap();
					for ( int intKey : map.keySet() ) {
						a.add("{" + map.get(intKey).getKOR() + " / " + map.get(intKey).getAddress() + "}");
					}
				}
				Collections.sort(a);
				System.out.println(a);
				System.out.println(a.size());
			}
		});
		
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
		tp = isTVAPanelOn ? MoviePanel.getInstance() : TVAPanel.getInstance();
		isTVAPanelOn = !isTVAPanelOn;
		panel.add((JPanel) tp);
		panel.revalidate();
		panel.repaint();
	}
	
	public TypePanel getElementPanel() {
		return tp;
	}
	
	public boolean isTVAPanelOn() {
		return isTVAPanelOn;
	}
	
}
