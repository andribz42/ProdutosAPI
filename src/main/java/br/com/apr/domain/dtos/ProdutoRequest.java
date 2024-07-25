package br.com.apr.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProdutoRequest {

	@NotEmpty(message = "O preenchimento do nome é obrigatório.")
	@Size(min = 8, max = 100, message = "O nome deve ter de 8 a 100 caracteres.")
	private String nome;
	
	@NotEmpty(message = "O preenchimento do preco é obrigatório.")
	private double preco;
	
	@NotEmpty(message = "O preenchimento da quantidade é obrigatório.")
	private int quantidade;
	
	@NotEmpty(message = "O preenchimento do id do fornecedor é obrigatório.")
	private UUID fornecedorId;
}
