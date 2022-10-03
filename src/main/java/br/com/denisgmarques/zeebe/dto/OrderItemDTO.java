package br.com.denisgmarques.zeebe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItemDTO {
    @JsonProperty("Nome")
    private String name;

    @JsonProperty("Categoria")
    private String category;

    @JsonProperty("Preco")
    private Double price;
}
