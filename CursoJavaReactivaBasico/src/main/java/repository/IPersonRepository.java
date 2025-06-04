package repository;

import model.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

public interface IPersonRepository extends JpaRepository<PersonEntity, String> {
    Mono<PersonEntity> findById(Long id);

}
