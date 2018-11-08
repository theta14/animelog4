package animelog4.gui.component;

import java.io.IOException;

public class OriginalImage {
	
	public OriginalImage(String path) {
		try {
			new ProcessBuilder(new String[]{ "rundll32", "url.dll", "FileProtocolHandler", path }).start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
