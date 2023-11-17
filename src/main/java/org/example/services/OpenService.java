package org.example.services;

import org.example.databases.mongo.reposistories.ParagraphRepo;
import org.example.databases.mongo.templates.KeywordCountTemplate;
import org.example.models.KeywordCount;
import org.example.models.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenService {
    private final ParagraphRepo repo;
    private final KeywordCountTemplate template;

    @Autowired
    public OpenService(ParagraphRepo repo, KeywordCountTemplate template) {
        this.repo = repo;
        this.template = template;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        return repo.searchFullText(keyText);
    }

    public List<KeywordCount> searchCommonText() {
        return template.getCommonWords();
    }

//    public List<KeywordCount>
}
