package animelog4.gui.event;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import animelog4.collection.RemovedImageList;
import animelog4.collection.UserInfo;
import animelog4.gui.component.ExtensionFileFilter;
import animelog4.gui.view.MovieDetail;
import animelog4.type.Movie;

public class AddNewImage {
	
	public AddNewImage(Component component, JPanel imagePane, Movie movie) {
		UserInfo ui = UserInfo.getInstance();
		RemovedImageList ril = RemovedImageList.getInstance();
		
		JFileChooser fc = new JFileChooser(ui.getImageFilePath());
		String ext[] = { ".bmp", ".dib", ".jpg", "jpeg", ".jpe", ".jfif", ".gif", ".tif", ".tfii", ".png", ".ico" };
		String exts = ext[0];
		for (int i=1; i<ext.length; i++) exts += ", " + ext[i];
		ExtensionFileFilter filter = new ExtensionFileFilter(ext, "Image File (" + exts + ")");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		if ( fc.showOpenDialog(component) != JFileChooser.APPROVE_OPTION ) return;
		
		File selectedFile = fc.getSelectedFile();
		ui.setImageFilePath(selectedFile.getParent());
		String fileName = selectedFile.getName();
		String newImageName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf('.'));
		File saveLocation = new File("data/image/" + newImageName);
		try {
			FileInputStream fi = new FileInputStream(selectedFile);
			FileOutputStream fo = new FileOutputStream(saveLocation);
			byte[] buf = new byte[1024 * 10];
			while (true) {
				int n = fi.read(buf);
				fo.write(buf, 0, n);
				if ( n < buf.length ) break;
			}
			fi.close();
			fo.close();
			
			ril.add(new RemovedImageList.ImageInfo(RemovedImageList.PAST, movie.getImageKey()));
			ril.add(new RemovedImageList.ImageInfo(RemovedImageList.NEW, newImageName));
			movie.setImageKey(newImageName);
			
			ImageIcon icon = MovieDetail.getProperIcon("data/image/" + movie.getImageKey(), imagePane);
			JLabel la = new JLabel(icon, JLabel.CENTER);
			
			imagePane.removeAll();
			imagePane.add(la);
			imagePane.revalidate();
			imagePane.repaint();
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "사진 추가/변경 오류", "오류", JOptionPane.ERROR_MESSAGE);
		}
	}
}
