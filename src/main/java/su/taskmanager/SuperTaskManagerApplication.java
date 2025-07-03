package su.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SuperTaskManagerApplication {
	public static void main(String[] args) {
		var context = SpringApplication.run(SuperTaskManagerApplication.class, args);
		System.out.println("Beans in context:");
		for (String beanName : context.getBeanDefinitionNames()) {
			System.out.println(beanName);
		}
	}
}


