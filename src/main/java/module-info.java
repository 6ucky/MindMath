module mindmath {
	exports com.MindMath.Server.cabri;
	exports com.MindMath.Server;

	exports com.MindMath.learning;

	requires com.fasterxml.jackson.annotation;
	requires java.persistence;
	requires spring.beans;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.context;
	requires spring.data.commons;
	requires spring.web;
	requires com.fasterxml.jackson.databind;
	requires java.desktop;

	opens com.MindMath.Server;
}
