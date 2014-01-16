package asia.sejong.eclipse.plugin.tools.ui.view.sfffopv;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SfffopvLabelProvider extends WorkbenchLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if ( columnIndex == 0 ) {
			return getImage(element);
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if ( columnIndex == 0 ) {
			return getText(element);
		} else {
			if ( element instanceof IFile ) {
				return ((IFile)element).getFullPath().toString();
			}
		}
		return null;
	}
}
