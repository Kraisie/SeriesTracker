package kraisie.util;

import java.util.Random;

public class RandomUtil {

	private static final Random random = new Random();

	public static int getIntInBound(int bound) {
		return random.nextInt(bound);
	}
}
