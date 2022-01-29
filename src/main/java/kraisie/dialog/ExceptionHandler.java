package kraisie.dialog;

import kraisie.util.LogUtil;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		LogUtil.logError("Caught uncaught exception!", e);
	}
}
