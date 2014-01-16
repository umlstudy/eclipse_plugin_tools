package asia.sejong.eclipse.plugin.tools.test;

public class RuntimeCommand {
	private boolean selected;
	private String label;
	private String runtimeCommand;
	
	private RuntimeCommand(String label, String runtimeCommand) {
		this.setLabel(label);
		this.setRuntimeCommand(runtimeCommand);
	}
	
	public static RuntimeCommand newInstance(String label, String runtimeCommand) {
		return new RuntimeCommand(label, runtimeCommand);
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
