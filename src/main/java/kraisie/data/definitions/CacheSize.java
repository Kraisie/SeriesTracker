package kraisie.data.definitions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CacheSize {

	UNLIMITED {
		@Override
		public long getSizeInBytes() {
			return 0;
		}

		@Override
		public String toString() {
			return "Unlimited";
		}
	},
	MB_50 {
		@Override
		public long getSizeInBytes() {
			return 50_000_000;
		}

		@Override
		public String toString() {
			return "50 MB";
		}
	},
	MB_250 {
		@Override
		public long getSizeInBytes() {
			return 250_000_000;
		}

		@Override
		public String toString() {
			return "250 MB";
		}
	},
	MB_500 {
		@Override
		public long getSizeInBytes() {
			return 500_000_000;
		}

		@Override
		public String toString() {
			return "500 MB";
		}
	},
	GB_1 {
		@Override
		public long getSizeInBytes() {
			return 1_000_000_000;
		}

		@Override
		public String toString() {
			return "1 GB";
		}
	},
	GB_10 {
		@Override
		public long getSizeInBytes() {
			return 10_000_000_000L;
		}

		@Override
		public String toString() {
			return "10 GB";
		}
	};

	public abstract long getSizeInBytes();

	public abstract String toString();

	public static CacheSize get(String abbreviation) {
		for (CacheSize cs : values()) {
			if (cs.toString().equalsIgnoreCase(abbreviation)) {
				return cs;
			}
		}

		throw new IllegalArgumentException("Received invalid cache size abbreviation: '" + abbreviation + "'!");
	}

	public static List<String> toStringList() {
		return Arrays.stream(values())
				.map(CacheSize::toString)
				.collect(Collectors.toList());
	}
}
