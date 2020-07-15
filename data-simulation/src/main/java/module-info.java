open module com.mocah.mindmath {

	exports com.mocah.mindmath.datasimulation;

	requires java.desktop;
	requires com.google.gson;
	requires transitive com.google.common;

	// test requirements
	requires org.junit.jupiter.api;
}
