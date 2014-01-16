package asia.sejong.eclipse.plugin.tools.ui.view.sfffopv;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SfffopvContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if ( !(inputElement instanceof IFile) ) {
			return new Object[0];
		}
		
		IFile file = (IFile)inputElement;
		IWorkspaceRoot root = file.getWorkspace().getRoot();
		String path = file.getFullPath().toString();
		String pathWithoutProjectName = path.replace("/" + file.getProject().getName(), ""); //$NON-NLS-1$//$NON-NLS-2$
		
		List<IFile> files = new ArrayList<IFile>();
		for ( IProject project : root.getProjects() ) {
			IFile foundFiles = project.getFile(pathWithoutProjectName);
			if ( foundFiles.exists() ) {
				files.add(project.getFile(pathWithoutProjectName));
			}
		}
		
		return files.toArray();
	}

	@Override
	public void dispose() {
		// nothing
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// nothing
	}
}
