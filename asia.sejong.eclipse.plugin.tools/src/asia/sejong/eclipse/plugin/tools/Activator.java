package asia.sejong.eclipse.plugin.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import asia.sejong.eclipse.plugin.tools.ui.view.fav.FavoriteLinkItem;
import asia.sejong.eclipse.plugin.tools.ui.view.fav.FavoriteLinksView;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "asia.sejong.eclipse.plugin.tools"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		// save
		Map<String, FavoriteLinkItem> favoriteLinkItemsMap = Activator.getDefault().favoriteLinkItemsMap;
		
		List<String> lines = new ArrayList<String>();
		for ( FavoriteLinkItem item : favoriteLinkItemsMap.values() ) {
			lines.add(String.format("%d,%s", item.getSortOrder(), item.getPath()));
		}
		File file = getFile();
		FileUtils.writeLines(file, lines);
		 
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static void log(Throwable cause) {
		log(cause.getMessage(), cause);
	}
	
	public static void log(String message, Throwable cause) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, 0, message, cause);
		log(status);
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	public static void showError(Exception e ) {
		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openInformation(shell, "error occured", e.getMessage()); //$NON-NLS-1$
		} catch ( Exception ex ) {}
	}

	public static void asyncRun(Runnable runnable) {
		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			shell.getDisplay().asyncExec(runnable);
		} catch ( Exception ex ) {}
	}

	private Map<String, FavoriteLinkItem> favoriteLinkItemsMap = null;

	public static void addFavoriteLink(IFile ele) {
		Map<String, FavoriteLinkItem> map = Activator.getDefault().favoriteLinkItemsMap;
		FavoriteLinkItem item = new FavoriteLinkItem(ele);
		boolean added = false;
		if ( !map.containsKey(item.getPath()) ) {
			map.put(item.getPath(), item);
			added = true;
		}
		System.out.println("added ? " + added);
		
		if ( added ) {
			for ( IWorkbenchWindow ww : PlatformUI.getWorkbench().getWorkbenchWindows() ) {
				for ( IWorkbenchPage wp : ww.getPages() ) {
					FavoriteLinksView findView = (FavoriteLinksView)wp.findView(FavoriteLinksView.ID);
					System.out.println("findView ? " + findView);
					if ( findView != null ) {
						findView.add(item);
					}
				}
			}
			
		}
	}
	
	public static void removeFavoriteLink(FavoriteLinkItem item) {
		Map<String, FavoriteLinkItem> map = Activator.getDefault().favoriteLinkItemsMap;
		//FavoriteLinkItem item = new FavoriteLinkItem(ele);
		boolean removed = false;
		if ( map.containsKey(item.getPath()) ) {
			item = map.remove(item.getPath());
			removed = true;
		}
		
		if ( removed ) {
			for ( IWorkbenchWindow ww : PlatformUI.getWorkbench().getWorkbenchWindows() ) {
				for ( IWorkbenchPage wp : ww.getPages() ) {
					FavoriteLinksView findView = (FavoriteLinksView)wp.findView(FavoriteLinksView.ID);
					if ( findView != null ) {
						findView.remove(item);
					}
				}
			}
		}
	}

	public synchronized List<FavoriteLinkItem> getFavoriteLinkItems() {
		if ( favoriteLinkItemsMap == null ) {
			try {
				favoriteLinkItemsMap = loadFavoriteLinkItemsMap();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if ( favoriteLinkItemsMap == null ) {
			favoriteLinkItemsMap = new HashMap<String, FavoriteLinkItem>();
		}
		
		ArrayList<FavoriteLinkItem> arrayList = new ArrayList<FavoriteLinkItem>();
		arrayList.addAll(favoriteLinkItemsMap.values());
		Collections.sort(arrayList, new Comparator<FavoriteLinkItem>() {

			@Override
			public int compare(FavoriteLinkItem o1, FavoriteLinkItem o2) {
				int rslt = o1.getSortOrder() - o2.getSortOrder();
				if ( rslt == 0 ) {
					rslt = o1.getPath().compareTo(o2.getPath());
				}
				return rslt;
			}
		});
		
		return arrayList;
	}

	public void setFavoriteLinkItemsMap(Map<String, FavoriteLinkItem> favoriteLinkItemsMap) {
		this.favoriteLinkItemsMap = favoriteLinkItemsMap;
	}
	
	private File getFile() {
		return new File(getPluginDir(Activator.PLUGIN_ID) + "favoriteLinks.ini");
	}
	private Map<String, FavoriteLinkItem> loadFavoriteLinkItemsMap() throws IOException {
		Map<String, FavoriteLinkItem> map = new HashMap<String, FavoriteLinkItem>();
		File file = getFile();
		if ( file.exists() ) {
			List<String> lines = FileUtils.readLines(file, "UTF-8");
			for ( String line : lines ) {
				String[] strings = StringUtils.split(line,',');
				if ( strings.length != 2) {
					continue;
				}
				FavoriteLinkItem favoriteLinkItem = new FavoriteLinkItem(strings[0], strings[1]);
				map.put(favoriteLinkItem.getPath(), favoriteLinkItem);
			}
		}
		
		return map;
		
	}
	
	public static String getPluginDir(String pluginId) {
		Bundle bundle = Platform.getBundle(pluginId);
		if( bundle == null )
			throw new RuntimeException("Could not resolve plugin: " + pluginId + "\r\n" +
					"Probably the plugin has not been correctly installed.\r\n" +
					"Running eclipse from shell with -clean option may rectify installation.");
		
		/* resolve Bundle::getEntry to local URL */
		URL pluginURL = null;
		try {
			pluginURL = Platform.resolve(bundle.getEntry("/"));
		} catch (IOException e) {
			throw new RuntimeException("Could not get installation directory of the plugin: " + pluginId);
		}
		String pluginInstallDir = pluginURL.getPath().trim();
		if( pluginInstallDir.length() == 0 )
			throw new RuntimeException("Could not get installation directory of the plugin: " + pluginId);
		
		/* since path returned by URL::getPath starts with a forward slash, that
		 * is not suitable to run commandlines on Windows-OS, but for Unix-based
		 * OSes it is needed. So strip one character for windows. There seems
		 * to be no other clean way of doing this. */
		if( Platform.getOS().compareTo(Platform.OS_WIN32) == 0 )
			pluginInstallDir = pluginInstallDir.substring(1);
		
		return pluginInstallDir;
	}
}
