package org.example.databases.mongo.reposistories;

import com.mongodb.lang.NonNull;
import org.example.models.Paragraph;
import org.example.models.TextWithKeyword;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TextWithKeywordRepo extends MongoRepository<TextWithKeyword, String> {
    @Override
    @NonNull
    <S extends TextWithKeyword> List<S> saveAll(@NonNull Iterable<S> entities);

    @Aggregation(pipeline = {
            "{ $match: {$text: {$search: '?0', $language: vi } } }",
            "{ $sort: { score: { $meta: 'textScore' } } }",
            "{ $limit: 10 }"
    })
    List<TextWithKeyword> searchFullText(String text);
}
