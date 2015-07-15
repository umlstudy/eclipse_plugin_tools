package asia.sejong.eclipse.plugin.tools.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

public class SelectionUtil {

	public static String getSelectionCanonicalPath(ISelection selection) {
		if ( selection instanceof IStructuredSelection ) {
			IStructuredSelection ss = (IStructuredSelection)selection;
			Object firstElement = ss.getFirstElement();
			IContainer container = null;
			if ( firstElement instanceof PlatformObject ) {
				PlatformObject po = (PlatformObject)firstElement;
				IResource resource = (IResource)po.getAdapter(IResource.class);
				if ( resource instanceof IFile ) {
					container = ((IFile)resource).getParent();
				} else if ( resource instanceof IContainer ) {
					container = (IContainer)resource;
				}
			}
			
			if ( container != null ) {
				try {
					return container.getLocation().toString();
				} catch (Exception e) {
					throw ExceptionUtil.getRuntimeException(e);
				}
			}
		}
		return null;
	}

	public static ISelection getSelection() {
		ISourceProviderService service = (ISourceProviderService)PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		Object selection = service.getSourceProvider("showInSelection").getCurrentState().get("selection"); //$NON-NLS-1$//$NON-NLS-2$
		if ( selection instanceof ISelection ) {
			return (ISelection)selection;
		} else {
			return null;
		}
	}
}
