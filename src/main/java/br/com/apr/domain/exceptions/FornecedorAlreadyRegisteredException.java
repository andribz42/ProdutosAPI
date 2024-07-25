package br.com.apr.domain.exceptions;

public class FornecedorAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = 1L;

	public FornecedorAlreadyRegisteredException() {
		super("Fornecedor já cadastrado no sistema.");
	}
	
}
