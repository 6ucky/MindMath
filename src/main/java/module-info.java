module com.mocah.mindmath {
	exports com.mocah.mindmath.server.cabri;
	exports com.mocah.mindmath.server;

	exports com.mocah.mindmath.learning;
	
	requires java.persistence;
	requires spring.beans;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.context;
	requires spring.data.commons;
	requires spring.web;
	requires java.desktop;

	//opens com.mocah.mindmath.server;
}
