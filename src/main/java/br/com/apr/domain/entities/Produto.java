package br.com.apr.domain.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Produto {
	
	@Id
	@Column
	private UUID id;
	
	@Column
	private String nome;
	
	@Column
	private double preco;
	
	@Column
	private int quantidade;
	
	@ManyToOne
	@JoinColumn(name = "fornecedor_id", nullable = false)
	private Fornecedor fornecedor;

}
