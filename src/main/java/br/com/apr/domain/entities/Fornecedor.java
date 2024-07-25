package br.com.apr.domain.entities;

import java.util.UUID;

import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Fornecedor {
	
	@Id
	@Column
	private UUID id;
	
	@Column
	@Indexed(unique = true)
	private String nome;
	

}
