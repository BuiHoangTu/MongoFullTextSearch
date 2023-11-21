package org.example.services;

import org.example.databases.mongo.reposistories.ParagraphRepo;
import org.example.databases.mongo.reposistories.TextRepo;
import org.example.databases.mongo.reposistories.TextWithAllWordCountRepo;
import org.example.databases.mongo.templates.KeywordCountTemplate;
import org.example.databases.mongo.templates.TextWithAllWordCountTemplate;
import org.example.models.*;
import org.example.utils.counter.StackCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpenService {
    private static final Logger LOGGER_OPEN_SERVICE = LoggerFactory.getLogger(OpenService.class);
    private final ParagraphRepo paragraphRepo;
    private final KeywordCountTemplate keywordCountTemplate;
    private final CommonKeywords commonKeywords;
    private final TextRepo textRepo;
    private final TextWithAllWordCountRepo textWithAllWordCountRepo;
    private final TextWithAllWordCountTemplate textWithAllWordCountTemplate;

    @Autowired
    public OpenService(ParagraphRepo paragraphRepo, KeywordCountTemplate keywordCountTemplate, CommonKeywords commonKeywords, TextRepo textRepo, TextWithAllWordCountRepo textWithAllWordCountRepo, TextWithAllWordCountTemplate textWithAllWordCountTemplate) {
        this.paragraphRepo = paragraphRepo;
        this.keywordCountTemplate = keywordCountTemplate;
        this.commonKeywords = commonKeywords;
        this.textRepo = textRepo;
        this.textWithAllWordCountRepo = textWithAllWordCountRepo;
        this.textWithAllWordCountTemplate = textWithAllWordCountTemplate;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        return paragraphRepo.searchFullText(keyText);
    }

    public List<WordCount> searchCommonText() {
        return keywordCountTemplate.getCommonWords();
    }

    public List<Text> searchTextWithKeyword(String text) {
        // remove every words that is common
        List<String> splitText = new java.util.ArrayList<>(Arrays.stream(text.split(" ")).toList());
        splitText.removeIf(word -> commonKeywords.get(word) != null);

        // perform search
        return textRepo.searchFullText(String.join(" ", splitText));
    }

    public List<TextWithAllWordCount> searchTextWithAllWordCount(String text) {
        final long COMMON_PERCENTAGE = 50;
        final long MINIMUM_SEARCH_KEY = 3;
        final long MAX_RESULT = 10;

        var optionalTextWithAllWordCount = textWithAllWordCountRepo.findFirstWithKeywordCount();
        if (optionalTextWithAllWordCount.isEmpty()) {
            LOGGER_OPEN_SERVICE.error("Database can't find keywordCounts, search reduce is going to be performed");
        } else {
            // get a count
            var listWordCount = optionalTextWithAllWordCount.get().getWordCounts();

            // reduce to list of common words
            List<String> commonWords = listWordCount
                    // use c2 - c1 to get descending order
                    .stream().sorted((c1, c2) -> {
                        long res = c2.getCount() - c1.getCount();
                        if (res > 0) return 1;
                        if (res < 0) return -1;
                        return 0;
                    })
                    // only get most common words
                    .limit(((long) listWordCount.size() * COMMON_PERCENTAGE) / 100)
                    // map to a list string
                    .map(WordCount::getWord)
                    .toList();

            // remove every words that is common
            List<String> splitText = new java.util.ArrayList<>(Arrays.stream(text.split(" ")).toList());

            // if key-phrase to short, just search it
            if (splitText.size() > MINIMUM_SEARCH_KEY) {
                remove_common:
                for (var word : commonWords) {
                    // remove all that word
                    while (splitText.remove(word)) {
                        if (splitText.size() <= MINIMUM_SEARCH_KEY) break remove_common;
                    }
                }
            }

            text = String.join(" ", splitText);
        }


        // perform search
        return textWithAllWordCountRepo.searchFullText(text, MAX_RESULT);
    }

    public void saveText(List<String> texts) {
        // retrieve words count from db
        var listWordCount = textWithAllWordCountRepo.findFirstWithKeywordCount()
                .orElse(new TextWithAllWordCount(null, null, Collections.emptyList()))
                .getWordCounts();

        // add to text before push db
        List<TextWithAllWordCount> textsWithAllWordCount = texts.stream().map(text -> new TextWithAllWordCount(null, text, listWordCount)).toList();

        textWithAllWordCountRepo.insert(textsWithAllWordCount);

        StackCounter<WordCount> extraWordCounter = new StackCounter<>(listWordCount);

        // count words
        for (var text : texts) {
            Set<String> uniqueWords = new HashSet<>(List.of(text.split(" ")));

            // same word in same document is counted only one
            extraWordCounter.addAll(uniqueWords.stream().map(word -> new WordCount(null, word, 1)).toList());
        }

        // increase word count for every existing document
        textWithAllWordCountTemplate.increaseWordCount(extraWordCounter.stream().collect(Collectors.toMap(WordCount::getWord, WordCount::getCount)));
    }
}
