package asia.sejong.eclipse.plugin.tools.ui.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import asia.sejong.eclipse.plugin.tools.Activator;
import asia.sejong.eclipse.plugin.tools.common.util.CommandUtil;
import asia.sejong.eclipse.plugin.tools.ui.preference.ConveniencePreferencePage;
import asia.sejong.eclipse.plugin.tools.ui.preference.rc.RuntimeCommand;
import asia.sejong.eclipse.plugin.tools.ui.preference.rc.RuntimeCommandList;
import asia.sejong.eclipse.plugin.tools.util.SelectionUtil;

public class HelperActionsContributionItem extends ContributionItem {

	public void fill(Menu menu, int index) {
		System.out.println(menu);
		
		ISelection selection = SelectionUtil.getSelection();
		final String selectionCanonicalPath = SelectionUtil.getSelectionCanonicalPath(selection);
		
		RuntimeCommandList rcList = ConveniencePreferencePage.getRuntimeCommandList(null);
		try {
			for (final RuntimeCommand rc : rcList.getItems() ) {
				final MenuItem item = new MenuItem(menu, SWT.PUSH);
				// item.setImage(normal);
				// item.setHotImage(hover);
				item.setText(rc.getLabel());
				item.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						CommandUtil.runCommand(rc.getCmdString(selectionCanonicalPath));
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public boolean isVisible() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isDynamic() {
		return true;
	}

	public void cmd() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Runtime rt = Runtime.getRuntime();
					Process p = rt.exec(new String[]{"cmd.exe","/c","start"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					p.waitFor();
	
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String line = reader.readLine();
					while (line != null) {
						line = reader.readLine();
					}
					System.out.println(line);
				} catch ( Exception e ) {
					Activator.log(e);
				}
			}
		});
		thread.start();
	}
}
