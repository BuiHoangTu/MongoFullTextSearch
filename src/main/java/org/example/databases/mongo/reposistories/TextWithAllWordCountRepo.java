package org.example.databases.mongo.reposistories;

import org.example.models.Text;
import org.example.models.TextWithAllWordCount;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TextWithAllWordCountRepo extends MongoRepository<TextWithAllWordCount, String> {
    /**
     * This method should be used to retrieve `keywordCounts` as it will find
     * a document with not empty `keywordCounts` and `keywordCounts` are maintains
     * across documents
     * @return a document which contains `keywordCounts`
     */
    TextWithAllWordCount findFirst();

    /**
     * Perform a full-text search on document associate with TextWithAllWordCount.
     * @apiNote This method always return results with `keywordCounts = null` to
     * speed up search and reduce memory. To access `keywordCounts`, please use
     * `findFirst`
     * @param text search phrase
     * @param limit maximum result expected
     * @return best suited texts with the search phrase in descending order.
     */
    @Aggregation(pipeline = {
            "{ $match: {$text: {$search: '?0', $language: vi } } }",
            // this field is not necessary in this step
            "{ $unset: 'keywordCounts'}",
            "{ $sort: { score: { $meta: 'textScore' } } }",
            "{ $limit: ?1 }"
    })
    List<TextWithAllWordCount> searchFullText(String text, long limit);
}
