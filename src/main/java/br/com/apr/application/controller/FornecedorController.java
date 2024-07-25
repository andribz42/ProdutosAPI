package br.com.apr.application.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.apr.domain.dtos.FornecedorRequest;
import br.com.apr.domain.dtos.FornecedorResponse;
import br.com.apr.domain.interfaces.FornecedorServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

	@Autowired FornecedorServiceImpl fornecedorService;
	
	@PostMapping
	public ResponseEntity<FornecedorResponse> create(@RequestBody @Valid FornecedorRequest request) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.create(request));
	}

	@PutMapping("{id}")
	public ResponseEntity<FornecedorResponse> update(@PathVariable("id") UUID id, 
			@RequestBody @Valid FornecedorRequest request) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.update(id, request));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<FornecedorResponse> delete(@PathVariable("id") UUID id) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.delete(id));
	}

	@GetMapping("{id}")
	public ResponseEntity<FornecedorResponse> getById(@PathVariable("id") UUID id) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.getById(id));
	}

	@GetMapping
	public ResponseEntity<List<FornecedorResponse>> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy) throws Exception {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.getAll(pageable));
	}
}
