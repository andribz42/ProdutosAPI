package br.com.apr.domain.entities;

import java.time.temporal.Temporal;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class MessageLogger {

	@Id
	private UUID id;
	private String produto;
	private String status;
	private Temporal createdAt;
	private String message;
	
}
