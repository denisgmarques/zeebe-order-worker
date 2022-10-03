package br.com.denisgmarques.zeebe.worker;

import br.com.denisgmarques.zeebe.dto.OrderDTO;
import br.com.denisgmarques.zeebe.dto.OrderItemDTO;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class OrderSplitWorker {
    private static final String BRINDES = "Lembrancinhas/Brindes";

    @ZeebeWorker(type = "orderSplit")
    public void handleOrder(final JobClient client, final ActivatedJob job) {
        try {
            final OrderDTO order = job.getVariablesAsType(OrderDTO.class);

            Map<String, List<OrderItemDTO>> itemsByCategory = new HashMap<>();
            order.getProducts().forEach(orderItemDTO -> {
                // TODO Buscar categoria do novo pedido através de uma DMN
                if (!orderItemDTO.getCategory().equals("Bebidas") && orderItemDTO.getPrice() < 20) {
                    itemsByCategory.put(BRINDES, insertItem(BRINDES, itemsByCategory, orderItemDTO));
                } else {
                    itemsByCategory.put(orderItemDTO.getCategory(), insertItem(orderItemDTO.getCategory(), itemsByCategory, orderItemDTO));
                }
            });

            client.newCompleteCommand(job.getKey()).variables(Map.of("newOrders", processOrders(order, itemsByCategory))).send();

            log.info(order.getOrderId() + " splitted!");
        } catch (Exception ex) {
            throw new ZeebeBpmnError("Error on order split worker", ex.getMessage());
        }
    }

    private List<OrderDTO> processOrders(OrderDTO originalOrder, Map<String, List<OrderItemDTO>> itemsByCategory) {
        List<OrderDTO> orders = new ArrayList<>();
        AtomicInteger ordinal = new AtomicInteger(0);

        itemsByCategory.forEach((category, items) -> {
            OrderDTO order = new OrderDTO();
            order.setOrderId(originalOrder.getOrderId() + "_" + ordinal.incrementAndGet());
            order.setClientId(originalOrder.getClientId());
            order.setClientName(originalOrder.getClientName());

            if (!category.equals(BRINDES)) {
              order.getProducts().addAll(items);
              orders.add(order);
            } else {
                double itemsSum = 0d;
                for (OrderItemDTO item : items) {
                    if (itemsSum + item.getPrice() > 100) {
                        orders.add(order);
                        itemsSum = 0d;

                        order = new OrderDTO();
                        order.setOrderId(originalOrder.getOrderId() + "_" + ordinal.incrementAndGet());
                        order.setClientId(originalOrder.getClientId());
                        order.setClientName(originalOrder.getClientName());
                    }
                    itemsSum += item.getPrice();
                    order.getProducts().add(item);
                }
                orders.add(order);
            }
        });

        return orders;
    }
    private List<OrderItemDTO> insertItem(String category, Map<String, List<OrderItemDTO>> itemsByCategory, OrderItemDTO item) {
        List<OrderItemDTO> items = new ArrayList<>();

        if (itemsByCategory.containsKey(category)) {
            items = itemsByCategory.get(category);
        }

        OrderItemDTO newItem = new OrderItemDTO();
        newItem.setCategory(category);
        newItem.setName(item.getName());
        newItem.setPrice(item.getPrice());

        items.add(newItem);

        return items;
    }
}