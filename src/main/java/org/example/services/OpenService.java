package org.example.services;

import org.example.databases.mongo.reposistories.ParagraphRepo;
import org.example.databases.mongo.reposistories.TextWithKeywordRepo;
import org.example.databases.mongo.templates.KeywordCountTemplate;
import org.example.models.CommonKeywords;
import org.example.models.KeywordCount;
import org.example.models.Paragraph;
import org.example.models.TextWithKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OpenService {
    private final ParagraphRepo paragraphRepo;
    private final KeywordCountTemplate template;
    private final CommonKeywords commonKeywords;
    private final TextWithKeywordRepo textRepo;

    @Autowired
    public OpenService(ParagraphRepo paragraphRepo, KeywordCountTemplate template, CommonKeywords commonKeywords, TextWithKeywordRepo textRepo) {
        this.paragraphRepo = paragraphRepo;
        this.template = template;
        this.commonKeywords = commonKeywords;
        this.textRepo = textRepo;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        return paragraphRepo.searchFullText(keyText);
    }

    public List<KeywordCount> searchCommonText() {
        return template.getCommonWords();
    }

    public List<TextWithKeyword> searchTextWithKeyword(String text) {
        // remove every words that is common
        List<String> splitText = new java.util.ArrayList<>(Arrays.stream(text.split(" ")).toList());
        splitText.removeIf(word -> commonKeywords.get(word) != null);

        // perform search
        return textRepo.searchFullText(String.join(" ", splitText));
    }
}
