package asia.sejong.eclipse.plugin.tools.ui.preference;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "asia.sejong.eclipse.plugin.tools.ui.preference.messages"; //$NON-NLS-1$
	public static String ConveniencePreferencePage_ADD;
	public static String ConveniencePreferencePage_REMOVE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
