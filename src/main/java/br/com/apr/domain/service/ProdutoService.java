package br.com.apr.domain.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.apr.domain.dtos.ProdutoResponse;
import br.com.apr.domain.dtos.ProdutoRequest;
import br.com.apr.domain.entities.Fornecedor;
import br.com.apr.domain.entities.Produto;
import br.com.apr.domain.exceptions.FornecedorNotFoundException;
import br.com.apr.domain.exceptions.ProdutoNotFoundException;
import br.com.apr.domain.interfaces.ProdutoServiceImpl;
import br.com.apr.infrastucture.components.RabbitMQProducerComponent;
import br.com.apr.infrastucture.repositories.FornecedorRepository;
import br.com.apr.infrastucture.repositories.ProdutoRepository;

@Service
public class ProdutoService implements ProdutoServiceImpl {

	@Autowired ProdutoRepository repository;
	@Autowired FornecedorRepository fornecedorRepository;
	@Autowired ModelMapper modelMapper;
	@Autowired RabbitMQProducerComponent rabbitMQProducerComponent;
	
	@Override
	public ProdutoResponse create(ProdutoRequest request) throws Exception {
		Fornecedor fornecedor = fornecedorRepository.findById(request.getFornecedorId())
				.orElseThrow(() -> new FornecedorNotFoundException());
		Produto produto = modelMapper.map(request, Produto.class);
		produto.setId(UUID.randomUUID());
		produto.setFornecedor(fornecedor);
		repository.save(produto);
		rabbitMQProducerComponent.sendMessage(produto);
		ProdutoResponse response = modelMapper.map(produto, ProdutoResponse.class);
		response.setFornecedorId(produto.getFornecedor().getId());
		return response;
	}

	@Override
	public ProdutoResponse update(UUID id, ProdutoRequest request) throws Exception {
		
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ProdutoNotFoundException());
		
		Fornecedor fornecedor = fornecedorRepository.findById(request.getFornecedorId())
				.orElseThrow(() -> new FornecedorNotFoundException());

		produto.setNome(request.getNome());
		produto.setPreco(request.getPreco());
		produto.setQuantidade(request.getQuantidade());
		produto.setFornecedor(fornecedor);
		repository.save(produto);
		
		ProdutoResponse response = modelMapper.map(produto, ProdutoResponse.class);
		response.setFornecedorId(produto.getFornecedor().getId());
		return response;
	}

	@Override
	public ProdutoResponse delete(UUID id) throws Exception {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ProdutoNotFoundException());
		ProdutoResponse response = modelMapper.map(produto, ProdutoResponse.class);
		response.setFornecedorId(produto.getFornecedor().getId());
		repository.delete(produto);
		return response;
	}

	@Override
	public ProdutoResponse getById(UUID id) throws Exception {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ProdutoNotFoundException());
		ProdutoResponse response = modelMapper.map(produto, ProdutoResponse.class);
		response.setFornecedorId(produto.getFornecedor().getId());
		return response;
	}

	@Override
	public List<ProdutoResponse> getAll(Pageable pageable) {
		Page<Produto> produtos = repository.findAll(pageable);
		List<ProdutoResponse> response = produtos.stream()
				.map(produto -> {
					ProdutoResponse produtoResponse = modelMapper.map(produto, ProdutoResponse.class);
					produtoResponse.setFornecedorId(produto.getFornecedor().getId());
					return produtoResponse;
				})
				.collect(Collectors.toList());
		return response;
	}

}
