package com.ega.SpringWS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;

@ServletComponentScan
//Ця анотація говорить Спрингу, що це основний клас, який запускає наш Веб-додаток
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SpringWsApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SpringWsApplication.class, args);
            SpringApplication application = new SpringApplication(SpringWsApplication.class);
            application.setAdditionalProfiles("ssl");
            application.run(args); 
        
	}

}

