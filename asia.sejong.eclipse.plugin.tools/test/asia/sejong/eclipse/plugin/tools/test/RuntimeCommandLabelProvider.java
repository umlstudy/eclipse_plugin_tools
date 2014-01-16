package asia.sejong.eclipse.plugin.tools.test;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import asia.sejong.eclipse.plugin.tools.ui.images.ToolsImageRegistry;

public class RuntimeCommandLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if ( columnIndex != 0 ) return null; 
		String key = ((RuntimeCommand) element).isSelected() ? ToolsImageRegistry.CHECKED_IMAGE : ToolsImageRegistry.UNCHECKED_IMAGE;
		return  ToolsImageRegistry.INSTANCE.get(key);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		RuntimeCommand rc = (RuntimeCommand)element;
		String result = ""; //$NON-NLS-1$
		switch ( columnIndex ) {
		case 0 : 
			break;
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
