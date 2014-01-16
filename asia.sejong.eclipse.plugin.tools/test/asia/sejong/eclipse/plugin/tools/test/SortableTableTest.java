package asia.sejong.eclipse.plugin.tools.test;

import java.util.Iterator;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SortableTableTest {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		createTableViewer(shell);

		
		shell.setSize(400, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createTableViewer(Composite parent) {

		parent.setLayout(GridLayoutFactory.fillDefaults().create());
		
		Composite row = creatGridRowComposite(parent, 1);
		row.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		
		int tableStyle = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.FULL_SELECTION ;
		
		final RuntimeCommandContentProvider cp = new RuntimeCommandContentProvider();
		final RuntimeCommandTableViewer tableViewer = new RuntimeCommandTableViewer(row, tableStyle, cp);
		
		row = creatGridRowComposite(parent, 2);
		Button addBtn = new Button(row, SWT.NONE);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cp.addRuntimeCommand(RuntimeCommand.newInstance("AA", "BBB"));
			}
		});
		addBtn.setText("ADD");
		Button removeBtn = new Button(row, SWT.NONE);
		removeBtn.setText("REMOVE");
		removeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( tableViewer.getSelection() instanceof IStructuredSelection ) {
					IStructuredSelection ss = (IStructuredSelection)tableViewer.getSelection();
					Iterator<?> iter = ss.iterator();
					while ( iter.hasNext() ) {
						cp.removeRuntimeCommand((RuntimeCommand)iter.next());
					}
				}
			}
		});
	}

	private static Composite creatGridRowComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().numColumns(numColumns).create());
		return composite;
	}
}
