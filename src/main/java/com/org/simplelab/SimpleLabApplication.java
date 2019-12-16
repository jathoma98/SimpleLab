package com.org.simplelab;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@EnableJpaRepositories(basePackages = {"com.org.simplelab.database.repositories.sql"})
@EnableMongoRepositories(basePackages = {"com.org.simplelab.database.repositories.mongodb"})
@SpringBootApplication
public class SimpleLabApplication {

	@Autowired
	private ThymeleafProperties properties;

	@Value("${spring.thymeleaf.templates_root:}")
	private String templatesRoot;



	public static void main(String[] args) {
		SpringApplication.run(SimpleLabApplication.class, args);
	}

	@Bean
	public ITemplateResolver defaultTemplateResolver() {
		FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setSuffix(properties.getSuffix());
		resolver.setPrefix(templatesRoot);
		resolver.setTemplateMode(properties.getMode());
		resolver.setCacheable(properties.isCache());
		return resolver;
	}

	@Bean
	public Module datatypeHibernateModule(){
		Hibernate5Module mod = new Hibernate5Module();
		mod.configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, true);
		//mod.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return mod;
	}

	/**
	//enables rollbacks for MongoDB
	@Bean
	MongoTransactionManager txManager(MongoDbFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}**/

}
