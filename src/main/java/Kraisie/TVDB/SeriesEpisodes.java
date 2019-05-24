package Kraisie.TVDB;

class SeriesEpisodes {

	private BasicEpisode[] data;
	private JSONErrors errors;
	private Links links;

	SeriesEpisodes(BasicEpisode[] data, JSONErrors errors, Links links) {
		this.data = data;
		this.errors = errors;
		this.links = links;
	}

	BasicEpisode[] getData() {
		return data;
	}

	JSONErrors getErrors() {
		return errors;
	}

	Links getLinks() {
		return links;
	}
}
