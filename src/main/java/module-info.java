module SeriesTracker {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.swing;
	requires gson;
	requires java.sql;    // for gson may change with release gson >2.8.5
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.commons.lang3;
	requires org.apache.httpcomponents.httpcore;

	opens com.Kraisie.SceneController to javafx.fxml;
	opens com.Kraisie.Data to gson, javafx.base;
	opens com.Kraisie.TVDB to gson;
	exports com.Kraisie;
}