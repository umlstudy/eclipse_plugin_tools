package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class RuntimeCommandContentProvider implements IStructuredContentProvider, IRuntimeCommandListViewer {

	private RuntimeCommandList runtimeCommandList;
	private RuntimeCommandTableViewer tableViewer;
	
	public void setRuntimeCommandTableViewer(RuntimeCommandTableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if (newInput != null) {
			runtimeCommandList = (RuntimeCommandList) newInput;
			runtimeCommandList.addChangeListener(this);
		}
		if (oldInput != null) {
			((RuntimeCommandList) oldInput).removeChangeListener(this);
		}
	}

	public void dispose() {
		if ( runtimeCommandList != null ) {
			runtimeCommandList.removeChangeListener(this);
		}
	}

	public Object[] getElements(Object parent) {
		return runtimeCommandList.getRuntimeCommands().toArray();
	}

	@Override
	public void addRuntimeCommand(RuntimeCommand runtimeCommand) {
		runtimeCommandList.add(runtimeCommand);
		tableViewer.add(runtimeCommand);
	}

	@Override
	public void removeRuntimeCommand(RuntimeCommand runtimeCommand) {
		runtimeCommandList.remove(runtimeCommand);
		tableViewer.remove(runtimeCommand);
	}

	@Override
	public void updateRuntimeCommand(RuntimeCommand runtimeCommand) {
		tableViewer.update(runtimeCommand, null);
	}
}