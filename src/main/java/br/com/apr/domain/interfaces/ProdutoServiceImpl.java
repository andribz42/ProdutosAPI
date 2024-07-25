package br.com.apr.domain.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import br.com.apr.domain.dtos.ProdutoRequest;
import br.com.apr.domain.dtos.ProdutoResponse;

public interface ProdutoServiceImpl {

	public ProdutoResponse create(ProdutoRequest request) throws Exception;
	public ProdutoResponse update(UUID id, ProdutoRequest request) throws Exception;
	public ProdutoResponse delete(UUID id) throws Exception;
	public ProdutoResponse getById(UUID id) throws Exception;
	public List<ProdutoResponse> getAll(Pageable pageable) throws Exception;
	
}
