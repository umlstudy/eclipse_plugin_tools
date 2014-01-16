package asia.sejong.eclipse.plugin.tools.test;

import java.util.ArrayList;
import java.util.List;

public class RuntimeCommandList {

	private List<IRuntimeCommandListViewer> runtimeCommandListViewers;
	private List<RuntimeCommand> items;
	
	private RuntimeCommandList() {
		items = new ArrayList<RuntimeCommand>();
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
}
