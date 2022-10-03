package br.com.denisgmarques.zeebe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SplittedOrderDTO {
    @JsonProperty("order")
    private OrderDTO order;
}
