package Data.TVDB;

public class JSONErrors {

    private String[] invalidFilters;
    private String invalidLanguage;
    private String[] invalidQueryParams;

    public JSONErrors(String[] invalidFilters, String invalidLanguage, String[] invalidQueryParams) {
        this.invalidFilters = invalidFilters;
        this.invalidLanguage = invalidLanguage;
        this.invalidQueryParams = invalidQueryParams;
    }

    public String[] getInvalidFilters() {
        return invalidFilters;
    }

    public void setInvalidFilters(String[] invalidFilters) {
        this.invalidFilters = invalidFilters;
    }

    public String getInvalidLanguage() {
        return invalidLanguage;
    }

    public void setInvalidLanguage(String invalidLanguage) {
        this.invalidLanguage = invalidLanguage;
    }

    public String[] getInvalidQueryParams() {
        return invalidQueryParams;
    }

    public void setInvalidQueryParams(String[] invalidQueryParams) {
        this.invalidQueryParams = invalidQueryParams;
    }
}
