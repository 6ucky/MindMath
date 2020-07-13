package com.mocah.mindmath.server;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.TestLearningProcess;
import com.mocah.mindmath.learning.algorithms.ILearning;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ServerApplication extends SpringBootServletInitializer {

	private Contact yan = new Contact("Yan Wang", "https://sites.google.com/view/yanwang/introduction",
			"yan.wang@lip6.fr");
	private Contact thibaut = new Contact("Thibaut SIMON-FINE", "https://github.com/ThibautSF", "tsimonfine@gmail.com");
	private Contact amel = new Contact("Amel Yessad", "https://www.lip6.fr/actualite/personnes-fiche.php?ident=P763",
			"amel.yessad@lip6.fr");
	private String hostbase = "mindmath.lip6.fr";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);

		// TODO remove line & class in prod
		TestLearningProcess.main(args);

		// TODO improve init system
		// ie Save learning object in DB and add initLearning with ILearning parameter
		// -> thus it can allow multiple learning instances (and of course backups !)
		// Note : each time a learn decision will be call, we will need to get the
		// correct instance from DB, pass it to makeDecision and in case it's a decision
		// with learning -> update DB (because the instance is modified : only qValues
		// attribute)
		ILearning learn1 = LearningProcess.initLearningProcess();
	}

	// Documentation of task API v1.0
	@Bean
	public Docket swaggerTaskApi1_0() {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("task-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.cabri"))
				.paths(PathSelectors.regex("/task/v1.0.*")).build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Task API from Cabri")
						.description("POST JSON of Cabri from Tralalere and Return Feedback Documentation v1.0")
						.contact(amel).build());
	}

	// Documentation of Matrix API v1.0
	@Bean
	public Docket swaggerMatrixApi1_0() {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("matrix-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.config"))
				.paths(PathSelectors.regex("/matrix/v1.0.*")).build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Matrix API")
						.description("Update Decision Tree Matrix Documentation v1.0").contact(amel).build());
	}

	// Documentation of Ontology API v1.0
	@Bean
	public Docket swaggerOntologyApi1_0() {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("ontology-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.config"))
				.paths(PathSelectors.regex("/ontology/v1.0.*")).build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Ontology API")
						.description("Update Ontology OWL file Documentation v1.0").contact(amel).build());
	}

	// Documentation of LRS Learning Locker API
	@Bean
	public Docket swaggerLRSApi1_0() {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("lrs-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.lrs")).paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder().version("1.0").title("LRS API")
						.description("Connect with Learning Locker LRS Documentation").contact(amel).build());
	}

}
