package asia.sejong.eclipse.plugin.tools.ui.view.SameFilesView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;

import asia.sejong.eclipse.plugin.tools.ui.view.sfffopv.Messages;

public class SameFilesView extends ViewPart {

	private static final boolean D = true;

	private FilteredTree tree ;
	
	private ActionGroup actionGroup;
	
	private IDoubleClickListener doubleClickListener = new IDoubleClickListener() {
		
		@Override
		public void doubleClick(DoubleClickEvent event) {
			doubleClicked(getIFile(null, event.getSelection()));
		}
	};

//	private ISelectionListener selectionListener = new ISelectionListener() {
//		@Override
//		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//			if ( D ) System.out.println(part.toString() + selection.toString());
//			selectionChangedReceived(getIFile(part, selection));
//		}
//	};
	
	@Override
	public void createPartControl(Composite parent) {
		
		// 1. CREATE UI
		GridLayoutFactory gridLayoutFactory = GridLayoutFactory.fillDefaults();
		
		parent.setLayout(gridLayoutFactory.create());
		
		// 1.1. search field
		int treeStyle = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.FULL_SELECTION ;
		tree = new FilteredTree(parent, treeStyle, new PatternFilter() {
		}, true);
		tree.getViewer().setContentProvider(new WorkbenchContentProvider());
		tree.getViewer().setLabelProvider(new WorkbenchLabelProvider());
		tree.getViewer().addDoubleClickListener(doubleClickListener);
		createTreeDetails(tree.getViewer().getTree());
		
		// 2. 액션 그룹
		actionGroup = new ActionGroup() {};
		
		// 3. 이벤트
		if ( D ) System.err.println("ADD HOOK");
		addHook();
		if ( D ) System.err.println("CREATE PARTCONTROL END");
		
		tree.getViewer().setInput(ResourcesPlugin.getWorkspace());
	}

	protected static IFile getIFile(IWorkbenchPart part, ISelection selection) {
		if ( selection instanceof IStructuredSelection ) {
			IStructuredSelection ss = (IStructuredSelection)selection;
			Object firstElement = ss.getFirstElement();
			if ( D ) System.out.println(firstElement);
			if ( firstElement instanceof PlatformObject ) {
				PlatformObject po = (PlatformObject)firstElement;
				IResource resource = (IResource)po.getAdapter(IResource.class);
				if ( resource instanceof IFile )
				return (IFile)resource;
			}
		} else {
			if ( D ) System.out.println(part.toString() + selection.toString());
			if ( part instanceof IEditorPart ) {
				IEditorPart epart = (IEditorPart)part;
				if ( D ) System.out.println(epart);
				if ( epart.getEditorInput() instanceof FileEditorInput ) {
					FileEditorInput fei = (FileEditorInput)epart.getEditorInput();
					return fei.getFile();
				}
 			}
		}
		
		return null;
	}

	private void addHook() {
		if ( D ) System.err.println("initContextMenu()");
		initContextMenu();
//		if ( D ) System.err.println("addSelectionListener");
//		getSite().getPage().addSelectionListener(selectionListener);
	}
	
    public void dispose() {
//    	getSite().getPage().removeSelectionListener(selectionListener);
    }

	private void initContextMenu() {
		if ( D ) System.err.println("createMenu");
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		if ( D ) System.err.println("registerMenu");
		Menu menu = menuMgr.createContextMenu(tree.getViewer().getTree());
		tree.getViewer().getTree().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tree.getViewer());
	}

	protected void fillContextMenu(IMenuManager manager) {
		IStructuredSelection selection = (IStructuredSelection)tree.getViewer().getSelection();
		getActionGroup().setContext(new ActionContext(selection));
		getActionGroup().fillContextMenu(manager);
	}

	private ActionGroup getActionGroup() {
		return actionGroup;
	}
	
//	protected void selectionChangedReceived(IFile file) {
//		if ( file != null ) {
//			tree.getViewer().setInput(file);
//			for ( TreeItem item : tree.getViewer().getTree().getItems() ) {
//				if ( file.equals(item.getData()) ) {
//					item.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
//				} else {
//					item.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
//				}
//			}
//		}
//	}
	
	protected void doubleClicked(IFile iFile) {
		if ( iFile != null ) {
			boolean activate = OpenStrategy.activateOnOpen();
			try {
				IDE.openEditor(getSite().getPage(), iFile, activate);
			} catch (PartInitException e) {
				MessageDialog.openError(getSite().getShell(), "error", e.getMessage()); //$NON-NLS-1$
			}
		}
	}

	private void createTreeDetails(Tree tree) {
		tree.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createTreeColumns(tree);
	}

	private void createTreeColumns(Tree tree) {
		// 1stColumn
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setWidth(200);
		column.setText(Messages.SameFolderFileFinderOnProjectsView_NAME);
		
		column = new TreeColumn(tree, SWT.NONE);
		column.setWidth(400);
		column.setText(Messages.SameFolderFileFinderOnProjectsView_PATH);
	}

	@Override
	public void setFocus() {
		
	}
}
