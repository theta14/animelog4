package animelog4.gui.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(BasePanel.getInstance());
		
		try {
			setIconImage(ImageIO.read(new File("icon.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		setSize((int) (width / 1.5), (int) (height / 1.8));
		setLocationRelativeTo(null);
	}
}
