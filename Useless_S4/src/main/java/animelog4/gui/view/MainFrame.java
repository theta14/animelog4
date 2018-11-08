package animelog4.gui.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import animelog4.collection.RemovedImageList;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(BasePanel.getInstance());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				RemovedImageList ril = RemovedImageList.getInstance();
				for (int i=0; i<ril.size(); i++)
					if ( ril.get(i).getType() == RemovedImageList.NEW )
						new File("data/image/" + ril.get(i).getKey()).delete();
			}
		});
		
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
