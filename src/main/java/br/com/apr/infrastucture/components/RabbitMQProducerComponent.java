package br.com.apr.infrastucture.components;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.apr.domain.entities.Produto;

@Component
public class RabbitMQProducerComponent {
	
	@Autowired RabbitTemplate rabbitTemplate;
	@Autowired ObjectMapper objectMapper;
	@Autowired Queue queue;
	
	public void sendMessage(Produto produto) throws Exception {
		String json = objectMapper.writeValueAsString(produto);
		rabbitTemplate.convertAndSend(queue.getName(), json);
	}
}

