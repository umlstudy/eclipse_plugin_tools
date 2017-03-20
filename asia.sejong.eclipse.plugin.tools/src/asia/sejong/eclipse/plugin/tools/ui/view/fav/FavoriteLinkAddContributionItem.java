package asia.sejong.eclipse.plugin.tools.ui.view.fav;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import asia.sejong.eclipse.plugin.tools.Activator;
import asia.sejong.eclipse.plugin.tools.util.SelectionUtil;

public class FavoriteLinkAddContributionItem extends ContributionItem {

	public void fill(Menu menu, int index) {
		
		final MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Add favorite lin");
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)SelectionUtil.getSelection();
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(FavoriteLinksView.ID);
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
				
				if ( selection!= null ) {
					Iterator<?> iterator = selection.iterator();
					if ( iterator != null ) {
						while ( iterator.hasNext() ) {
							Object ele = iterator.next();
							if ( ele instanceof PlatformObject ) {
								PlatformObject po = (PlatformObject)ele;
								Object target = po.getAdapter(IResource.class);
								if ( target instanceof IFile ) {
									Activator.addFavoriteLink((IFile)target);
								}
							}
						}
					}
				}
			}
		});
	}


	public boolean isVisible() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isDynamic() {
		return true;
	}
}
