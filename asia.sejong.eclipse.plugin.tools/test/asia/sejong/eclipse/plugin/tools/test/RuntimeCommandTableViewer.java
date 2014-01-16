package asia.sejong.eclipse.plugin.tools.test;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class RuntimeCommandTableViewer extends TableViewer {

	private static String [] COLUMN_NAMES = new String[] {"!","이름","쉘명령","테스트"};
	
	private RuntimeCommandContentProvider runtimeCommandContentProvider;

	private TableEditor tableEditor;
	
	public RuntimeCommandTableViewer(Composite parent, int style, RuntimeCommandContentProvider runtimeCommandContentProvider) {
		super(parent, style);
		setTableViwerDetails();
		setTableDetails(getTable());
		
		this.runtimeCommandContentProvider = runtimeCommandContentProvider;
		this.runtimeCommandContentProvider.setRuntimeCommandTableViewer(this);
		setContentProvider(runtimeCommandContentProvider);
		setLabelProvider(new RuntimeCommandLabelProvider());
		
		tableEditor = new TableEditor(getTable());
		tableEditor.setColumn(3);
	    final Button button = new Button(getTable(), SWT.NONE);
	    button.setVisible(true);
	    button.setText("Test");
	    button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RuntimeCommand cmd = (RuntimeCommand)button.getData();
				if ( cmd != null ) {
					testButtonClicked(cmd);
				}
			}
		});
	    tableEditor.grabHorizontal = true;
	    getTable().addListener(SWT.MouseHover, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if ( event.widget instanceof Table ) {
					Table table = (Table)event.widget;
					TableItem item = table.getItem(new Point(event.x, event.y));
					if ( item != null ) {
						tableEditor.setEditor(button);
						tableEditor.setItem(item);
						button.setData(item.getData());
					} else {
						tableEditor.setEditor(null);
					}
				}
			}
		});
	}
	
	private void testButtonClicked(RuntimeCommand cmd) {
		System.out.println(cmd);
		
	}
	
	protected static int getColumnIndex(TableColumn[] columns, int px) {
		int columnIndex = -1;
		int widthFrom = 0;
		for ( int i=0; i<columns.length; i++ ) {
			if ( px>=(widthFrom+5) && px < (widthFrom + columns[i].getWidth() - 10) ) {
				columnIndex = i;
				break;
			}
			widthFrom += columns[i].getWidth();
		}
		return columnIndex;
	}

	public RuntimeCommandContentProvider getRuntimeCommandContentProvider() {
		return runtimeCommandContentProvider;
	}
	
	public List<String> getColumnNames() {
		return Arrays.asList(COLUMN_NAMES);
	}
	
	private void setTableViwerDetails() {

		setUseHashlookup(true);
		setColumnProperties(COLUMN_NAMES);

		CellEditor[] editors = createCellEditors();
		setCellEditors(editors);
		setCellModifier(new RuntimeCommandCellModifier(this));
		setSorter(new RuntimeCommandTableSorter(RuntimeCommandTableSorter.LABEL));
	}
	
	private CellEditor[] createCellEditors() {
		CellEditor[] editors = new CellEditor[COLUMN_NAMES.length];

		Table table = getTable();
		editors[0] = new CheckboxCellEditor(table);

		// Column 2 : 이름
		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(60);
		editors[1] = textEditor;

		// Column 3 : 쉘명령
		textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(60);
        ((Text) textEditor.getControl()).addVerifyListener(
        		new VerifyListener() {
        			public void verifyText(VerifyEvent e) {
        				e.doit = !e.text.isEmpty();  
        			}
        		}
		);
        editors[2] = textEditor;
        
        return editors;
	}
	
	private void setTableDetails(Table table) {
		table.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createTableColumns(table);
	}

	private static void createTableColumns(Table table) {
		
		// Column
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(23);
		column.setText("!");
		
		// Column
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(80);
		column.setText("액션명");
		
		// Column
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(150);
		column.setText("런타임커맨드");
		
		// Column
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(50);
		column.setText("Test");
	}
}
