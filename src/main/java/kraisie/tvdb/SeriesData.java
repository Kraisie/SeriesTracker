package kraisie.tvdb;

class SeriesData {

	private Series data;
	private JSONErrors errors;

	SeriesData(Series data, JSONErrors errors) {
		this.data = data;
		this.errors = errors;
	}

	Series getData() {
		return data;
	}

	JSONErrors getErrors() {
		return errors;
	}
}
