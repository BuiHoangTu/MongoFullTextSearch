package org.example.services;

import org.example.databases.mongo.reposistories.ParagraphRepo;
import org.example.databases.mongo.reposistories.TextRepo;
import org.example.databases.mongo.reposistories.TextWithAllWordCountRepo;
import org.example.databases.mongo.templates.KeywordCountTemplate;
import org.example.models.*;
import org.example.utils.counter.ICounter;
import org.example.utils.counter.StackCounter;
import org.example.utils.counter.UniqueCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class OpenService {
    private final ParagraphRepo paragraphRepo;
    private final KeywordCountTemplate template;
    private final CommonKeywords commonKeywords;
    private final TextRepo textRepo;
    private final TextWithAllWordCountRepo textWithAllWordCountRepo;

    @Autowired
    public OpenService(ParagraphRepo paragraphRepo, KeywordCountTemplate template, CommonKeywords commonKeywords, TextRepo textRepo, TextWithAllWordCountRepo textWithAllWordCountRepo) {
        this.paragraphRepo = paragraphRepo;
        this.template = template;
        this.commonKeywords = commonKeywords;
        this.textRepo = textRepo;
        this.textWithAllWordCountRepo = textWithAllWordCountRepo;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        return paragraphRepo.searchFullText(keyText);
    }

    public List<KeywordCount> searchCommonText() {
        return template.getCommonWords();
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

        // get a count
        var listWordCount = textWithAllWordCountRepo.findFirst().getKeywordCounts();

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
                .map(KeywordCount::getWord)
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

        // perform search
        return textWithAllWordCountRepo.searchFullText(String.join(" ", splitText), 10);
    }
}
