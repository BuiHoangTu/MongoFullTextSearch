package org.example.databases.mongo.reposistories;

import com.mongodb.lang.NonNull;
import org.example.models.Text;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TextRepo extends MongoRepository<Text, String> {
    @Override
    @NonNull
    <S extends Text> List<S> saveAll(@NonNull Iterable<S> entities);

    @Aggregation(pipeline = {
            "{ $match: {$text: {$search: '?0', $language: vi } } }",
            "{ $sort: { score: { $meta: 'textScore' } } }",
            "{ $limit: 10 }"
    })
    List<Text> searchFullText(String text);
}
