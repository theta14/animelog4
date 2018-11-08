package animelog4.gui.view;

import animelog4.gui.component.ALTable;

public interface TypePanel {
	int TVA = 0;
	int MOVIE = 1;
	int WATCHING_TVA = 2;
	
	ALTable getTable();
	int getType();
}
