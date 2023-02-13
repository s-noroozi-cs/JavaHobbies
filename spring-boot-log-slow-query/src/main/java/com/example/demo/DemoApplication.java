package com.example.demo;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.CommonsLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



	@Bean
	DataSource dataSource(Environment env){

		HikariDataSource ds = new HikariDataSource();
		ds.setMaximumPoolSize(100);
		ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		ds.setJdbcUrl(env.getProperty("spring.datasource.url"));
		ds.setUsername(env.getProperty("spring.datasource.username"));
		ds.setPassword(env.getProperty("spring.datasource.password"));

		DataSource dataSource =
				ProxyDataSourceBuilder.create(ds)  // pass original datasource
						//.logQueryByCommons(CommonsLogLevel.INFO)    // logQueryBySlf4j(), logQueryByJUL(), logQueryToSysOut()
						//.countQuery()               // enable query count metrics
						.logSlowQueryByCommons(5, TimeUnit.MILLISECONDS)  // also by sl4j, jul, system out
						//.proxyResultSet()           // enable proxying ResultSet
						//.listener(myListener)       // register my custom listener
						.afterMethod(executionContext -> {    // register a custom listener with lambda
							//System.out.println("ok");
						})
						.build();

		return dataSource;
	}

}
