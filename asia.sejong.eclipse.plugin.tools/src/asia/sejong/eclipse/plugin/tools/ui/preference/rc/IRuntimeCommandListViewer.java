package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

public interface IRuntimeCommandListViewer {
	
	void addRuntimeCommand(RuntimeCommand runtimeCommand);
	void removeRuntimeCommand(RuntimeCommand runtimeCommand);
	void updateRuntimeCommand(RuntimeCommand runtimeCommand);
}
