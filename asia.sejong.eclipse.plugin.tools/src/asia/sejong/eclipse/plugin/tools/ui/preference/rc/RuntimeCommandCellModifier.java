package asia.sejong.eclipse.plugin.tools.ui.preference.rc;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class RuntimeCommandCellModifier implements ICellModifier {

	private RuntimeCommandTableViewer tableViewer;
	
	public RuntimeCommandCellModifier(RuntimeCommandTableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		int columnIndex = tableViewer.getColumnNames().indexOf(property);
		if ( columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 3) {
			return true;
		}
		return false;
	}

	@Override
	public Object getValue(Object element, String property) {

		int columnIndex = tableViewer.getColumnNames().indexOf(property);
		Object result = null;
		RuntimeCommand cmd = (RuntimeCommand) element;

		switch (columnIndex) {
        case 0 :
            result = new Boolean(cmd.isUse());
            break;
        case 1 : 
            result = cmd.getLabel();
            break;
         case 2 : 
    	 	result = cmd.getRuntimeCommand();
    	 	break;
         case 3 :
             result = new Boolean(cmd.isMovePath());
             break;
        }
        return result;	
	}

	@Override
	public void modify(Object element, String property, Object value) {
		int columnIndex = tableViewer.getColumnNames().indexOf(property);
		TableItem ti = (TableItem)element;
		RuntimeCommand cmd = (RuntimeCommand) ti.getData();

		switch (columnIndex) {
        case 0 :
        	cmd.setUse(((Boolean)value));
            break;
        case 1 : 
        	cmd.setLabel(((String)value));
            break;
         case 2 : 
        	 cmd.setRuntimeCommand(((String)value));
    	 	break;
         case 3 :
         	cmd.setMovePath(((Boolean)value));
         	break;
        }
		
		tableViewer.update(cmd, null);
	}
}
