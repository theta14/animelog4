package animelog4.gui.event;

import java.util.Iterator;

import animelog4.collection.TypeCollection;
import animelog4.gui.view.AddToMovie;
import animelog4.gui.view.AddToTVA;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class DividedElementsAdd {
	private TVA tvas[];
	private Movie movies[];
	
	public String toTVACollection(TVA dividedTVA, AddToTVA att[]) {
		TypeCollection tc = TypeCollection.getInstance();
		String s[] = { "KOR", "ENG", "JPN", "제작사" };
		tvas = new TVA[att.length];
		int qtr = 0;
		boolean seasonDuplicatesWithPast = false;
		for (int i=0; i<att.length; i++) {
			String seasonStr = (String) att[i].getSeason().getValue();
			seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
			int seasonInt = Integer.parseInt(seasonStr);
			if ( seasonInt == dividedTVA.getSeason() ) seasonDuplicatesWithPast = true;
			else if ( tc.getTVAMap().get(dividedTVA.getSeriesKey()).getElementMap().get(seasonInt) != null )
				return String.format("시리즈 내에 같은 시즌이 있습니다 (%d번째 패널)", i+1);
			qtr += (Integer) att[i].getSpnr().getValue();
			
			for (int j=0; j<att[i].getTf().length; j++) {
				if ( att[i].getTf()[j].getText().trim().isEmpty() ) return String.format("%d번째 패널 %s 필드가 비어있습니다.", j+1, s[j]);
				else att[i].getTf()[j].setText(att[i].getTf()[j].getText().trim());
			}
			
			for (int j=i+1; j<att.length; j++) {
				String sj = (String) att[j].getSeason().getValue();
				sj = sj.substring(0, sj.indexOf('기'));
				int ij = Integer.parseInt(sj);
				
				if ( seasonInt == ij ) return "중복되는 시즌이 있습니다.";
			}
		}
		if ( qtr != dividedTVA.getQTR() ) return String.format("쿨의 합이 %d(이)가 되어야 합니다.", dividedTVA.getQTR());
		
		final String seriesKey = dividedTVA.getSeriesKey();
		for (int i=0; i<att.length; i++) {
			String seasonStr = (String) att[i].getSeason().getValue();
			seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
			
			int representValue = -1;
			for (int j=0; j<att[i].getRbtn().length; j++) {
				if ( att[i].getRbtn()[j].isSelected() ) {
					representValue = j;
					break;
				}
			}
			if ( representValue == -1 ) return "Unknown error while setting representValue near line (ElementAddEvent.java:60)";
			
			TVA t = new TVA(att[i].getTf()[0].getText(), att[i].getTf()[1].getText(), att[i].getTf()[2].getText(), att[i].getTf()[3].getText(), att[i].getTa().getText().trim(), (Integer) att[i].getSpnr().getValue(), Integer.parseInt(seasonStr), representValue);
			tc.getTVAMap().get(seriesKey).add(t);
			tvas[i] = t;
		}
		if ( !seasonDuplicatesWithPast ) tc.removeTVAByAddress(dividedTVA.getAddress());
		return null;
	}
	
	public String fixedTVAEntirely(TVASeries ts, AddToTVA[] att) {
		String s[] = { "KOR", "ENG", "JPN", "제작사" };
		tvas = new TVA[att.length];
		for (int i=0; i<att.length; i++) {
			String seasonStr = (String) att[i].getSeason().getValue();
			seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
			int seasonInt = Integer.parseInt(seasonStr);
			
			for (int j=i+1; j<att.length; j++) {
				String seasonStr2 = (String) att[j].getSeason().getValue();
				seasonStr2 = seasonStr2.substring(0, seasonStr2.indexOf('기'));
				int seasonInt2 = Integer.parseInt(seasonStr2);
				if ( seasonInt == seasonInt2 )
					return String.format("중복되는 시즌이 있습니다 (%d번째 패널, %d번째 패널)", i+1, j+1);
			}
			
			for (int j=0; j<att[i].getTf().length; j++) {
				if ( att[i].getTf()[j].getText().trim().isEmpty() ) return String.format("%d번째 패널 %s 필드가 비어있습니다.", j+1, s[j]);
				else att[i].getTf()[j].setText(att[i].getTf()[j].getText().trim());
			}
		}
		
		for (int i=0; i<att.length; i++) {
			String seasonStr = (String) att[i].getSeason().getValue();
			seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
			
			int representValue = -1;
			for (int j=0; j<att[i].getRbtn().length; j++) {
				if ( att[i].getRbtn()[j].isSelected() ) {
					representValue = j;
					break;
				}
			}
			if ( representValue == -1 ) return "Unknown error while setting representValue near line (ElementAddEvent.java:104)";
			
			TVA t = new TVA(att[i].getTf()[0].getText(), att[i].getTf()[1].getText(), att[i].getTf()[2].getText(), att[i].getTf()[3].getText(), att[i].getTa().getText().trim(), (Integer) att[i].getSpnr().getValue(), Integer.parseInt(seasonStr), representValue);
			tvas[i] = t;
		}
		return null;
	}
	
	public String fixedMovieEntirely(MovieSeries ms, AddToMovie[] atm) {
		String s[] = { "KOR", "ENG", "JPN", "제작사" };
		movies = new Movie[atm.length];
		
		for (int i=0; i<atm.length; i++) {
			for (int j=i+1; j<atm.length; j++)
				if ( ((Integer) atm[i].getSpnr().getValue()).equals((Integer) atm[j].getSpnr().getValue()) )
					return String.format("중복되는 시즌이 있습니다 (%d번째 패널, %d번째 패널)", i+1, j+1);
			
			for (int j=0; j<atm[i].getTf().length; j++) {
				if ( atm[i].getTf()[j].getText().trim().isEmpty() ) return String.format("%d번째 패널 %s 필드가 비어있습니다.", j+1, s[j]);
				else atm[i].getTf()[j].setText(atm[i].getTf()[j].getText().trim());
			}
		}
		
		Iterator<Movie> iterator = ms.getElementMap().values().iterator();
		for (int i=0; i<atm.length; i++) {
			Movie m = new Movie(iterator.next());
			m.setKOR(atm[i].getTf()[0].getText());
			m.setENG(atm[i].getTf()[1].getText());
			m.setJPN(atm[i].getTf()[2].getText());
			m.setPD(atm[i].getTf()[3].getText());
			m.setOrder((Integer) atm[i].getSpnr().getValue());
			m.setNote(atm[i].getTa().getText());
			movies[i] = m;
		}
		return null;
	}
	
	public TVA[] getTVAArray() {
		return tvas;
	}
	
	public Movie[] getMovieArray() {
		return movies;
	}
	
}
