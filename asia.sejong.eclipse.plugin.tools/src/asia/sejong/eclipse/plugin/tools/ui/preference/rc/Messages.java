package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "asia.sejong.eclipse.plugin.tools.ui.preference.rc.messages"; //$NON-NLS-1$
	public static String RuntimeCommandTableViewer_ACTION_NAME;
	public static String RuntimeCommandTableViewer_MOVE_PATH;
	public static String RuntimeCommandTableViewer_SHELL_COMMAND;
	public static String RuntimeCommandTableViewer_TEST1;
	public static String RuntimeCommandTableViewer_USE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
