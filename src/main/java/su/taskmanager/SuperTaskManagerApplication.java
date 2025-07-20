package su.taskmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SuperTaskManagerApplication {

	private static final Logger logger = LoggerFactory.getLogger(SuperTaskManagerApplication.class);

	public static void main(String[] args) {
		var context = SpringApplication.run(SuperTaskManagerApplication.class, args);
	}
}