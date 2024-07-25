package br.com.apr.domain.exceptions;

public class ProdutoNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProdutoNotFoundException() {
		super("Produto informado não existe no sistema.");
	}
}
