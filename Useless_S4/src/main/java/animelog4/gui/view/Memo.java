package animelog4.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import animelog4.collection.UserInfo;
import animelog4.gui.component.ALDialog;

public class Memo {
	
	public Memo() {
		final UserInfo ui = UserInfo.getInstance();
		final ALDialog di = new ALDialog("메모");
		final JTextArea ta = new JTextArea(ui.getMemo());
		ta.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				di.setTitle("*메모");
			}
			public void removeUpdate(DocumentEvent e) {
				di.setTitle("*메모");
			}
			public void changedUpdate(DocumentEvent e) {
				di.setTitle("*메모");
			}
		});
		di.add(new JScrollPane(ta));
		
		JButton save = new JButton("저장");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ui.setMemo(ta.getText());
				di.setTitle("메모");
			}
		});
		
		di.add(save, BorderLayout.SOUTH);
//		di.pack();
		di.setSize(500, 500);
		di.setLocationRelativeTo(BasePanel.getInstance());
		di.setModal(true);
		di.setVisible(true);
	}
	
}
