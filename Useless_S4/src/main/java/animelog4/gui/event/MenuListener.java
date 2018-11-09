package animelog4.gui.event;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import animelog4.collection.RemovedImageList;
import animelog4.collection.TypeCollection;
import animelog4.gui.component.ALDialog;
import animelog4.gui.view.WatchingTVA;
import animelog4.io.Save;
import animelog4.type.Movie;
import animelog4.type.TVA;

public class MenuListener implements ActionListener {
	private static int selection = 0;
	private Component component;
	
	public MenuListener(Component component) {
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		final String rbtns[] = { "정보", "검색", "시청 중인 TVA", "시청 예정 목록", "파일 저장", "종료", "디버깅용" };
		JRadioButton rbtn[] = new JRadioButton[rbtns.length];
		ButtonGroup g = new ButtonGroup();
		JPanel p = new JPanel();
		p.setLayout(new GridLayout((rbtns.length+1)/2, 2));
		for (int i=0; i<rbtns.length; i++) {
			rbtn[i] = new JRadioButton(rbtns[i]);
			g.add(rbtn[i]);
			p.add(rbtn[i]);
			final int $i = i;
			rbtn[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e1) {
					selection = $i;
				}
			});
		}
		rbtn[selection].setSelected(true);
		
		JPanel q = new JPanel(new BorderLayout(5, 5));
		q.add(p);
		q.add(new JLabel("원하는 메뉴를 선택하세요."), BorderLayout.NORTH);
		int answer = JOptionPane.showConfirmDialog(component, q, "메뉴", JOptionPane.OK_CANCEL_OPTION);
		if ( answer != JOptionPane.OK_OPTION ) return;
		
		switch (selection) {
		case 0:
			TypeCollection tc = TypeCollection.getInstance();
			StringBuilder sb = new StringBuilder("TVA 시리즈 수: ");
			sb.append(tc.getTVAMap().size());
			sb.append("\nTVA 수: ");
			
			int numOfTVA = 0;
			int qtr = 0;
			for ( String key : tc.getTVAMap().keySet() ) {
				numOfTVA += tc.getTVAMap().get(key).getElementMap().size();
				for ( int intKey : tc.getTVAMap().get(key).getElementMap().keySet() )
					qtr += tc.getTVAMap().get(key).getElementMap().get(intKey).getQTR();
			}
			
			sb.append(numOfTVA);
			sb.append("\nTVA 전체 쿨: ");
			sb.append(qtr);
			sb.append("\n\n극장판 시리즈 수: ");
			sb.append(tc.getMovieMap().size());
			sb.append("\n극장판 수: ");
			
			int numOfMovie = 0;
			for ( String key : tc.getMovieMap().keySet() ) numOfMovie += tc.getMovieMap().get(key).getElementMap().size();
			sb.append(numOfMovie);
			JOptionPane.showMessageDialog(component, sb.toString(), "정보", JOptionPane.INFORMATION_MESSAGE);
			break;
			
		case 1:
			String s = JOptionPane.showInputDialog(component, "검색어를 입력하세요.", "검색", JOptionPane.QUESTION_MESSAGE);
			try {
				while ( s.trim().isEmpty() )
					s = JOptionPane.showInputDialog(component, "검색할 내용이 없습니다. 검색어를 다시 입력하세요.", "검색", JOptionPane.QUESTION_MESSAGE);
			}
			catch(NullPointerException e1) {
				return;
			}
//			ALDialog di = new SearchDialog(s).getDialog();
			ALDialog di = new ALDialog();
			if ( di == null )
				JOptionPane.showMessageDialog(component, "검색결과가 없습니다!", "'" + s + "'에 대한 검색결과: 0건", JOptionPane.WARNING_MESSAGE);
			else {
				di.setLocationRelativeTo(component);
				di.setModal(true);
				di.setVisible(true);
			}
			break;
			
		case 2:
			new WatchingTVA();
			break;
			
		case 3:
			// to watch list
			break;
			
		case 4:
			Save save = new Save();
			save.tva();
			save.movie();
			save.watchingTVA();
			save.userInfo();
			JOptionPane.showMessageDialog(component, "저장 완료", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
			break;
			
		case 5:
			RemovedImageList ril = RemovedImageList.getInstance();
			for (int i=0; i<ril.size(); i++)
				if ( ril.get(i).getType() == RemovedImageList.NEW )
					new File("image/" + ril.get(i).getKey()).delete();
			System.exit(0);
			break;
			
		default:
			System.out.println("====================================================================================");
			ArrayList<String> a = new ArrayList<String>();
			tc = TypeCollection.getInstance();
			
			for ( String key : tc.getTVAMap().keySet() ) {
				Map<Integer, TVA> map = tc.getTVAMap().get(key).getElementMap();
				for ( int intKey : map.keySet() ) {
					a.add("{" + map.get(intKey).getKOR() + " / " + map.get(intKey).getAddress() + "}");
				}
			}
			Collections.sort(a);
			System.out.println(a);
			System.out.println(a.size());
			
			a = new ArrayList<String>();
			for ( String key : tc.getMovieMap().keySet() ) {
				Map<Integer, Movie> map = tc.getMovieMap().get(key).getElementMap();
				for ( int intKey : map.keySet() ) {
					a.add("{" + map.get(intKey).getKOR() + " / " + map.get(intKey).getAddress() + "}");
				}
			}
			Collections.sort(a);
			System.out.println(a);
			System.out.println(a.size());
			
			a = new ArrayList<String>();
			for ( String key : tc.getWatchingTVAMap().keySet() ) {
				TVA tva = tc.getWatchingTVAMap().get(key);
				a.add("{" + tva.getKOR() + " / " + tva.getAddress() + "}");
			}
			Collections.sort(a);
			System.out.println(a);
			System.out.println(a.size());
			System.out.println("====================================================================================");
		}
			
	}
}
