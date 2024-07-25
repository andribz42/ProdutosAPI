package br.com.apr.infrastucture.components;

import java.time.Instant;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.apr.domain.entities.MessageLogger;
import br.com.apr.domain.entities.Produto;
import br.com.apr.infrastucture.repositories.MessageLoggerRepository;

@Component
public class RabbitMQConsumerComponent {

	@Value("${receiver.mail.username}")
	private String to;
	
	@Autowired MessageLoggerRepository messageLoggerRepository;
	@Autowired MailMessageComponent mailMessageComponent;
	@Autowired ObjectMapper objectMapper;
	
	@RabbitListener(queues = {"${queue.name}"})
	public void proccessMessage(@Payload String message) {
		
		MessageLogger messageLogger = new MessageLogger();
		messageLogger.setId(UUID.randomUUID());
		messageLogger.setCreatedAt(Instant.now());
		
		try {
			Produto produto = objectMapper.readValue(message, Produto.class);
			
			String subject = "O cadastro do produto '" + produto.getNome() + "' foi realizado com sucesso!";
			String body = "Olá, o cadastro do produto '" + produto.getNome() + "' foi realizado com sucesso!\n\nAt.te.\nEquipe\n\n" +
					objectMapper.writeValueAsString(produto);
			
			mailMessageComponent.send(to, subject, body);
			messageLogger.setProduto(objectMapper.writeValueAsString(produto));
			messageLogger.setStatus("SUCESSO");
			messageLogger.setMessage("Email de boas vindas enviado com sucesso para: " + to);
		} catch (Exception e) {
			messageLogger.setStatus("ERROR");
			messageLogger.setMessage(e.getMessage());
		} finally {
			messageLoggerRepository.save(messageLogger);
		}
	}
}
