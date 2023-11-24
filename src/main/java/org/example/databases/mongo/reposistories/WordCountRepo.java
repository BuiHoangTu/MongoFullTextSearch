package org.example.databases.mongo.reposistories;

import org.example.models.WordCount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordCountRepo extends MongoRepository<WordCount, String> {
    List<WordCount> findAllByWord(Iterable<String> words);
}
