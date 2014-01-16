package asia.sejong.eclipse.plugin.tools.ui.preference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import asia.sejong.eclipse.plugin.tools.Activator;
import asia.sejong.eclipse.plugin.tools.common.util.Base64;
import asia.sejong.eclipse.plugin.tools.ui.preference.rc.RuntimeCommand;
import asia.sejong.eclipse.plugin.tools.ui.preference.rc.RuntimeCommandContentProvider;
import asia.sejong.eclipse.plugin.tools.ui.preference.rc.RuntimeCommandList;
import asia.sejong.eclipse.plugin.tools.ui.preference.rc.RuntimeCommandTableViewer;

public class ConveniencePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private static final String RUNTIME_COMMAND_LIST = "RuntimeCommandList"; //$NON-NLS-1$
	
	private RuntimeCommandTableViewer runtimeCommandTableViewer;

	public ConveniencePreferencePage() {
		setPreferenceStore(getPersistentPreferenceStore());
	}
	
	@Override
	protected Control createContents(Composite parent) {
		runtimeCommandTableViewer = createTableViewer(parent);
		runtimeCommandTableViewer.setInput(getRuntimeCommandList(getPreferenceStore()));
		return runtimeCommandTableViewer.getControl();
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}
	
    private static Object fromString( String s ) throws IOException, ClassNotFoundException {
    	byte[] data = Base64.base64ToByteArray(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.byteArrayToBase64(baos.toByteArray());
    }
    
    @Override
	public boolean performOk() {
		try {
			IPersistentPreferenceStore ps = (IPersistentPreferenceStore)getPreferenceStore();
			save(ps, (RuntimeCommandList)runtimeCommandTableViewer.getInput());
		} catch ( IOException e ) {
			Activator.log(e);
			throw new RuntimeException(e);
		}
		
		return super.performOk();
	}
	
    private static void save(IPersistentPreferenceStore ps, RuntimeCommandList rcList) throws IOException {
		ps.setValue(RUNTIME_COMMAND_LIST, toString(rcList));
		ps.save();
	}

    @Override
	protected void performDefaults() {
		runtimeCommandTableViewer.setInput(createDefault());
    	super.performDefaults();
    }

	private static RuntimeCommandTableViewer createTableViewer(Composite parent) {

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
				cp.addRuntimeCommand(RuntimeCommand.newInstance(false, "noname", "cmd", false)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
		addBtn.setText(Messages.ConveniencePreferencePage_ADD); 
		Button removeBtn = new Button(row, SWT.NONE);
		removeBtn.setText(Messages.ConveniencePreferencePage_REMOVE);
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
		
		return tableViewer;
	}

	private static Composite creatGridRowComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().numColumns(numColumns).create());
		return composite;
	}

	public static RuntimeCommandList getRuntimeCommandList(IPreferenceStore preferences) {
		if ( preferences == null ) {
			preferences = getPersistentPreferenceStore();
		}
		String serializedRCList = preferences.getString(ConveniencePreferencePage.RUNTIME_COMMAND_LIST);
		RuntimeCommandList rcList = null;
		try {
			rcList = (RuntimeCommandList)fromString(serializedRCList);
			rcList.init();
		} catch (Exception e) {
			rcList = RuntimeCommandList.newInstance();
		}
		return rcList;
	}
	
	protected static IPersistentPreferenceStore getPersistentPreferenceStore() {
		ScopedPreferenceStore preferences = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, Activator.PLUGIN_ID);
		String serializedRCList = preferences.getString(ConveniencePreferencePage.RUNTIME_COMMAND_LIST);
		if ( serializedRCList == null ) {
			RuntimeCommandList rcl = createDefault();
			try {
				save(preferences, rcl);
			} catch ( IOException e ) {
				Activator.log(e);
				throw new RuntimeException(e);
			}
		}
		
		return preferences;
	}

	private static RuntimeCommandList createDefault() {
		RuntimeCommandList rcl = RuntimeCommandList.newInstance();
		rcl.add(RuntimeCommand.newInstance(true, "cmd", "cmd", true)); //$NON-NLS-1$ //$NON-NLS-2$
		rcl.add(RuntimeCommand.newInstance(true, "gitbash", "C:/Progra~1/Git/bin/sh.exe --login -i", true)); //$NON-NLS-1$ //$NON-NLS-2$
		rcl.add(RuntimeCommand.newInstance(true, "explorer", "explorer .", true)); //$NON-NLS-1$ //$NON-NLS-2$
		return rcl;
	}
}
