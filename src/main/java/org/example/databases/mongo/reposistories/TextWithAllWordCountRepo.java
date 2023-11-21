package org.example.databases.mongo.reposistories;

import org.example.models.TextWithAllWordCount;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TextWithAllWordCountRepo extends MongoRepository<TextWithAllWordCount, String> {
    /**
     * This method should be used to retrieve `keywordCounts` as it will find
     * a document with not empty `keywordCounts` and `keywordCounts` are maintains
     * across documents
     *
     * @return a document which contains `keywordCounts`
     */
    @Aggregation(pipeline = {
            "{ $match: " +
                "{" +
                    // make sure this field exist
                    "'keywordCounts': { " +
                        "$exists: true," +
                        "$ne: null," +
                        "$not: {$size: 0}" +
                    " }" +
                "} " +
            "}",
            "{ $limit: 1}"
    })
    Optional<TextWithAllWordCount> findFirstWithKeywordCount();

    /**
     * Perform a full-text search on document associate with TextWithAllWordCount.
     *
     * @param text  search phrase
     * @param limit maximum result expected
     * @return sorted of best suited texts with the search phrase in descending order.
     * @apiNote This method always return results with `keywordCounts = null` to
     * speed up search and reduce memory. To access `keywordCounts`, please use
     * `findFirstWithKeywordCount`
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
