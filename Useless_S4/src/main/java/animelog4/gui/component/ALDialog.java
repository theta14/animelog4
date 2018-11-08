package animelog4.gui.component;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

public class ALDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public ALDialog() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
	
}
