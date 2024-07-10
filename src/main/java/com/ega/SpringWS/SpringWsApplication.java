package com.ega.SpringWS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
//Ця анотація говорить Спрингу, що це основний клас, який запускає наш Веб-додаток
@SpringBootApplication
public class SpringWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWsApplication.class, args);
	}

}

