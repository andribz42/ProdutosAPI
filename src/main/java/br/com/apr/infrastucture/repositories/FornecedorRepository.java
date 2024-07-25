package br.com.apr.infrastucture.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.apr.domain.entities.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {

	Fornecedor findByNome(String nome); 
}
