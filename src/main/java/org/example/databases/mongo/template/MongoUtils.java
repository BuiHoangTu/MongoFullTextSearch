package org.example.databases.mongo.template;

import org.example.models.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoUtils {
    private final org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @Autowired
    public MongoUtils(org.springframework.data.mongodb.core.MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        TextQuery query = TextQuery.queryText(new TextCriteria().matchingAny(keyText)).sortByScore();
        List<Paragraph> res = mongoTemplate.find(query, Paragraph.class);
        return res;
    }
}
