package co.com.bancolombia.mongo;

import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.model.MovementDocument;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoMovementRepositoryAdapter extends AdapterOperations<Movement, MovementDocument, String, MongoDBMovementRepository>
implements MovementRepository
{

    public MongoMovementRepositoryAdapter(MongoDBMovementRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Movement.class/* change for domain model */));
    }

    @Override
    public Mono<Movement> save(Movement movement) {
        MovementDocument movementDocument = MovementDocument.builder()
                .id(movement.getId())
                .movementId(movement.getMovementId())
                .boxId(movement.getBoxId())
                .date(movement.getDate())
                .type(movement.getType())
                .amount(movement.getAmount())
                .currency(movement.getCurrency())
                .description(movement.getDescription())
                .build();
        return repository.save(movementDocument).map(this::toEntity);
    }
}
