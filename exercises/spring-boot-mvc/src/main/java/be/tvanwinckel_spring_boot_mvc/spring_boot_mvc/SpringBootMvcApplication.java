package be.tvanwinckel_spring_boot_mvc.spring_boot_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMvcApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/spring-web-mvc-system-prop");
		SpringApplication.run(SpringBootMvcApplication.class, args);
	}

}
