package animelog4.gui.component;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {
	private ArrayList<String> extensions;
	private String description;

	public ExtensionFileFilter(String[] exts, String desc) {
		if ( exts != null ) {
			extensions = new ArrayList<String>();

			for (String ext : exts)
				extensions.add(ext.replace(".", "").trim().toLowerCase());
		}
		description = (desc != null) ? desc.trim() : "Custom File List";
	}

	public boolean accept(File f) {
		if ( f.isDirectory() ) return true;
		if ( extensions == null ) return false;
		
		for (String ext : extensions)
			if ( f.getName().toLowerCase().endsWith("." + ext) )
				return true;
		
		return false;
	}

	public String getDescription() {
		return description;
	}
}
