package animelog4.gui.event;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import animelog4.collection.RemovedImageList;
import animelog4.collection.TypeCollection;
import animelog4.gui.view.PlannedToWatch;
import animelog4.gui.view.Search;
import animelog4.gui.view.WatchingTVA;
import animelog4.io.Save;

public class MenuListener implements ActionListener {
	private static int selection = 0;
	private Component component;
	
	public MenuListener(Component component) {
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		final String rbtns[] = { "정보", "검색", "시청 중인 TVA", "시청 예정 목록", "파일 저장", "종료" };
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
		
		// information
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
			
		// search
		case 1:
			String s = JOptionPane.showInputDialog(component, "검색어를 입력하세요.", "검색", JOptionPane.QUESTION_MESSAGE);
			try {
				while ( s.trim().isEmpty() )
					s = JOptionPane.showInputDialog(component, "검색할 내용이 없습니다. 검색어를 다시 입력하세요.", "검색", JOptionPane.QUESTION_MESSAGE);
			}
			catch(NullPointerException e1) {
				return;
			}
			Search search = new Search(s.trim());
			search.setDialog();
			search.show();
			break;
			
		// watching
		case 2:
			new WatchingTVA();
			break;
			
		// plan to watch
		case 3:
			new PlannedToWatch();
			break;
			
		// save as file
		case 4:
			Save save = new Save();
			sb = new StringBuilder("");
			if ( !save.tva() ) sb.append("에러 발생: TVA 항목 저장\n");
			if ( !save.movie() ) sb.append("에러 발생: 극장판 항목 저장\n");
			if ( !save.watchingTVA() ) sb.append("에러 발생: 시청 중인 항목 저장\n");
			if ( !save.userInfo() ) sb.append("에러 발생: 사용자 설정 저장\n");
			if ( !save.planned() ) sb.append("에러 발생: 시청 예정 항목 저장\n");
			
			if ( !sb.toString().isEmpty() )
				JOptionPane.showMessageDialog(component, sb.toString(), "에러 발생", JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(component, "저장 완료", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
			break;
			
		// exit
		case 5:
			RemovedImageList ril = RemovedImageList.getInstance();
			for (int i=0; i<ril.size(); i++)
				if ( ril.get(i).getType() == RemovedImageList.NEW )
					new File("image/" + ril.get(i).getKey()).delete();
			System.exit(0);
			break;
		}
			
	}
}
