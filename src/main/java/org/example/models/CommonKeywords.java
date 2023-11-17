package org.example.models;

import org.example.databases.mongo.templates.KeywordCountTemplate;
import org.example.utils.counter.StackCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonKeywords extends StackCounter<KeywordCount> {
    private final KeywordCountTemplate keywordCountTemplate;

    @Autowired
    public CommonKeywords(KeywordCountTemplate keywordCountTemplate) {
        this.keywordCountTemplate = keywordCountTemplate;
        this.loadFromDatabase();
    }

    public void loadFromDatabase() {
        this.addAll(this.keywordCountTemplate.getCommonWords());
    }
}
