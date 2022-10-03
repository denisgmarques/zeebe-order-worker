package br.com.denisgmarques.zeebe.worker;

import br.com.denisgmarques.zeebe.dto.OrderDTO;
import br.com.denisgmarques.zeebe.dto.SplittedOrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderProcessWorker {
    @Autowired
    private ObjectMapper mapper;

    @ZeebeWorker(type = "orderProcess", fetchVariables = { "order" })
    public void handleOrder(final JobClient client, final ActivatedJob job) {
        try {
            // TODO Como fazer pra pegar só o valor da variável sem ter o nome da variável na propriedade do JSON?
            final OrderDTO order = job.getVariablesAsType(SplittedOrderDTO.class).getOrder();
            log.info(mapper.writeValueAsString(order));
        } catch (Exception ex) {
            throw new ZeebeBpmnError("Error on order process worker", ex.getMessage());
        }
    }
}