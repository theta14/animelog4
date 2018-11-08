package animelog4.gui.component;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ALTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	private DefaultTableModel dtm;
	
	public ALTable(Object rowData[][], Object columnNames[]) {
		super(rowData, columnNames);
		dtm = new DefaultTableModel();
		dtm.setDataVector(rowData, columnNames);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoCreateRowSorter(true);
		setShowHorizontalLines(false);
		setRowHeight(20);
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = getColumnModel();
		for (int i=0; i<getColumnCount(); i++) {
			int width = 50;	// min width
			for (int j=0; j<getRowCount(); j++) {
				TableCellRenderer tcr = getCellRenderer(j, i);
				Component c = prepareRenderer(tcr, j, i);
				width = Math.max(c.getPreferredSize().width + 1, width);
			}
			tcm.getColumn(i).setPreferredWidth(width);
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
		
		DefaultTableCellRenderer dtcrH = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
		dtcrH.setHorizontalAlignment(SwingConstants.CENTER);
		getTableHeader().setDefaultRenderer(dtcrH);
		
		registerKeyboardAction(null, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
	
	public ALTable(DefaultTableModel dtm) {
		super(dtm);
		this.dtm = dtm;
		
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoCreateRowSorter(true);
		setShowHorizontalLines(false);
		setRowHeight(20);
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = getColumnModel();
		for (int i=0; i<getColumnCount(); i++) {
			int width = 50;	// min width
			for (int j=0; j<getRowCount(); j++) {
				TableCellRenderer tcr = getCellRenderer(j, i);
				Component c = prepareRenderer(tcr, j, i);
				width = Math.max(c.getPreferredSize().width + 1, width);
			}
			tcm.getColumn(i).setPreferredWidth(width);
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
		
		DefaultTableCellRenderer dtcrH = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
		dtcrH.setHorizontalAlignment(SwingConstants.CENTER);
		getTableHeader().setDefaultRenderer(dtcrH);
		
		registerKeyboardAction(null, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
	
	public DefaultTableModel getDefaultTableModel() {
		return dtm;
	}
	
	public void sortAs(int index) {
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
		sorter.toggleSortOrder(index);
		setRowSorter(sorter);
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
}
