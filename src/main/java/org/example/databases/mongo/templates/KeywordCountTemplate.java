package org.example.databases.mongo.templates;

import org.example.models.KeywordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeywordCountTemplate {
    private static final int COMMON_PERCENTAGE = 50;

    private final MongoTemplate mongo;

    @Autowired
    public KeywordCountTemplate(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public List<KeywordCount> getCommonWords() {
        long total = mongo.count(new Query(), KeywordCount.class);

        long commonQuantity = total * COMMON_PERCENTAGE / 100;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC, "count"),
                Aggregation.limit(commonQuantity)
        );

        AggregationResults<KeywordCount> res = mongo.aggregate(aggregation, "keywords_count", KeywordCount.class);

        return res.getMappedResults();
    }
}
