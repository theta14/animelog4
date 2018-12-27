package animelog4.gui.component;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import animelog4.collection.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ALDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JButton saveButton;
	private boolean isFixing;
	
	public ALDialog() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				popup();
			}
		});
		try {
			setIconImage(ImageIO.read(new File("icon.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ALDialog(String title) {
		setTitle(title);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				popup();
			}
		});
		try {
			setIconImage(ImageIO.read(new File("icon.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setWidth(int width) {
		setSize(width, getHeight());
	}
	
	public void setHeight(int height) {
		setSize(getWidth(), height);
	}
	
	public void setVisible(boolean b) {
		if ( b )
			new Util().addKeyEventToAllComponents(this, KeyEvent.VK_ESCAPE, new EventToExecute() {
				public void execute() {
					popup();
					dispose();
				}
			});
		super.setVisible(b);
	}
	
	private void popup() {
		UserInfo ui = UserInfo.getInstance();
		if ( !ui.getSavePopUp() ) {
			if ( isFixing ) {
				JCheckBox chbx = new JCheckBox("다시 표시하지 않기");
				int ans = JOptionPane.showConfirmDialog(ALDialog.this, new Object[] { "변경사항이 있습니다.\n저장하시겠습니까?" , chbx }, "변경사항", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if ( chbx.isSelected() ) UserInfo.getInstance().setSavePopUp(true);
				if ( ans == JOptionPane.YES_OPTION ) saveButton.doClick();
			}
		}
	}
	
}
