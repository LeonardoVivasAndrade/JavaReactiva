package service.impl;

import model.PersonEntity;
import service.IPersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements IPersonService {
    private final IPersonService personService;

    @Override
    public Mono<PersonEntity> getById(Long id) {
        var person = this.personService.getById(id);

        return person
                .doOnNext(p -> System.out.println("Persona encontrada, id: " + id))
                .doOnError(e -> System.out.println("Persona no encontrada, id: " + id))
                .then(Mono.empty());
    }
}
