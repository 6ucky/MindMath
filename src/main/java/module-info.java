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
	requires org.hibernate.orm.core;
	requires org.apache.httpcomponents.httpcore;
	requires org.apache.tomcat.embed.core;
	requires spring.webmvc;
	requires tuprolog;
	requires org.apache.httpcomponents.httpclient;
	requires com.google.gson;

	opens com.mocah.mindmath.server;
	opens com.mocah.mindmath.server.cabri;
}
