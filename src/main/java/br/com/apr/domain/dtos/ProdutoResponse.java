package br.com.apr.domain.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class ProdutoResponse {

	private UUID id;
	private String nome;
	private double preco;
	private int quantidade;
	private UUID fornecedorId;
}
