open module com.mocah.mindmath {

	exports com.mocah.mindmath.server;
	exports com.mocah.mindmath.server.cabri;
	exports com.mocah.mindmath.server.cabri.jsondata;

	exports com.mocah.mindmath.repository.jxapi;

	exports com.mocah.mindmath.decisiontree;
	exports com.mocah.mindmath.learning;
	exports com.mocah.mindmath.learning.algorithms;
	exports com.mocah.mindmath.learning.utils.actions;
	exports com.mocah.mindmath.learning.utils.states;
	exports com.mocah.mindmath.learning.utils.values;

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
	requires transitive com.google.gson;
	requires org.apache.jena.core;
	requires org.apache.commons.lang3;
	requires com.github.mustachejava;
	requires springfox.spring.web;
	requires spring.core;
	requires springfox.swagger2;
	requires springfox.assembly;
	requires bcprov.jdk16;
	requires spring.data.jpa;

	// test requirements
	requires spring.boot.test;
	requires org.junit.jupiter.api;
	requires org.assertj.core;
	requires spring.boot.test.autoconfigure;
	requires spring.test;
	requires org.hamcrest;
	requires org.mockito;
}
