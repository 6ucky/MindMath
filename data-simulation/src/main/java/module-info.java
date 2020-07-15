open module com.mocah.mindmath {

	exports com.mocah.mindmath.datasimulation;
	exports com.mocah.mindmath.datasimulation.attributes;
	exports com.mocah.mindmath.datasimulation.attributes.constraints.in;
	exports com.mocah.mindmath.datasimulation.attributes.constraints.between;

	requires java.desktop;
	requires com.google.gson;
	requires transitive com.google.common;

	// test requirements
	requires org.junit.jupiter.api;
}
