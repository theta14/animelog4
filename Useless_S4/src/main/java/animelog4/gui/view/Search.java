package animelog4.gui.view;

import animelog4.gui.component.ALDialog;
import animelog4.gui.component.SearchedTVA;

public class Search {
	
	public Search(String item) {
		ALDialog di = new ALDialog();
//		JSplitPane sp = new JSplitPane();
//		
		SearchedTVA st = new SearchedTVA(item);
		di.add(st);
//		sp.add(st);
//		di.add(sp);
		
		di.pack();
		di.setLocationRelativeTo(BasePanel.getInstance());
		di.setModal(true);
		di.setVisible(true);
	}
}
