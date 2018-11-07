package animelog4.gui.event;

import animelog4.collection.TypeCollection;
import animelog4.gui.view.AddToTVA;
import animelog4.type.TVA;

public class DividedElementsAdd {
	private TVA tvas[];
	
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
		}
		if ( qtr != dividedTVA.getQTR() ) return String.format("쿨의 합이 %d(이)가 되어야 합니다.", dividedTVA.getQTR());
		
		for (int i=0; i<att.length-1; i++) {
			for (int j=i+1; j<att.length; j++) {
				String si = (String) att[i].getSeason().getValue();
				si = si.substring(0, si.indexOf('기'));
				int ii = Integer.parseInt(si);
				
				String sj = (String) att[j].getSeason().getValue();
				sj = sj.substring(0, sj.indexOf('기'));
				int ij = Integer.parseInt(sj);
				
				if ( ii == ij ) return "중복되는 시즌이 있습니다.";
			}
		}
		
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
	
	public TVA[] getTVAArray() {
		return tvas;
	}
	
}
