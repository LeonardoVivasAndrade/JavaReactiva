package co.com.bancolombia.mongo;

import co.com.bancolombia.mongo.model.BoxDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBRepository extends ReactiveMongoRepository<BoxDocument, String>, ReactiveQueryByExampleExecutor<BoxDocument> {
}
