package com.mocah.mindmath.server;

import java.util.Collections;

import javax.servlet.ServletContext;

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
import springfox.documentation.spring.web.paths.RelativePathProvider;
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
//		TestLearningProcess.main(args);
	}

	// Documentation of task API v1.0
	@Bean
	public Docket swaggerTaskApi1_0(ServletContext servletContext) {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("task-api-1.1")
				.protocols(Collections.singleton("https")).host(hostbase).pathProvider(new RelativePathProvider(servletContext) {
		              @Override
		              public String getApplicationBasePath() {
		                  return null;
		              }
		          }).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.controller.cabri"))
				.paths(PathSelectors.ant("/task/**")).build()
				.apiInfo(new ApiInfoBuilder().version("1.1").title("Task API from Cabri")
						.description("POST JSON of Cabri from Tralalere and Return Feedback Documentation v1.1")
						.contact(amel).build());
	}
	
	// Documentation of task API v1.0
		@Bean
		public Docket swaggerLearningApi1_0(ServletContext servletContext) {
			return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("learning-api-1.1")
					.protocols(Collections.singleton("https")).host(hostbase).pathProvider(new RelativePathProvider(servletContext) {
			              @Override
			              public String getApplicationBasePath() {
			                  return null;
			              }
			          }).select()
					.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.controller.learning"))
					.paths(PathSelectors.ant("/learning/**")).build()
					.apiInfo(new ApiInfoBuilder().version("1.1").title("Learning API from Cabri")
							.description("Backup values in learning Documentation v1.1")
							.contact(amel).build());
		}

	// Documentation of Ontology API v1.0
	@Bean
	public Docket swaggerOntologyApi1_0(ServletContext servletContext) {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("ontology-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).pathProvider(new RelativePathProvider(servletContext) {
		              @Override
		              public String getApplicationBasePath() {
		                  return null;
		              }
		          }).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.controller.config"))
				.paths(PathSelectors.regex("/ontology/v1.0.*")).build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Ontology API")
						.description("Update Ontology OWL file Documentation v1.0").contact(amel).build());
	}

	// Documentation of LRS Learning Locker API
	@Bean
	public Docket swaggerLRSApi1_0(ServletContext servletContext) {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("lrs-api-1.1")
				.protocols(Collections.singleton("https")).host(hostbase).pathProvider(new RelativePathProvider(servletContext) {
		              @Override
		              public String getApplicationBasePath() {
		                  return null;
		              }
		          }).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.controller.config"))
				.paths(PathSelectors.ant("/lrs/**"))
//				.paths(Predicates.not(PathSelectors.ant("/lrs/test/**")))
				.build().apiInfo(new ApiInfoBuilder().version("1.1").title("LRS API")
						.description("Connect with Learning Locker LRS Documentation").contact(amel).build());
	}
	
	// Documentation of feedback for Derby API
	@Bean
	public Docket swaggerFBApi1_0(ServletContext servletContext) {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("fb-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).pathProvider(new RelativePathProvider(servletContext) {
		              @Override
		              public String getApplicationBasePath() {
		                  return null;
		              }
		          }).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.controller.feedback")).paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder().version("1.0").title("Feedback API")
						.description("Feedback content in Derby Documentation").contact(amel).build());
	}

	// Documentation of file management API
	@Bean
	public Docket swaggerFileApi1_0(ServletContext servletContext) {
		return new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2).groupName("file-api-1.0")
				.protocols(Collections.singleton("https")).host(hostbase).pathProvider(new RelativePathProvider(servletContext) {
		              @Override
		              public String getApplicationBasePath() {
		                  return null;
		              }
		          }).select()
				.apis(RequestHandlerSelectors.basePackage("com.mocah.mindmath.server.controller.config"))
				.paths(PathSelectors.ant("/file/**"))
				.build().apiInfo(new ApiInfoBuilder().version("1.0").title("File API")
						.description("Local file read/write Documentation").contact(amel).build());
	}
}
