package com.stockmarket.configurations;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackages = "com.stockmarket.*")
@PropertySources(value = {@PropertySource(value = "classpath:properties/datasource.properties"),@PropertySource(value="classpath:properties/hibernate.properties")})
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private Environment environment;
	
	@Value(value = "${hibernate.hbm2ddl.auto}")
	private String hibernatehbm2ddl;
	
	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	
	@Value("${hibernate.search.default.directory_provider}")
	private String hibernateDirectoryProvider;
	
	@Value("${hibernate.search.default.indexBase}")
	private String hibernateIndexBase;
	
	@Value(value="${hibernate.show_sql}")
	private String hibernateShowSql;
	
	@Value(value="${hibernate.c3p0.min_size}")
	private String hibernate_c3p0_minSize;
	
	@Value(value="${hibernate.c3p0.max_size}")
	private String hibernate_c3p0_maxSize;
	
	@Value(value="${hibernate.c3p0.acquire_increment}")
	private String hibernate_c3p0_acquireIncrement;
	
	@Value(value="${hibernate.c3p0.timeout}")
	private String hibernate_c3p0_timeout;
	
	@Value(value = "${hibernate.c3p0.max_statements}")
	private String hibernate_c3p0_maxStatements;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
	
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName(environment.getProperty("datasource.driver"));
		datasource.setUsername(environment.getProperty("datasource.user"));
		datasource.setPassword(environment.getProperty("datasource.password"));
		datasource.setUrl(environment.getProperty("datasource.url"));
		return datasource;
	}
	
	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hibernatehbm2ddl);
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.search.default.directory_provider", hibernateDirectoryProvider);
		properties.setProperty("hibernate.search.default.indexBase", hibernateIndexBase);
		properties.setProperty("hibernate.show_sql", hibernateShowSql);
		properties.setProperty("hibernate.c3p0.min_size", hibernate_c3p0_minSize);
		properties.setProperty("hibernate.c3p0.max_size", hibernate_c3p0_maxSize);
		properties.setProperty("hibernate.c3p0.acquire_increment", hibernate_c3p0_acquireIncrement);
		properties.setProperty("hibernate.c3p0.timeout", hibernate_c3p0_timeout);
		properties.setProperty("hibernate.c3p0.max_statements", hibernate_c3p0_maxStatements);
		return properties;
	}
	
	@Bean("sessionFactory")
	public LocalSessionFactoryBean localSessionFactoryBean() {
		LocalSessionFactoryBean entityBean = new LocalSessionFactoryBean();
		entityBean.setDataSource(getDataSource());
		entityBean.setPackagesToScan("com.stockmarket.entities");
		entityBean.setHibernateProperties(hibernateProperties());
		return entityBean;
	}
	
	@Bean("transactionManager")
	public HibernateTransactionManager transactionManage() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory((SessionFactory) localSessionFactoryBean().getObject());
		//transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean().getObject());
		return transactionManager;
	}
	
	
}
