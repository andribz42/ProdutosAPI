package br.com.apr.domain.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import br.com.apr.domain.dtos.FornecedorRequest;
import br.com.apr.domain.dtos.FornecedorResponse;

public interface FornecedorServiceImpl {

	public FornecedorResponse create(FornecedorRequest request) throws Exception;
	public FornecedorResponse update(UUID id, FornecedorRequest request) throws Exception;
	public FornecedorResponse delete(UUID id) throws Exception;
	public FornecedorResponse getById(UUID id) throws Exception;
	public List<FornecedorResponse> getAll(Pageable pageable) throws Exception;
}
