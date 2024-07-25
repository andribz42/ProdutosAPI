package br.com.apr.infrastucture.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.apr.domain.entities.MessageLogger;

@Repository
public interface MessageLoggerRepository extends MongoRepository<MessageLogger, UUID> {

}
