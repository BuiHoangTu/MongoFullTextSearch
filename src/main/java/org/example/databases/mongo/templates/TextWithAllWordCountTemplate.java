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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class TextWithAllWordCountTemplate {
    private final MongoTemplate mongo;
    private final MongoCollection<Document> collection;
    private final Class<TextWithAllWordCount> rootClass = TextWithAllWordCount.class;

    @Autowired
    public TextWithAllWordCountTemplate(MongoTemplate mongo) {
        this.mongo = mongo;
        this.collection = mongo.getCollection(mongo.getCollectionName(this.rootClass));
    }

    public List<WordCount> getWordsCount() {
        Query query = new Query();

        query.addCriteria(Criteria.where("wordCounts")
                // field exist
                .exists(true)
                // field is not null
                .ne(null)
                // field is array
                .type(JsonSchemaObject.Type.ARRAY)
                // size != 0
                .not().size(0)
        );

        var res = mongo.findOne(query, this.rootClass);

        if (res != null) return res.getWordCounts();
        else return Collections.emptyList();
    }

    public void increaseWordCount(Map<String, Number> wordCountIncrement) {
        wordCountIncrement.forEach((word, count) -> {
            UpdateResult res = collection.updateMany(
                    // document has field wordCounts, in which word = input
                    Filters.elemMatch("wordCounts", Filters.eq("word", word)),
                    // increase by count
                    Updates.inc("wordCounts.$[elem].count", count.longValue()),
                    // set option to update all
                    new UpdateOptions().arrayFilters(List.of(Filters.eq("elem.word", word)))
            );

            // if word is not in there (all document is in sync so if it is not in one, it is not in all)
            if (res.getModifiedCount() == 0) {
                var wordCountDocument = new Document()
                        .append("word", word)
                        .append("count", count.longValue());

                collection.updateMany(
                        // update all
                        Filters.empty(),
                        Updates.addToSet("wordCounts", wordCountDocument)
                );
            }

        });
    }
}
