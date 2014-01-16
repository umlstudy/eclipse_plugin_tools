package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

import java.io.Serializable;

public class RuntimeCommand implements Serializable {
	
	private static final long serialVersionUID = -4269393543868145197L;
	
	private boolean use;
	private String label;
	private String runtimeCommand;
	private boolean movePath;
	
	private RuntimeCommand(boolean use, String label, String runtimeCommand, boolean movePath) {
		this.setLabel(label);
		this.setRuntimeCommand(runtimeCommand);
		this.setUse(use);
		this.setMovePath(movePath);
	}
	
	public static RuntimeCommand newInstance(boolean use, String label, String runtimeCommand, boolean movePath) {
		return new RuntimeCommand(use, label, runtimeCommand, movePath);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRuntimeCommand() {
		return runtimeCommand;
	}

	public void setRuntimeCommand(String runtimeCommand) {
		this.runtimeCommand = runtimeCommand;
	}

	public boolean isMovePath() {
		return movePath;
	}

	public void setMovePath(boolean movePath) {
		this.movePath = movePath;
	}

	public String getCmdString(String path) {
		if ( path == null || !movePath ) {
			return String.format("start %s", runtimeCommand); //$NON-NLS-1$
		} else {
			return String.format("cd %s && start %s", path, runtimeCommand); //$NON-NLS-1$
		}
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}
}
