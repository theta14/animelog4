package animelog4.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.component.ALTable;
import animelog4.gui.view.MoviePanel;
import animelog4.gui.view.TVAPanel;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class ElementAddEvent {
	private ALDialog di;
	
	public ElementAddEvent(ALDialog di) {
		this.di = di;
	}
	
	public ActionListener getTVAActionListener(final JComboBox<TVASeries> cbx, final JCheckBox chbox, final JTextField series, final JRadioButton rbtn[], final JTextField tf[], final JSpinner spnr, final JSpinner season, final JTextArea ta) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TypeCollection tc = TypeCollection.getInstance();
				final String s[] = { "KOR", "ENG", "JPN", "제작사" };
				
				if ( series.getText().trim().isEmpty() ) {
					JOptionPane.showMessageDialog(di, "시리즈 타이틀을 입력해야 합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else series.setText(series.getText().trim());
				for (int i=0; i<tf.length; i++) {
					if ( tf[i].getText().trim().isEmpty() ) {
						JOptionPane.showMessageDialog(di, String.format("%s 필드가 비어있습니다.", s[i]), "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else tf[i].setText(tf[i].getText().trim());
				}
				
				int ans = JOptionPane.showConfirmDialog(di, "저장하시겠습니까?", "저장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if ( ans != JOptionPane.YES_OPTION ) return;
				
				
				String seasonStr = (String) season.getValue();
				seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
				
				int representValue = -1;
				for (int i=0; i<rbtn.length; i++) {
					if ( rbtn[i].isSelected() ) {
						representValue = i;
						break;
					}
				}
				if ( representValue == -1 ) {
					System.out.println("Unknown error while setting representValue (ElementAddEvent.java:64)");
					return;
				}
				
				TVA t = new TVA(tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), ta.getText().trim(), (Integer) spnr.getValue(), Integer.parseInt(seasonStr), representValue);
				TVASeries ts;
				if ( chbox.isSelected() ) {
					ts = new TVASeries(series.getText());
					ts.add(t);
					tc.getTVAMap().put(ts.getKey(), ts);
				}
				else {
					ts = (TVASeries) cbx.getSelectedItem();
					if ( tc.getTVAMap().get(ts.getKey()).getElementMap().get(t.getSeason()) != null ) {
						JOptionPane.showMessageDialog(di, t.getSeason() + "기가 이미 존재합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					tc.getTVAMap().get(ts.getKey()).add(t);
				}
				String row[] = { ts.getTitleFrontChar(), t.getKOR(), t.getENG(), t.getJPN(), t.getPD(), Integer.toString(t.getQTR()), t.getAddress() };
				TVAPanel.getInstance().getTable().getDefaultTableModel().addRow(row);
				di.dispose();
			}
		};
	}
	
	public ActionListener getWatchingTVAActionListener(final ALTable table, final JComboBox<TVASeries> cbx, final JCheckBox chbox, final JTextField series, final JRadioButton rbtn[], final JTextField tf[], final JSpinner spnr, final JSpinner season, final JTextArea ta) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TypeCollection tc = TypeCollection.getInstance();
				final String s[] = { "KOR", "ENG", "JPN", "제작사" };
				
				for (int i=0; i<tf.length; i++) {
					if ( tf[i].getText().trim().isEmpty() ) {
						JOptionPane.showMessageDialog(di, String.format("%s 필드가 비어있습니다.", s[i]), "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else tf[i].setText(tf[i].getText().trim());
				}
				
				int ans = JOptionPane.showConfirmDialog(di, "저장하시겠습니까?", "저장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if ( ans != JOptionPane.YES_OPTION ) return;
				
				
				String seasonStr = (String) season.getValue();
				seasonStr = seasonStr.substring(0, seasonStr.indexOf('기'));
				
				int representValue = -1;
				for (int i=0; i<rbtn.length; i++) {
					if ( rbtn[i].isSelected() ) {
						representValue = i;
						break;
					}
				}
				if ( representValue == -1 ) {
					System.out.println("Unknown error while setting representValue (ElementAddEvent.java:64)");
					return;
				}
				
				TVA t = new TVA(tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), ta.getText().trim(), (Integer) spnr.getValue(), Integer.parseInt(seasonStr), representValue);
				String title = null;
				if ( chbox.isSelected() ) {
					t.setSeriesKey('x' + Long.toHexString(System.currentTimeMillis()));
					switch ( representValue ) {
					case 0:
						title = t.getKOR().substring(0, 2);
						break;
					case 1:
						title = t.getENG().substring(0, 2);
						break;
					case 2:
						title = t.getJPN().substring(0, 2);
						break;
					}
				}
				else {
					TVASeries ts = (TVASeries) cbx.getSelectedItem();
					if ( tc.getTVAMap().get(ts.getKey()).getElementMap().get(t.getSeason()) != null ) {
						JOptionPane.showMessageDialog(di, t.getSeason() + "기가 이미 존재합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					t.setSeriesKey(ts.getKey());
					title = ts.getTitleFrontChar();
				}
				tc.getWatchingTVAMap().put(t.getAddress(), t);
				
				String row[] = { title, t.getKOR(), t.getENG(), t.getJPN(), t.getPD(), Integer.toString(t.getQTR()), t.getAddress() };
				if ( table != null ) table.getDefaultTableModel().addRow(row);
				di.dispose();
			}
		};
	}
	
	public ActionListener getMovieActionListener(final JComboBox<TVASeries> cbx, final JTextField tf[], final JSpinner spnr, final JTextArea ta) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TypeCollection tc = TypeCollection.getInstance();
				final String s[] = { "KOR", "ENG", "JPN", "제작사" };
				
				for (int i=0; i<tf.length; i++) {
					if ( tf[i].getText().trim().isEmpty() ) {
						JOptionPane.showMessageDialog(di, String.format("%s 필드가 비어있습니다.", s[i]), "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else tf[i].setText(tf[i].getText().trim());
				}
				
				int ans = JOptionPane.showConfirmDialog(di, "저장하시겠습니까?", "저장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if ( ans != JOptionPane.YES_OPTION ) return;
				
				Movie m = new Movie(tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), ta.getText().trim(), (Integer) spnr.getValue());
				MovieSeries ms;
				String tvaSeriesKey = ((TVASeries) cbx.getSelectedItem()).getKey();
				if ( (ms = tc.getMovieSeriesWithTVAKey(tvaSeriesKey)) == null ) {
					ms = new MovieSeries(tvaSeriesKey);
					ms.add(m);
					tc.getMovieMap().put(ms.getKey(), ms);
				}
				else {
					if ( tc.getMovieMap().get(ms.getKey()).getElementMap().get(m.getOrder()) != null ) {
						JOptionPane.showMessageDialog(di, m.getOrder() + "번째가 이미 존재합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					tc.getMovieMap().get(ms.getKey()).add(m);
				}
				String row[] = { tc.getTVAMap().get(ms.getTVASeriesKey()).getTitle(), m.getKOR(), m.getENG(), m.getJPN(), m.getPD(), m.getAddress() };
				MoviePanel.getInstance().getTable().getDefaultTableModel().addRow(row);
				di.dispose();
			}
		};
	}
	
}
