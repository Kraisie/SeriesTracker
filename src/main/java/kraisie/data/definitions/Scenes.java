package kraisie.data.definitions;

public enum Scenes {

	MOTHER {
		@Override
		public String getTitle() {
			return "SeriesTracker";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "MotherScene.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	MAIN {
		@Override
		public String getTitle() {
			return "Main Menu";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "MainSeries.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	ADD {
		@Override
		public String getTitle() {
			return "Add a new series";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "AddSeries.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	API_KEY {
		@Override
		public String getTitle() {
			return "API Key";
		}


		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "ApiKeyForm.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	FINISHED {
		@Override
		public String getTitle() {
			return "All finished series";
		}


		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "FinishedSeries.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	INFO {
		@Override
		public String getTitle() {
			return "Information";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "Information.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	SEARCH {
		@Override
		public String getTitle() {
			return "Search a series";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "SearchSeries.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	SELECT {
		@Override
		public String getTitle() {
			return "Select a series to add";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "SelectSeries.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	SETTINGS {
		@Override
		public String getTitle() {
			return "Settings";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "Settings.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	WAITING {
		@Override
		public String getTitle() {
			return "All awaited series";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "WaitingSeries.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	},
	HELP {
		@Override
		public String getTitle() {
			return "Help";
		}

		@Override
		public String getPath() {
			return PATH_FOLDER_PREFIX + "Help.fxml";
		}

		@Override
		public String getIcon() {
			return PATH_STANDARD_ICON;
		}
	};

	private static final String PATH_FOLDER_PREFIX = "/FXML/";
	private static final String PATH_STANDARD_ICON = "/Pics/Icon/series.png";

	public abstract String getTitle();

	public abstract String getPath();

	public abstract String getIcon();
}
