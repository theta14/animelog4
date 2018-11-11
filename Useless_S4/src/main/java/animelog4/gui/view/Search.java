package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import animelog4.gui.component.ALDialog;
import animelog4.gui.component.RequestFocusListener;
import animelog4.gui.component.SearchedMovie;
import animelog4.gui.component.SearchedTVA;

public class Search {
	private ALDialog di;
	private String item;
	private SearchedTVA st;
	private SearchedMovie sm;
	
	public Search(String item) {
		this.item = item;
		di = new ALDialog();
		
		final JTextField searchField = new JTextField();
		final JButton searchButton = new JButton(new ImageIcon("search.png"));
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchButton.doClick();
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = searchField.getText().trim();
				if ( s.isEmpty() ) {
					JOptionPane.showMessageDialog(di, "검색할 내용이 없습니다.", "검색", JOptionPane.WARNING_MESSAGE);
					return;
				}
				Search search = new Search(s);
				if ( search.numberOfSearched() == 0 ) {
					JOptionPane.showMessageDialog(di, "검색결과가 없습니다.", "검색결과 없음", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				di.dispose();
				search.setDialog();
				search.show();
			}
		});
		searchField.addAncestorListener(new RequestFocusListener());
		
		JPanel north = new JPanel(new BorderLayout());
		north.add(searchField);
		north.add(searchButton, BorderLayout.WEST);
		
		di.add(north, BorderLayout.NORTH);
		
		st = new SearchedTVA(item, di);
		sm = new SearchedMovie(item, di);
	}
	
	public void setDialog() {
		if ( st.numOfSearched() + sm.numOfSearched() == 0 )
			JOptionPane.showMessageDialog(BasePanel.getInstance(), "검색결과가 없습니다.", "검색결과 없음", JOptionPane.INFORMATION_MESSAGE);
		else {
			if ( sm.numOfSearched() == 0 ) {
				di.setTitle(String.format("[%s] 검색결과 %d건", item, st.numOfSearched()));
				di.add(st);
				st.setEventWhenTVAOnlySearched();
			}
			else if ( st.numOfSearched() == 0 ) {
				di.setTitle(String.format("[%s] 검색결과 %d건", item, sm.numOfSearched()));
				di.add(sm);
			}
			else {
				di.setTitle(String.format("[%s] 검색결과 %d건", item, st.numOfSearched() + sm.numOfSearched()));
				JPanel p = new JPanel(new GridLayout(1, 2));
				
				JTextField tvaField = new JTextField(String.format("TVA %d건", st.numOfSearched()));
				tvaField.setEditable(false);
				tvaField.setBackground(Color.WHITE);
				tvaField.setHorizontalAlignment(JTextField.CENTER);
				JPanel tvaPanel = new JPanel(new BorderLayout());
				tvaPanel.add(st);
				tvaPanel.add(tvaField, BorderLayout.NORTH);
				
				JTextField movieField = new JTextField(String.format("극장판 %d건", sm.numOfSearched()));
				movieField.setEditable(false);
				movieField.setBackground(Color.WHITE);
				movieField.setHorizontalAlignment(JTextField.CENTER);
				JPanel moviePanel = new JPanel(new BorderLayout());
				moviePanel.add(sm);
				moviePanel.add(movieField, BorderLayout.NORTH);
				
				st.setSearchedMovie(sm);
				p.add(tvaPanel);
				p.add(moviePanel);
				di.add(p);
			}
		}
	}
	
	public void show() {
		di.pack();
		di.setWidth((int) (di.getWidth() * 1.2));
		di.setLocationRelativeTo(BasePanel.getInstance());
		di.setModal(true);
		di.setVisible(true);
	}
	
	public int numberOfSearched() {
		return st.numOfSearched() + sm.numOfSearched();
	}
	
}
