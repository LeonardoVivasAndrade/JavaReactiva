package service;

import model.PersonEntity;
import reactor.core.publisher.Mono;

public interface IPersonService {
    Mono<PersonEntity> getById(Long id);
}
