package animelog4.mainactivity;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import animelog4.collection.RemovedImageList;
import animelog4.gui.view.MainFrame;
import animelog4.io.Load;

public class App {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		Load load = new Load();
		load.tva();
		load.watcingTVA();
		load.movie();
		load.userInfo();
		
		MainFrame frame = new MainFrame("Anime Log Ⅳ");
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				RemovedImageList ril = RemovedImageList.getInstance();
				for (int i=0; i<ril.size(); i++)
					if ( ril.get(i).getType() == RemovedImageList.NEW )
						new File("data/image/" + ril.get(i).getKey()).delete();
			}
		});
	}
}
