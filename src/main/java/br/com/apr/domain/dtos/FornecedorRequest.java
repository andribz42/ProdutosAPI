package br.com.apr.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FornecedorRequest {

	@NotEmpty(message = "O preenchimento do nome é obrigatório.")
	@Size(min = 3, max = 100, message = "O nome deve ter de 3 a 50 caracteres.")
	private String nome;
	
}
