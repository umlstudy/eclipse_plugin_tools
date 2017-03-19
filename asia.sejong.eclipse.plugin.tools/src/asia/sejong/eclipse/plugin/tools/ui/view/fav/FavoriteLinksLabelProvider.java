package asia.sejong.eclipse.plugin.tools.ui.view.fav;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class FavoriteLinksLabelProvider extends WorkbenchLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if ( element instanceof FavoriteLinkItem ) {
			FavoriteLinkItem item = (FavoriteLinkItem)element;
			if ( columnIndex == 0 ) {
				return item.getPath();
			}
		}
		return "ERR";
	}
}
