package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import asia.sejong.eclipse.plugin.tools.ui.images.ToolsImageRegistry;

public class RuntimeCommandLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if ( columnIndex == 0 ) {
			String key = ((RuntimeCommand) element).isUse() ? ToolsImageRegistry.CHECKED_IMAGE : ToolsImageRegistry.UNCHECKED_IMAGE;
			return  ToolsImageRegistry.INSTANCE.get(key);
		} else if ( columnIndex == 3 ) {
			String key = ((RuntimeCommand) element).isMovePath() ? ToolsImageRegistry.CHECKED_IMAGE : ToolsImageRegistry.UNCHECKED_IMAGE;
			return  ToolsImageRegistry.INSTANCE.get(key);
		}
		return null; 
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		RuntimeCommand rc = (RuntimeCommand)element;
		String result = ""; //$NON-NLS-1$
		switch ( columnIndex ) {
		case 1 :
			result = rc.getLabel();
			break;
		case 2 :
			result = rc.getRuntimeCommand();
			break;
		}
		return result;
	}

}
