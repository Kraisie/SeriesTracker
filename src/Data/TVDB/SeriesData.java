package Data.TVDB;

class SeriesData {

    private Series data;
    private JSONErrors errors;

    public SeriesData(Series data, JSONErrors errors) {
        this.data = data;
        this.errors = errors;
    }

    public Series getData() {
        return data;
    }

    public void setData(Series data) {
        this.data = data;
    }

    public JSONErrors getErrors() {
        return errors;
    }

    public void setErrors(JSONErrors errors) {
        this.errors = errors;
    }
}
