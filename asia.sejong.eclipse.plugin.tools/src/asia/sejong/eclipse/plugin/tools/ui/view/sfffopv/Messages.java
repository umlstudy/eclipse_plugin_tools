package asia.sejong.eclipse.plugin.tools.ui.view.sfffopv;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "asia.sejong.eclipse.plugin.tools.ui.view.sfffopv.messages"; //$NON-NLS-1$
	public static String SameFolderFileFinderOnProjectsView_NAME;
	public static String SameFolderFileFinderOnProjectsView_PATH;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
