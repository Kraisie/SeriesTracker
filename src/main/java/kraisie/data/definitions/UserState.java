package kraisie.data.definitions;

public enum UserState {

	NOT_STARTED {
		@Override
		public int getId() {
			return 0;
		}

		@Override
		public String toString() {
			return "Not started";
		}
	},
	WATCHING {
		@Override
		public int getId() {
			return 1;
		}

		@Override
		public String toString() {
			return "Currently watching";
		}
	},
	WAITING {
		@Override
		public int getId() {
			return 2;
		}

		@Override
		public String toString() {
			return "Waiting for new episodes";
		}
	},
	FINISHED {
		@Override
		public int getId() {
			return 1;
		}

		@Override
		public String toString() {
			return "Finished";
		}
	};

	public abstract int getId();

	public abstract String toString();
}
