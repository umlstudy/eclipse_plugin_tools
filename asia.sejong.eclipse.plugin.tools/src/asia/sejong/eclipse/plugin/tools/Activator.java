package asia.sejong.eclipse.plugin.tools;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "asia.sejong.eclipse.plugin.tools"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static void log(Throwable cause) {
		log(cause.getMessage(), cause);
	}
	
	public static void log(String message, Throwable cause) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, 0, message, cause);
		log(status);
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	public static void showError(Exception e ) {
		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openInformation(shell, "error occured", e.getMessage()); //$NON-NLS-1$
		} catch ( Exception ex ) {}
	}

	public static void asyncRun(Runnable runnable) {
		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			shell.getDisplay().asyncExec(runnable);
		} catch ( Exception ex ) {}
	}
}
