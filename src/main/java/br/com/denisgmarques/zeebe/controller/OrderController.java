package br.com.denisgmarques.zeebe.controller;

import br.com.denisgmarques.zeebe.dto.OrderDTO;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderController extends BaseRestController {
	@Autowired
	private ZeebeClient client;

	@PostMapping(value = "/order")
	public @ResponseBody OrderDTO order(@RequestBody OrderDTO pedido) {
		createProcess(pedido);
		return pedido;
	}

	private void createProcess(OrderDTO pedido) {
		final ProcessInstanceEvent event = client.newCreateInstanceCommand()
				.bpmnProcessId("order-split")
				.latestVersion()
				.variables(pedido)
				.send()
				.join();

		log.info("Started instance for processDefinitionKey='{}', bpmnProcessId='{}', version='{}' with processInstanceKey='{}'",
				event.getProcessDefinitionKey(), event.getBpmnProcessId(), event.getVersion(),
				event.getProcessInstanceKey());
	}
}

