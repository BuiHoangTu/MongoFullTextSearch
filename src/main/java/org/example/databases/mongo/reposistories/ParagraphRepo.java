package org.example.databases.mongo.reposistories;

import org.example.models.Paragraph;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParagraphRepo extends MongoRepository<Paragraph, String> {
    @Override
    <S extends Paragraph> List<S> saveAll(Iterable<S> entities);
}
