package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RuntimeCommandList implements Serializable {

	private static final long serialVersionUID = 6369957067980365921L;

	transient private List<IRuntimeCommandListViewer> runtimeCommandListViewers;
	private List<RuntimeCommand> items;
	
	private RuntimeCommandList() {
		items = new ArrayList<RuntimeCommand>();
		init();
	}

	public void init() {
		runtimeCommandListViewers = new ArrayList<IRuntimeCommandListViewer>();
	}
	
	public static RuntimeCommandList newInstance() {
		return new RuntimeCommandList();
	}

	public void addChangeListener(IRuntimeCommandListViewer runtimeCommandListViewer) {
		runtimeCommandListViewers.add(runtimeCommandListViewer);
	}

	public void removeChangeListener(IRuntimeCommandListViewer runtimeCommandListViewer) {
		runtimeCommandListViewers.remove(runtimeCommandListViewer);
	}

	public List<RuntimeCommand> getRuntimeCommands() {
		return items;
	}

	public void add(RuntimeCommand runtimeCommand) {
		items.add(runtimeCommand);
	}

	public void remove(RuntimeCommand runtimeCommand) {
		items.remove(runtimeCommand);
	}

	public List<RuntimeCommand> getItems() {
		return items;
	}
}
