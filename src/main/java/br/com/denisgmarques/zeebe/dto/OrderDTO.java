package br.com.denisgmarques.zeebe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO {
    @JsonProperty("Pedido")
    private String orderId;

    @JsonProperty("CPF")
    private String clientId;

    @JsonProperty("Cliente")
    private String clientName;

    @JsonProperty("Produtos")
    private List<OrderItemDTO> products = new ArrayList<>();
}
