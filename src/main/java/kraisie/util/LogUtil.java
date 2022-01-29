package kraisie.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

	public static void logInfo(String message) {
		LOGGER.info(message);
	}

	public static void logWarning(String message) {
		LOGGER.warn(message);
	}

	public static void logDebug(String message) {
		LOGGER.debug(message);
	}

	public static void logError(String message) {
		LOGGER.error(message);
	}

	public static void logError(String message, Throwable t) {
		LOGGER.error(message, t);
	}

	public static void logDebug(String message, Throwable t) {
		LOGGER.debug(message, t);
	}
}
