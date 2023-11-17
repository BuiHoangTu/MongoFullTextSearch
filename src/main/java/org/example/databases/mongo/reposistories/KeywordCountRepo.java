package org.example.databases.mongo.reposistories;

import org.example.models.KeywordCount;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KeywordCountRepo extends MongoRepository<KeywordCount, String> {
    List<KeywordCount> findAllByWord(Iterable<String> words);
}
