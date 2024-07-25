package br.com.apr.domain.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.apr.domain.dtos.FornecedorRequest;
import br.com.apr.domain.dtos.FornecedorResponse;
import br.com.apr.domain.entities.Fornecedor;
import br.com.apr.domain.exceptions.FornecedorAlreadyRegisteredException;
import br.com.apr.domain.exceptions.FornecedorNotFoundException;
import br.com.apr.domain.interfaces.FornecedorServiceImpl;
import br.com.apr.infrastucture.repositories.FornecedorRepository;

@Service
public class FornecedorService implements FornecedorServiceImpl {

	@Autowired FornecedorRepository repository;
	@Autowired ModelMapper modelMapper;
	
	@Override
	public FornecedorResponse create(FornecedorRequest request) throws Exception {
		
		if(repository.findByNome(request.getNome()) != null) {
			throw new FornecedorAlreadyRegisteredException();
		}
		
		Fornecedor fornecedor = modelMapper.map(request, Fornecedor.class);
		fornecedor.setId(UUID.randomUUID());
		repository.save(fornecedor);
		return modelMapper.map(fornecedor, FornecedorResponse.class);
	}

	@Override
	public FornecedorResponse update(UUID id, FornecedorRequest request) throws Exception {
		Fornecedor fornecedor = repository.findById(id).orElseThrow(() -> new FornecedorNotFoundException());
		fornecedor.setNome(request.getNome());
		repository.save(fornecedor);
		return modelMapper.map(fornecedor, FornecedorResponse.class);
	}

	@Override
	public FornecedorResponse delete(UUID id) throws Exception {
		Fornecedor fornecedor = repository.findById(id).orElseThrow(() -> new FornecedorNotFoundException());
		repository.delete(fornecedor);
		return modelMapper.map(fornecedor, FornecedorResponse.class);
	}

	@Override
	public FornecedorResponse getById(UUID id) throws Exception {
		Fornecedor fornecedor = repository.findById(id).orElseThrow(() -> new FornecedorNotFoundException());
		return modelMapper.map(fornecedor, FornecedorResponse.class);
	}

	@Override
	public List<FornecedorResponse> getAll(Pageable pageable) throws Exception {
		Page<Fornecedor> fornecedores = repository.findAll(pageable);
		List<FornecedorResponse> response = fornecedores.stream()
				.map(fornecedor -> modelMapper.map(fornecedor, FornecedorResponse.class))
				.collect(Collectors.toList());
		return response;
	}

}
