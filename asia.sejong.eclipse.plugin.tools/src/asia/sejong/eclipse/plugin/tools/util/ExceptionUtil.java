package asia.sejong.eclipse.plugin.tools.util;

public class ExceptionUtil {

	public static RuntimeException getRuntimeException(Exception e) {
		if ( e instanceof RuntimeException ) {
			return (RuntimeException)e;
		} else {
			return new RuntimeException(e);
		}
	}
}
