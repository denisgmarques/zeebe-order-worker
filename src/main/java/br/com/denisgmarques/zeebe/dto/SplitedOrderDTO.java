package br.com.denisgmarques.zeebe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SplitedOrderDTO {
    @JsonProperty("order")
    private OrderDTO order;
}
