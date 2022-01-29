package kraisie.util;

import java.util.Random;

public final class RandomUtil {

	private RandomUtil() {
		throw new AssertionError("Trying to instantiate a utility class.");
	}

	private static final Random random = new Random();

	public static int getIntInBound(int bound) {
		return random.nextInt(bound);
	}
}
