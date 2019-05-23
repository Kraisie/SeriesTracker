package com.Kraisie.TVDB;

class JSONErrors {

	private String[] invalidFilters;
	private String invalidLanguage;
	private String[] invalidQueryParams;

	JSONErrors(String[] invalidFilters, String invalidLanguage, String[] invalidQueryParams) {
		this.invalidFilters = invalidFilters;
		this.invalidLanguage = invalidLanguage;
		this.invalidQueryParams = invalidQueryParams;
	}

	String[] getInvalidFilters() {
		return invalidFilters;
	}


	String getInvalidLanguage() {
		return invalidLanguage;
	}

	String[] getInvalidQueryParams() {
		return invalidQueryParams;
	}
}
