package asia.sejong.eclipse.plugin.tools.ui.images;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import asia.sejong.eclipse.plugin.tools.Activator;

public class ToolsImageRegistry extends ImageRegistry {

	public static final String CHECKED_IMAGE 	= "checked"; //$NON-NLS-1$
	public static final String UNCHECKED_IMAGE  = "unchecked"; //$NON-NLS-1$

	public static ToolsImageRegistry INSTANCE = new ToolsImageRegistry();
	
	private ToolsImageRegistry() {
		super();
		String iconPath = "icons/";  //$NON-NLS-1$
		put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
				Activator.class, 
				iconPath + CHECKED_IMAGE + ".gif" //$NON-NLS-1$
				)
			);
		put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
				Activator.class, 
				iconPath + UNCHECKED_IMAGE + ".gif" //$NON-NLS-1$
				)
			);
	}
}
