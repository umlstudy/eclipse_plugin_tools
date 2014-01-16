package asia.sejong.eclipse.plugin.tools.test;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class RuntimeCommandTableSorter extends ViewerSorter {

	public final static int LABEL 		= 1;
	public final static int RUNTIME_COMMAND	= 2;

	private int criteria;

	public RuntimeCommandTableSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	@SuppressWarnings("unchecked")
	public int compare(Viewer viewer, Object o1, Object o2) {

		RuntimeCommand rc1 = (RuntimeCommand) o1;
		RuntimeCommand rc2 = (RuntimeCommand) o2;

		switch (criteria) {
			case LABEL :
				return getComparator().compare(rc1.getLabel(), rc2.getLabel());
			case RUNTIME_COMMAND :
				return getComparator().compare(rc1.getRuntimeCommand(), rc2.getRuntimeCommand());
			default:
				return 0;
		}
	}

	public int getCriteria() {
		return criteria;
	}
}
