package org.example.databases.mongo.templates;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.example.models.TextWithAllWordCount;
import org.example.models.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TextWithAllWordCountTemplate {
    private final MongoTemplate mongo;

    @Autowired
    public TextWithAllWordCountTemplate(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public void increaseWordCount(Map<String, Number> wordCountIncrement) {
        MongoCollection<Document> collection = mongo.getCollection(mongo.getCollectionName(TextWithAllWordCount.class));

        wordCountIncrement.forEach((word, count) -> {
            UpdateResult res = collection.updateMany(
                    // document has field wordCounts, in which word = input
                    Filters.elemMatch("wordCounts", Filters.eq("word", word)),
                    // increase by count
                    Updates.inc("wordCounts.$[elem].count", count),
                    // set option to update all
                    new UpdateOptions().arrayFilters(List.of(Filters.eq("elem.word", word)))
            );

            // if word is not in there (all document is in sync so if it is not in one, it is not in all)
            if (res.getModifiedCount() == 0) {
                collection.updateMany(
                        // update all
                        Filters.empty(),
                        Updates.addToSet("wordCounts", new WordCount(null, word, count.longValue()))
                );
            }

        });
    }
}
