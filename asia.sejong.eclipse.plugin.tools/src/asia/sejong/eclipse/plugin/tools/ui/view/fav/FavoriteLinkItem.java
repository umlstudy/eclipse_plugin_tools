package asia.sejong.eclipse.plugin.tools.ui.view.fav;

import org.eclipse.core.resources.IFile;

public class FavoriteLinkItem {

	private String path;
	private int sortOrder;

	public FavoriteLinkItem(String sortOrder, String path) {
		this.sortOrder = Integer.parseInt(sortOrder);
		this.path = path;
	}

	public FavoriteLinkItem(IFile ele) {
		this("0", ele.getFullPath().toString());
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public String toString() {
		return path;
	}
	
	public boolean equals(Object another) {
		if ( another instanceof FavoriteLinkItem ) {
			FavoriteLinkItem an= (FavoriteLinkItem)another;
			if ( an.getPath().equals(getPath()) ) {
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		return getPath().hashCode();
	}
}
