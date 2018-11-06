package animelog4.gui.event;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import animelog4.gui.component.ALDialog;
import animelog4.gui.component.OriginalImage;
import animelog4.type.Movie;

public class ImageClickEvent extends MouseAdapter {
	private Movie movie;
	private Component component;
	
	public ImageClickEvent(Movie movie, Component component) {
		this.movie = movie;
		this.component = component;
	}
	
	public void mouseClicked(MouseEvent e) {
		final JPanel imagePane = (JPanel) e.getSource();
		String path = "data/image/" + movie.getImageKey();
		final File file = new File(path);
		if ( !file.exists() ) {
			new AddNewImage(component, imagePane, movie);
			return;
		}
		final ALDialog di = new ALDialog("이미지 정보");
		BorderLayout bl = new BorderLayout();
		di.setLayout(bl);
		di.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e1) {
				if ( e1.getKeyCode() == KeyEvent.VK_ESCAPE )
					di.dispose();
			}
		});
		di.setFocusable(true);
		
		String labelNames[] = {
			"이름 : " + movie.getImageKey(),
			"해상도 : " + new ImageIcon(path).getIconWidth() + " x " + new ImageIcon(path).getIconHeight(),
			"크기 : " + NumberFormat.getInstance().format(file.length()) + " byte"
		};
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(labelNames.length, 1));
		for ( String s : labelNames ) grid.add(new JLabel(s));
		
		JPanel south = new JPanel();
		JButton originalImg = new JButton("원본 이미지");
		originalImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				di.dispose();
				new OriginalImage(file.getPath());
			}
		});
		JButton close = new JButton("이미지 변경");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				di.dispose();
				new AddNewImage(component, imagePane, movie);
			}
		});
		south.add(originalImg);
		south.add(close);
		
		di.add(grid);
		di.add(south, BorderLayout.SOUTH);
		di.add(new JPanel(), BorderLayout.WEST);
		di.pack();
		double h = di.getHeight();
		di.setSize((int) (di.getWidth() * 1.2), (int) (h *= 1.5));
		bl.setHgap((int) (h / 6.0));
		di.setLocationRelativeTo(component);
		di.setModal(true);
		di.setResizable(false);
		di.setVisible(true);
	}
}
