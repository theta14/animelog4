package animelog4.collection;

import animelog4.io.Save;
import lombok.Getter;

@Getter
public class UserInfo {
	Save save;
	
	private static final UserInfo instance = new UserInfo();
	
	private boolean savePopUp;
	private String imageFilePath;
	private int selectedTVAHeader;
	private int selectedMovieHeader;
	
	private UserInfo() {
		save = new Save();
	}
	
	/**
	 * If it is true, pop up will not be shown
	 * @return boolean value that about showing pop up
	 * */
	public boolean getSavePopUp() {
		return savePopUp;
	}
	
	public static UserInfo getInstance() {
		return instance;
	}

	/**
	 * If it is true, pop up will not be shown
	 * @param boolean value that about showing pop up
	 * */
	public void setSavePopUp(boolean savePopUp) {
		this.savePopUp = savePopUp;
		save.userInfo();
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
		save.userInfo();
	}

	public void setSelectedTVAHeader(int selectedTVAHeader) {
		this.selectedTVAHeader = selectedTVAHeader;
		save.userInfo();
	}

	public void setSelectedMovieHeader(int selectedMovieHeader) {
		this.selectedMovieHeader = selectedMovieHeader;
		save.userInfo();
	}
	
}
