package co.com.bancolombia.mongo;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.model.BoxDocument;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<Box, BoxDocument, String, MongoDBRepository>
implements BoxRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Box.class/* change for domain model */));
    }

    @Override
    public Mono<Box> findById(String id) {
        return repository.findById(id).map(this::toEntity);
    }

    @Override
    public Mono<Box> save(Box box) {
        BoxDocument boxDocument = BoxDocument.builder()
                .id(box.getId())
                .name(box.getName())
                .closedAt(box.getClosedAt())
                .closingAmount(box.getClosingAmount())
                .openedAt(box.getOpenedAt())
                .openingAmount(box.getOpeningAmount())
                .status(box.getStatus())
                .currentBalance(box.getCurrentBalance())
                .createdAt(box.getCreatedAt())
                .createdBy(box.getCreatedBy())
                .build();
        return repository.save(boxDocument).map(this::toEntity);
    }
}
