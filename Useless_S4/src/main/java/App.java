import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import animelog4.gui.view.MainFrame;
import animelog4.io.Load;

public class App {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		Load load = new Load();
		load.tva();
		load.movie();
		
		MainFrame frame = new MainFrame("Anime Log Ⅳ");
		frame.setVisible(true);
	}
}
