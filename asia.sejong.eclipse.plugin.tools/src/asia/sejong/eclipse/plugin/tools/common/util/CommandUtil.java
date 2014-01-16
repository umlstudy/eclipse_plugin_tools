package asia.sejong.eclipse.plugin.tools.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import asia.sejong.eclipse.plugin.tools.Activator;

public class CommandUtil {
	
	public static void main(String[] args) {
//		runCommand("cmd.exe /c \"cd c:/temp && C:/Progra~1/Git/bin/sh.exe --login -i\"");
//		runCommand("cmd.exe /c start C:/Progra~1/Git/bin/sh.exe --login -i");
		runCommand("cd c:/temp && start C:/Progra~1/Git/bin/sh.exe --login -i"); //$NON-NLS-1$
		runCommand("cd C:\\devel\\git\\git\\real\\042.DaeJoun\\DJJungGuNew\\DJJungGuNew\\Base && start cmd"); //$NON-NLS-1$
	}

	public static void runCommand(String cmd) {
		final String[] tokens = CSVParser.parse("cmd.exe /c \"" + cmd.trim() +"\""); //$NON-NLS-1$ //$NON-NLS-2$
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Runtime rt = Runtime.getRuntime();
					Process p = rt.exec(tokens); //new String[]{"cmd.exe","/c","start"});
					p.waitFor();
	
					StringBuilder sbLine = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = reader.readLine();
					while (line != null) {
						sbLine.append(line);
						line = reader.readLine();
					}
					if( !sbLine.toString().isEmpty() ) {
						System.out.println(sbLine);
					}
				} catch ( final Exception e ) {
					Activator.log(e);
					Activator.asyncRun(new Runnable() {
						@Override
						public void run() {
							Activator.showError(e);
						}
					});
				}
			}
		});
		thread.setName(cmd);
		thread.start();
	}
}
