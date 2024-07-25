package br.com.apr.domain.exceptions;

public class FornecedorNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public FornecedorNotFoundException() {
		super("Fornecedor informado não existe no sistema.");
	}
	
}
