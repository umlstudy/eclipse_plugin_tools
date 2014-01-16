package asia.sejong.eclipse.plugin.tools.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// from http://forums.codeguru.com/showthread.php?486177-How-to-run-a-very-long-SQL-statement
// Thanks
public class CSVParser {

	/*
	 * This Pattern will match on either quoted text or text between space,
	 * including whitespace, and accounting for beginning and end of line.
	 */
	// private final Pattern csvPattern =
	// Pattern.compile("\"([^\"]*)\"|(?<=,|^)([^,]*)(?:,|$)");
	private final Pattern csvPattern = Pattern
			.compile("\"([^\"]*)\"|(?<= |^)([^ ]*)(?: |$)"); //$NON-NLS-1$
	private ArrayList<String> allMatches = null;
	private Matcher matcher = null;

	// private String match = null;

	private CSVParser() {
		allMatches = new ArrayList<String>();
		matcher = null;
		// match = null;
	}

	public static String[] parse(String csvLine) {
		return new CSVParser().parseExecute(csvLine);
	}

	public String[] parseExecute(String csvLine) {
		matcher = csvPattern.matcher(csvLine);
		allMatches.clear();
		String match;
		while (matcher.find()) {
			match = matcher.group(1);
			if (match != null) {
				allMatches.add(match);
			} else {
				allMatches.add(matcher.group(2));
			}
		}
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < allMatches.size(); i++) {
			if (!allMatches.get(i).equals("")) {  //$NON-NLS-1$
				results.add(allMatches.get(i).trim());
			}
		}
		if (results.size() > 0) {
			return results.toArray(new String[results.size()]);
		} else {
			return new String[0];
		}
	}
}
