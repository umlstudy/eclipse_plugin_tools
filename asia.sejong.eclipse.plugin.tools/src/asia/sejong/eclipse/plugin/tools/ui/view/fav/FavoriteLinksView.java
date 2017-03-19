package asia.sejong.eclipse.plugin.tools.ui.view.fav;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import asia.sejong.eclipse.plugin.tools.Activator;

public class FavoriteLinksView extends ViewPart {
	
	private Action action = new Action() {
		
		public void run() {
			List<FavoriteLinkItem> files = getSelectedFiles();
			if ( files.size()>0 ) {
				for ( FavoriteLinkItem file : files ) {
					Activator.removeFavoriteLink(file);
				}
			}
		}
		
		public boolean isEnabled() {
			List<FavoriteLinkItem> files = getSelectedFiles();
			if ( files.size()>0 ) {
				return true;
			} else {
				return false;
			}
		}
	};
	
	public static final String ID = FavoriteLinksView.class.getName();
	
	private FilteredTree filteredTree;

	public FavoriteLinksView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		PatternFilter filter = new PatternFilter();

		int tableStyle = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.FULL_SELECTION ;
		filteredTree = new FilteredTree(parent, tableStyle, filter, true);
		TreeViewer treeViewer = filteredTree.getViewer();
		treeViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		treeViewer.setContentProvider(new FavoriteLinksContentProvider());
		treeViewer.setLabelProvider(new FavoriteLinksLabelProvider());
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection ss = (IStructuredSelection)event.getSelection();
				FavoriteLinkItem item = (FavoriteLinkItem)ss.getFirstElement();
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(item.getPath()));
				try {
					IDE.openEditor(getSite().getPage(), file);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		createTreeDetails(treeViewer.getTree());
		
		
		List<FavoriteLinkItem> list = Activator.getDefault().getFavoriteLinkItems();
		filteredTree.getViewer().setInput(list);
		
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(filteredTree.getViewer().getControl());
		filteredTree.getViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, filteredTree.getViewer());
		
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				action.setText("삭제");
//				action.setActionDefinitionId("item.delete");
				action.setId("item.delete");
				action.setEnabled(true);
				manager.add(action);
			}
		});
		menuManager.setRemoveAllWhenShown(true);
	}
	
	private List<FavoriteLinkItem> getSelectedFiles() {
		IStructuredSelection selection = (IStructuredSelection)filteredTree.getViewer().getSelection();
		Iterator<?> iterator = selection.iterator();
		List<FavoriteLinkItem> files = new ArrayList<FavoriteLinkItem>();
		while ( iterator.hasNext() ) {
			Object next = iterator.next();
			if ( next instanceof FavoriteLinkItem ) {
				files.add((FavoriteLinkItem)next);
			}
		}
		return files;
	}
	
	private void createTreeDetails(Tree tree) {
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createTreeColumns(tree);
	}

	private void createTreeColumns(Tree tree) {
		// 1stColumn
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setWidth(700);
		column.setText("경로");
	}

	@Override
	public void setFocus() {
	}

	public void add(FavoriteLinkItem favoriteLinkItem) {
//		filteredTree.getViewer().refresh();
		
		Object parent = filteredTree.getViewer().getInput();
		System.out.println("parent ? " + parent);
		Widget[] items= filteredTree.getViewer().testFindItems(favoriteLinkItem);
		for (int i= 0; i < items.length; i++) {
			Widget item= items[i];
			if (item instanceof TreeItem && !item.isDisposed()) {
				TreeItem parentItem= ((TreeItem) item).getParentItem();
				System.out.println("parentItem ? " + parentItem);
				if (parentItem != null && !parentItem.isDisposed() && parent.equals(parentItem.getData())) {
					System.out.println("parent.equals(parentItem.getData()) ? " + parent.equals(parentItem.getData()));
					return; // no add, element already added (most likely by a refresh)
				}
			}
		}
		System.out.println("add ? ing...");
		filteredTree.getViewer().add(filteredTree.getViewer().getInput(), favoriteLinkItem);
	}
	
	public void remove(FavoriteLinkItem favoriteLinkItem) {
		filteredTree.getViewer().remove(favoriteLinkItem);
	}
}
