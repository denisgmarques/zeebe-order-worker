package br.com.denisgmarques.zeebe;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableZeebeClient
@SpringBootApplication
//@ZeebeDeployment(resources = { "classpath*:/bpmn/**/*.bpmn", "classpath*:/bpmn/**/*.dmn" })
@ZeebeDeployment(resources = { "classpath:/bpmn/categoria_pedido.dmn", "classpath:/bpmn/order-split.bpmn" })
public class Configuration {

	public static void main(String[] args) {
		SpringApplication.run(Configuration.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}

}
