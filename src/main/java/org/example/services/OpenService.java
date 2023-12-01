package org.example.services;

import org.example.databases.mongo.reposistories.TextWithAllWordCountRepo;
import org.example.databases.mongo.templates.TextWithAllWordCountTemplate;
import org.example.models.TextWithAllWordCount;
import org.example.models.WordCount;
import org.example.utils.counter.UniqueCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OpenService {
    private static final Logger LOGGER_OPEN_SERVICE = LoggerFactory.getLogger(OpenService.class);
    private final TextWithAllWordCountRepo textWithAllWordCountRepo;
    private final TextWithAllWordCountTemplate textWithAllWordCountTemplate;

    @Autowired
    public OpenService(TextWithAllWordCountRepo textWithAllWordCountRepo, TextWithAllWordCountTemplate textWithAllWordCountTemplate) {
        this.textWithAllWordCountRepo = textWithAllWordCountRepo;
        this.textWithAllWordCountTemplate = textWithAllWordCountTemplate;
    }

    public Collection<TextWithAllWordCount> searchTextWithAllWordCount(String text) {
        final long COMMON_PERCENTAGE = 50;
        final long MINIMUM_SEARCH_KEY = 3;
        final long MAX_RESULT = 10;
        final long MIN_RESULT = 5;

        Set<TextWithAllWordCount> finalRes = new HashSet<>();

        var wordsCount = textWithAllWordCountTemplate.getWordsCount();
        if (wordsCount.isEmpty()) {
            LOGGER_OPEN_SERVICE.error("Database can't find keywordCounts, search reduce is going to be performed");
        } else {
            // reduce to list of common words
            List<String> commonWords = wordsCount
                    // use c2 - c1 to get descending order
                    .stream().sorted((c1, c2) -> {
                        long lCpm = c2.getCount() - c1.getCount();
                        if (lCpm > 0) return 1;
                        if (lCpm < 0) return -1;
                        return 0;
                    })
                    // only get most common words
                    .limit(((long) wordsCount.size() * COMMON_PERCENTAGE) / 100)
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

                LOGGER_OPEN_SERVICE.info("filtered text:");
                LOGGER_OPEN_SERVICE.info(splitText.toString());
            }

            // perform filtered search
            String filteredText = String.join(" ", splitText);
            List<TextWithAllWordCount> filteredRes = textWithAllWordCountRepo.searchFullText(filteredText, MAX_RESULT);
            finalRes.addAll(filteredRes);
            if (finalRes.size() > MIN_RESULT) return finalRes;
        }


        // perform original search
        var nonFilteredRes = textWithAllWordCountRepo.searchFullText(text, MAX_RESULT - finalRes.size());
        finalRes.addAll(nonFilteredRes);
        if (finalRes.size() > MIN_RESULT) return finalRes;

        // perform regex
        String regexText = "*" + text.replaceAll(" ", "* *") + "*";
        var regexRes = textWithAllWordCountRepo.searchFullText(regexText, MAX_RESULT - finalRes.size());
        finalRes.addAll(regexRes);
        return finalRes;
    }

    public void saveText(List<String> texts) {
        // retrieve words count from db
        var listWordCount = textWithAllWordCountTemplate.getWordsCount();

        // add to text before push db
        List<TextWithAllWordCount> textsWithAllWordCount = texts.stream().map(text -> new TextWithAllWordCount(null, text, listWordCount)).toList();

        textWithAllWordCountRepo.insert(textsWithAllWordCount);

        // empty word counter
        UniqueCounter<String> extraWordCounter = new UniqueCounter<>();

        // count words
        for (var text : texts) {
            // put all into set to get unique strings
            Set<String> uniqueWords = new HashSet<>(List.of(text.split(" ")));

            // same word in same document is counted only one
            uniqueWords.forEach(extraWordCounter::count);
        }

        LOGGER_OPEN_SERVICE.info("New words count:");
        LOGGER_OPEN_SERVICE.info(extraWordCounter.toString());

        // increase word count for every existing document
        textWithAllWordCountTemplate.increaseWordCount(extraWordCounter);
    }
}
