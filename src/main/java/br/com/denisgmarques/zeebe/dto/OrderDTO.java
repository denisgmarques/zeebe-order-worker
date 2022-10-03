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


/**
 Original:
 {
 "Pedido":"1234",
 "CPF":"99999999999",
 "Cliente":"Joao da Silva",
 "Produtos":[
 {
 "Nome":"Celular",
 "Categoria":"Eletronico",
 "Preco":"99.99"
 },
 {
 "Nome":"Pilhas",
 "Categoria":"Eletronico",
 "Preco":"19.99"
 },
 {
 "Nome":"Vinho",
 "Categoria":"Bebidas",
 "Preco":"99.99"
 },
 {
 "Nome":"Cerveja",
 "Categoria":"Bebidas",
 "Preco":"99.99"
 },
 {
 "Nome":"Camiseta",
 "Categoria":"Roupas",
 "Preco":"99.99"
 },
 {
 "Nome":"Calças",
 "Categoria":"Roupas",
 "Preco":"99.99"
 }
 ]
 }
 */