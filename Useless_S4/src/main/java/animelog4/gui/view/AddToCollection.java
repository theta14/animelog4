package animelog4.gui.view;

import animelog4.type.ElementType;

public interface AddToCollection {
	String s[] = { "KOR", "ENG", "JPN", "제작사" };
	int fieldSize = 20;
	
	void addEventToCollection();
	void setDialog();
	void show();
	void setFromElement(ElementType element);
}
